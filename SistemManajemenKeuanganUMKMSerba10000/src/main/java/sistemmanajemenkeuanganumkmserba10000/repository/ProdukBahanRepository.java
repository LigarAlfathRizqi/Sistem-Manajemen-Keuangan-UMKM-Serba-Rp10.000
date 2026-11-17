package sistemmanajemenkeuanganumkmserba10000.repository;

import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;
import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.ProdukBahan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdukBahanRepository {

    private static final String SELECT_ALL = """
            SELECT pb.id AS pb_id, pb.jumlah_pemakaian,
                   p.id AS produk_id, p.nama AS produk_nama, p.harga AS produk_harga,
                   b.id AS bahan_id, b.nama AS bahan_nama, b.satuan AS bahan_satuan,
                   b.harga AS bahan_harga
            FROM produk_bahan pb
            JOIN produk p ON p.id = pb.produk_id
            JOIN bahan b ON b.id = pb.bahan_id
            """;

    public List<ProdukBahan> findAll() throws SQLException {
        return query(SELECT_ALL + " ORDER BY p.nama, b.nama", null);
    }

    public List<ProdukBahan> findByProdukId(int produkId) throws SQLException {
        return query(SELECT_ALL + " WHERE p.id = ? ORDER BY b.nama", produkId);
    }

    public void replaceForProduk(int produkId, List<ProdukBahan> bahanProduk) throws SQLException {
        String deleteSql = "DELETE FROM produk_bahan WHERE produk_id = ?";
        String insertSql = "INSERT INTO produk_bahan (produk_id, bahan_id, jumlah_pemakaian) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try (PreparedStatement delete = connection.prepareStatement(deleteSql);
                 PreparedStatement insert = connection.prepareStatement(insertSql)) {
                delete.setInt(1, produkId);
                delete.executeUpdate();
                for (ProdukBahan relation : bahanProduk) {
                    insert.setInt(1, produkId);
                    insert.setInt(2, relation.getBahan().getId());
                    insert.setBigDecimal(3, relation.getJumlahPemakaian());
                    insert.addBatch();
                }
                insert.executeBatch();
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    public void deleteByProdukId(int produkId) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM produk_bahan WHERE produk_id = ?")) {
            statement.setInt(1, produkId);
            statement.executeUpdate();
        }
    }

    private List<ProdukBahan> query(String sql, Integer produkId) throws SQLException {
        List<ProdukBahan> result = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (produkId != null) {
                statement.setInt(1, produkId);
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Produk produk = new Produk(rs.getInt("produk_id"),
                            rs.getString("produk_nama"), rs.getBigDecimal("produk_harga"));
                    Bahan bahan = new Bahan(rs.getInt("bahan_id"),
                            rs.getString("bahan_nama"), rs.getString("bahan_satuan"),
                            rs.getBigDecimal("bahan_harga"));
                    result.add(new ProdukBahan(rs.getInt("pb_id"), produk, bahan,
                            rs.getBigDecimal("jumlah_pemakaian")));
                }
            }
        }
        return result;
    }
}
