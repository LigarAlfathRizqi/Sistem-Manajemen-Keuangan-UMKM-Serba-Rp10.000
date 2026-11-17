package sistemmanajemenkeuanganumkmserba10000.repository;

import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BahanRepository {

    public List<Bahan> findAll() throws SQLException {
        List<Bahan> result = new ArrayList<>();
        String sql = "SELECT id, nama, satuan, harga FROM bahan ORDER BY nama";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        }
        return result;
    }

    public Bahan findById(int id) throws SQLException {
        String sql = "SELECT id, nama, satuan, harga FROM bahan WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public void save(Bahan bahan) throws SQLException {
        String sql = "INSERT INTO bahan (nama, satuan, harga) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bahan.getNama().trim());
            statement.setString(2, bahan.getSatuan().trim());
            statement.setBigDecimal(3, bahan.getHarga());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    bahan.setId(keys.getInt(1));
                }
            }
        }
    }

    public boolean update(Bahan bahan) throws SQLException {
        String sql = "UPDATE bahan SET nama = ?, satuan = ?, harga = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bahan.getNama().trim());
            statement.setString(2, bahan.getSatuan().trim());
            statement.setBigDecimal(3, bahan.getHarga());
            statement.setInt(4, bahan.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM bahan WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Bahan map(ResultSet rs) throws SQLException {
        return new Bahan(rs.getInt("id"), rs.getString("nama"),
                rs.getString("satuan"), rs.getBigDecimal("harga"));
    }
}
