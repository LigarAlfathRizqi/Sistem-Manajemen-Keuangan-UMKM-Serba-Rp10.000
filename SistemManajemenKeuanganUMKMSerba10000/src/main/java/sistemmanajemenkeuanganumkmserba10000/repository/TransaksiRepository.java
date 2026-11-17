package sistemmanajemenkeuanganumkmserba10000.repository;

import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.Transaksi;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransaksiRepository {

    private static final String SELECT = """
            SELECT t.id, t.tanggal, t.jumlah, t.keterangan,
                   t.harga_jual_satuan, t.modal_satuan,
                   p.id AS produk_id, p.nama AS produk_nama, p.harga AS produk_harga
            FROM transaksi t
            JOIN produk p ON p.id = t.produk_id
            """;

    public List<Transaksi> findAll() throws SQLException {
        return query(SELECT + " ORDER BY t.tanggal DESC, t.id DESC", null, null);
    }

    public List<Transaksi> findByTanggal(LocalDate tanggal) throws SQLException {
        return query(SELECT + " WHERE t.tanggal = ? ORDER BY t.id DESC", tanggal, null);
    }

    public List<Transaksi> findByRange(LocalDate mulai, LocalDate sampai) throws SQLException {
        return query(SELECT + " WHERE t.tanggal BETWEEN ? AND ? ORDER BY t.tanggal DESC, t.id DESC",
                mulai, sampai);
    }

    public Transaksi findById(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT + " WHERE t.id = ?")) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /**
     * Adds a sale to the existing product/day row, matching the application rule
     * that repeated sales of the same product on one day are accumulated.
     */
    public void save(Transaksi transaksi) throws SQLException {
        String sql = """
                INSERT INTO transaksi
                    (tanggal, produk_id, jumlah, harga_jual_satuan, modal_satuan, keterangan)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    jumlah = jumlah + VALUES(jumlah),
                    keterangan = CASE
                        WHEN VALUES(keterangan) = '' THEN keterangan
                        WHEN keterangan = '' THEN VALUES(keterangan)
                        ELSE CONCAT(keterangan, ' | ', VALUES(keterangan))
                    END
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, transaksi);
            statement.executeUpdate();
            try (PreparedStatement find = connection.prepareStatement(
                    "SELECT id FROM transaksi WHERE tanggal = ? AND produk_id = ?")) {
                find.setDate(1, Date.valueOf(transaksi.getTanggal()));
                find.setInt(2, transaksi.getProduk().getId());
                try (ResultSet rs = find.executeQuery()) {
                    if (rs.next()) {
                        transaksi.setId(rs.getInt(1));
                    }
                }
            }
        }
    }

    public boolean update(Transaksi transaksi) throws SQLException {
        String sql = """
                UPDATE transaksi
                SET tanggal = ?, produk_id = ?, jumlah = ?, harga_jual_satuan = ?,
                    modal_satuan = ?, keterangan = ?
                WHERE id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, transaksi);
            statement.setInt(7, transaksi.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM transaksi WHERE id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private List<Transaksi> query(String sql, LocalDate firstDate, LocalDate secondDate)
            throws SQLException {
        List<Transaksi> result = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (firstDate != null) {
                statement.setDate(1, Date.valueOf(firstDate));
            }
            if (secondDate != null) {
                statement.setDate(2, Date.valueOf(secondDate));
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        }
        return result;
    }

    private void bind(PreparedStatement statement, Transaksi transaksi) throws SQLException {
        statement.setDate(1, Date.valueOf(transaksi.getTanggal()));
        statement.setInt(2, transaksi.getProduk().getId());
        statement.setInt(3, transaksi.getJumlah());
        statement.setBigDecimal(4, transaksi.getHargaJualSatuan());
        statement.setBigDecimal(5, transaksi.getModalSatuan());
        statement.setString(6, transaksi.getKeterangan() == null ? "" : transaksi.getKeterangan().trim());
    }

    private Transaksi map(ResultSet rs) throws SQLException {
        Produk produk = new Produk(rs.getInt("produk_id"), rs.getString("produk_nama"),
                rs.getBigDecimal("produk_harga"));
        return new Transaksi(rs.getInt("id"), rs.getDate("tanggal").toLocalDate(),
                produk, rs.getInt("jumlah"), rs.getString("keterangan"),
                rs.getBigDecimal("harga_jual_satuan"), rs.getBigDecimal("modal_satuan"));
    }
}
