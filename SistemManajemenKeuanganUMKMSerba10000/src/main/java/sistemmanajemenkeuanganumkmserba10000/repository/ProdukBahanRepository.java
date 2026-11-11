
package sistemmanajemenkeuanganumkmserba10000.repository;

import sistemmanajemenkeuanganumkmserba10000.repository.DatabaseConnection;
import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;
import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.ProdukBahan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukBahanRepository {

    public List<ProdukBahan> findAll() throws SQLException {
        List<ProdukBahan> daftar = new ArrayList<>();

        String sql = """
                SELECT
                    pb.id AS pb_id,
                    p.id AS produk_id,
                    p.nama AS produk_nama,
                    p.harga AS produk_harga,
                    b.id AS bahan_id,
                    b.nama AS bahan_nama,
                    b.harga AS bahan_harga
                FROM produk_bahan pb
                JOIN produk p ON pb.produk_id = p.id
                JOIN bahan b ON pb.bahan_id = b.id
                ORDER BY pb.id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftar.add(mapResultSet(rs));
            }
        }

        return daftar;
    }

    public List<ProdukBahan> findByProdukId(
            int produkId) throws SQLException {

        List<ProdukBahan> daftar = new ArrayList<>();

        String sql = """
                SELECT
                    pb.id AS pb_id,
                    p.id AS produk_id,
                    p.nama AS produk_nama,
                    p.harga AS produk_harga,
                    b.id AS bahan_id,
                    b.nama AS bahan_nama,
                    b.harga AS bahan_harga
                FROM produk_bahan pb
                JOIN produk p ON pb.produk_id = p.id
                JOIN bahan b ON pb.bahan_id = b.id
                WHERE p.id = ?
                ORDER BY pb.id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, produkId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    daftar.add(mapResultSet(rs));
                }
            }
        }

        return daftar;
    }

    public void save(ProdukBahan produkBahan) throws SQLException {
        String sql = """
                INSERT INTO produk_bahan (produk_id, bahan_id)
                VALUES (?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, produkBahan.getProduk().getId());
            ps.setInt(2, produkBahan.getBahan().getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    produkBahan.setId(rs.getInt(1));
                }
            }
        }
    }

    public void deleteByProdukId(int produkId) throws SQLException {
        String sql = """
                DELETE FROM produk_bahan
                WHERE produk_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, produkId);
            ps.executeUpdate();
        }
    }

    private ProdukBahan mapResultSet(ResultSet rs)
            throws SQLException {

        Produk produk = new Produk(
                rs.getInt("produk_id"),
                rs.getString("produk_nama"),
                rs.getBigDecimal("produk_harga")
        );

        Bahan bahan = new Bahan(
                rs.getInt("bahan_id"),
                rs.getString("bahan_nama"),
                rs.getBigDecimal("bahan_harga")
        );

        return new ProdukBahan(
                rs.getInt("pb_id"),
                produk,
                bahan
        );
    }
}
