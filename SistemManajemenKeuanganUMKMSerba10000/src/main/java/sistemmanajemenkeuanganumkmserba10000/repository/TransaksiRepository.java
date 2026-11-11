
package sistemmanajemenkeuanganumkmserba10000.repository;

import sistemmanajemenkeuanganumkmserba10000.repository .DatabaseConnection;
import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.Transaksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiRepository {

    public List<Transaksi> findAll() throws SQLException {
        List<Transaksi> daftarTransaksi = new ArrayList<>();

        String sql = """
                SELECT
                    t.id,
                    t.tanggal,
                    t.jumlah,
                    t.keterangan,
                    p.id AS produk_id,
                    p.nama AS produk_nama,
                    p.harga AS produk_harga
                FROM transaksi t
                JOIN produk p ON t.produk_id = p.id
                ORDER BY t.tanggal DESC, t.id DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftarTransaksi.add(mapResultSet(rs));
            }
        }

        return daftarTransaksi;
    }

    public Transaksi findById(int id) throws SQLException {
        String sql = """
                SELECT
                    t.id,
                    t.tanggal,
                    t.jumlah,
                    t.keterangan,
                    p.id AS produk_id,
                    p.nama AS produk_nama,
                    p.harga AS produk_harga
                FROM transaksi t
                JOIN produk p ON t.produk_id = p.id
                WHERE t.id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }

        return null;
    }

    public List<Transaksi> findByTanggal(
            java.time.LocalDate tanggal) throws SQLException {

        List<Transaksi> daftarTransaksi = new ArrayList<>();

        String sql = """
                SELECT
                    t.id,
                    t.tanggal,
                    t.jumlah,
                    t.keterangan,
                    p.id AS produk_id,
                    p.nama AS produk_nama,
                    p.harga AS produk_harga
                FROM transaksi t
                JOIN produk p ON t.produk_id = p.id
                WHERE t.tanggal = ?
                ORDER BY t.id DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(tanggal));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    daftarTransaksi.add(mapResultSet(rs));
                }
            }
        }

        return daftarTransaksi;
    }

    public void save(Transaksi transaksi) throws SQLException {
        String sql = """
                INSERT INTO transaksi
                    (tanggal, produk_id, jumlah, keterangan)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(transaksi.getTanggal()));
            ps.setInt(2, transaksi.getProduk().getId());
            ps.setInt(3, transaksi.getJumlah());
            ps.setString(4, transaksi.getKeterangan());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    transaksi.setId(rs.getInt(1));
                }
            }
        }
    }

    public boolean update(Transaksi transaksi) throws SQLException {
        String sql = """
                UPDATE transaksi
                SET tanggal = ?,
                    produk_id = ?,
                    jumlah = ?,
                    keterangan = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(transaksi.getTanggal()));
            ps.setInt(2, transaksi.getProduk().getId());
            ps.setInt(3, transaksi.getJumlah());
            ps.setString(4, transaksi.getKeterangan());
            ps.setInt(5, transaksi.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM transaksi WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private Transaksi mapResultSet(ResultSet rs)
            throws SQLException {

        Produk produk = new Produk(
                rs.getInt("produk_id"),
                rs.getString("produk_nama"),
                rs.getBigDecimal("produk_harga")
        );

        return new Transaksi(
                rs.getInt("id"),
                rs.getDate("tanggal").toLocalDate(),
                produk,
                rs.getInt("jumlah"),
                rs.getString("keterangan")
        );
    }
}
