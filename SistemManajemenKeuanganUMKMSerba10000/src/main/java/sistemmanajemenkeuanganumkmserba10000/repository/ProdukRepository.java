
package sistemmanajemenkeuanganumkmserba10000.repository;

import sistemmanajemenkeuanganumkmserba10000.repository.DatabaseConnection;
import sistemmanajemenkeuanganumkmserba10000.entity.Produk;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukRepository {

    public List<Produk> findAll() throws SQLException {
        List<Produk> daftarProduk = new ArrayList<>();

        String sql = "SELECT * FROM produk ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Produk produk = new Produk(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getBigDecimal("harga")
                );

                daftarProduk.add(produk);
            }
        }

        return daftarProduk;
    }

    public Produk findById(int id) throws SQLException {
        String sql = "SELECT * FROM produk WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Produk(
                            rs.getInt("id"),
                            rs.getString("nama"),
                            rs.getBigDecimal("harga")
                    );
                }
            }
        }

        return null;
    }

    public void save(Produk produk) throws SQLException {
        String sql = """
                INSERT INTO produk (nama, harga)
                VALUES (?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, produk.getNama());
            ps.setBigDecimal(2, produk.getHarga());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    produk.setId(rs.getInt(1));
                }
            }
        }
    }

    public boolean update(Produk produk) throws SQLException {
        String sql = """
                UPDATE produk
                SET nama = ?, harga = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produk.getNama());
            ps.setBigDecimal(2, produk.getHarga());
            ps.setInt(3, produk.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM produk WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}
