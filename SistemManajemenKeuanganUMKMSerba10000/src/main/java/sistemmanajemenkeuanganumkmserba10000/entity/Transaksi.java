package sistemmanajemenkeuanganumkmserba10000.entity;

import java.time.LocalDate;

public class Transaksi {

    private int id;
    private LocalDate tanggal;
    private Produk produk;
    private int jumlah;
    private String keterangan;

    public Transaksi() {
    }

    public Transaksi(int id, LocalDate tanggal, Produk produk, int jumlah, String keterangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.produk = produk;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
