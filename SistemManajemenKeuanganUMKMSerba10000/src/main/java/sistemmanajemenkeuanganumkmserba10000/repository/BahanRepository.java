
package sistemmanajemenkeuanganumkmserba10000.repository;

import sistemmanajemenkeuanganumkmserba10000.repository.DatabaseConnection;
import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BahanRepository {

    public List<Bahan> findAll() throws SQLException {
        List<Bahan> daftarBahan = new ArrayList<>();

        String sql = "SELECT * FROM bahan ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bahan bahan = new Bahan(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getBigDecimal("harga")
                );

                daftarBahan.add(bahan);
            }
        }

        return daftarBahan;
    }

    public Bahan findById(int id) throws SQLException {
        String sql = "SELECT * FROM bahan WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Bahan(
                            rs.getInt("id"),
                            rs.getString("nama"),
                            rs.getBigDecimal("harga")
                    );
                }
            }
        }

        return null;
    }

    public void save(Bahan bahan) throws SQLException {
        String sql = "INSERT INTO bahan (nama, harga) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, bahan.getNama());
            ps.setBigDecimal(2, bahan.getHarga());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bahan.setId(rs.getInt(1));
                }
            }
        }
    }

    public boolean update(Bahan bahan) throws SQLException {
        String sql = """
                UPDATE bahan
                SET nama = ?, harga = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bahan.getNama());
            ps.setBigDecimal(2, bahan.getHarga());
            ps.setInt(3, bahan.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM bahan WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}
