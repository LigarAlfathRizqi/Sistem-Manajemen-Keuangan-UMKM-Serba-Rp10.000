package sistemmanajemenkeuanganumkmserba10000.repository;

import sistemmanajemenkeuanganumkmserba10000.entity.Produk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdukRepository {

    public List<Produk> findAll() throws SQLException {
        List<Produk> result = new ArrayList<>();
        String sql = "SELECT id, nama, harga FROM produk ORDER BY nama";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        }
        return result;
    }

    public Produk findById(int id) throws SQLException {
        String sql = "SELECT id, nama, harga FROM produk WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public void save(Produk produk) throws SQLException {
        String sql = "INSERT INTO produk (nama, harga) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, produk.getNama().trim());
            statement.setBigDecimal(2, produk.getHarga());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    produk.setId(keys.getInt(1));
                }
            }
        }
    }

    public boolean update(Produk produk) throws SQLException {
        String sql = "UPDATE produk SET nama = ?, harga = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, produk.getNama().trim());
            statement.setBigDecimal(2, produk.getHarga());
            statement.setInt(3, produk.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try (PreparedStatement deleteRecipe = connection.prepareStatement(
                    "DELETE FROM produk_bahan WHERE produk_id = ?");
                 PreparedStatement deleteProduct = connection.prepareStatement(
                    "DELETE FROM produk WHERE id = ?")) {
                deleteRecipe.setInt(1, id);
                deleteRecipe.executeUpdate();
                deleteProduct.setInt(1, id);
                boolean deleted = deleteProduct.executeUpdate() > 0;
                connection.commit();
                return deleted;
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    private Produk map(ResultSet rs) throws SQLException {
        return new Produk(rs.getInt("id"), rs.getString("nama"), rs.getBigDecimal("harga"));
    }
}
