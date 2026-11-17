package sistemmanajemenkeuanganumkmserba10000.entity;

import java.math.BigDecimal;

public class Bahan {

    private int id;
    private String nama;
    private String satuan;
    private BigDecimal harga;

    public Bahan() {
    }

    public Bahan(int id, String nama, BigDecimal harga) {
        this(id, nama, "unit", harga);
    }

    public Bahan(int id, String nama, String satuan, BigDecimal harga) {
        this.id = id;
        this.nama = nama;
        this.satuan = satuan;
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

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }
}
