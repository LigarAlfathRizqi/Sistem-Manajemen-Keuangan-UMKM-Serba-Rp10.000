package sistemmanajemenkeuanganumkmserba10000.entity;

import java.math.BigDecimal;

public class Produk {

    private int id;
    private String nama;
    private BigDecimal harga;

    public Produk() {
    }

    public Produk(int id, String nama, BigDecimal harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }
}
