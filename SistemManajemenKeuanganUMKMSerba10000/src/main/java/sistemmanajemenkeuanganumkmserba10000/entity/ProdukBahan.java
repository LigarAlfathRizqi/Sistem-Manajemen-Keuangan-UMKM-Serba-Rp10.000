package sistemmanajemenkeuanganumkmserba10000.entity;

public class ProdukBahan {

    private int id;
    private Produk produk;
    private Bahan bahan;

    public ProdukBahan() {
    }

    public ProdukBahan(int id, Produk produk, Bahan bahan) {
        this.id = id;
        this.produk = produk;
        this.bahan = bahan;
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
}
