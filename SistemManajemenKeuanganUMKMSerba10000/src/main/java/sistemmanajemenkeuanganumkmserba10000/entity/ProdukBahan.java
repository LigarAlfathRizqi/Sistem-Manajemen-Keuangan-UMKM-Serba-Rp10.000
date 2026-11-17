package sistemmanajemenkeuanganumkmserba10000.entity;

import java.math.BigDecimal;

public class ProdukBahan {

    private int id;
    private Produk produk;
    private Bahan bahan;
    private BigDecimal jumlahPemakaian;

    public ProdukBahan() {
    }

    public ProdukBahan(int id, Produk produk, Bahan bahan) {
        this(id, produk, bahan, BigDecimal.ONE);
    }

    public ProdukBahan(int id, Produk produk, Bahan bahan, BigDecimal jumlahPemakaian) {
        this.id = id;
        this.produk = produk;
        this.bahan = bahan;
        this.jumlahPemakaian = jumlahPemakaian;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public Bahan getBahan() {
        return bahan;
    }

    public void setBahan(Bahan bahan) {
        this.bahan = bahan;
    }

    public BigDecimal getJumlahPemakaian() {
        return jumlahPemakaian;
    }

    public void setJumlahPemakaian(BigDecimal jumlahPemakaian) {
        this.jumlahPemakaian = jumlahPemakaian;
    }
}
