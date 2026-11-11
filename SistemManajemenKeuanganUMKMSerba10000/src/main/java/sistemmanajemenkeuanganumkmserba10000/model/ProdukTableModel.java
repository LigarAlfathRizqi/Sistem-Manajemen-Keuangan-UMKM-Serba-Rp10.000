
package sistemmanajemenkeuanganumkmserba10000.model;

import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.ProdukBahan;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdukTableModel extends AbstractTableModel {

    private final String[] kolom = {
        "Nama Produk",
        "Harga",
        "Bahan & Barang",
        "Modal",
        "Laba"
    };

    private List<Produk> daftarProduk = new ArrayList<>();
    private List<ProdukBahan> daftarProdukBahan = new ArrayList<>();

    public void setData(
            List<Produk> produk,
            List<ProdukBahan> produkBahan) {

        daftarProduk = new ArrayList<>(produk);
        daftarProdukBahan = new ArrayList<>(produkBahan);

        fireTableDataChanged();
    }

    public BigDecimal hitungModal(Produk produk) {
        BigDecimal modal = BigDecimal.ZERO;

        for (ProdukBahan pb : daftarProdukBahan) {
            if (pb.getProduk().getId() == produk.getId()) {
                modal = modal.add(pb.getBahan().getHarga());
            }
        }

        return modal;
    }

    public BigDecimal hitungLaba(Produk produk) {
        return produk.getHarga().subtract(hitungModal(produk));
    }

    private String getNamaBahan(Produk produk) {
        List<String> namaBahan = new ArrayList<>();

        for (ProdukBahan pb : daftarProdukBahan) {
            if (pb.getProduk().getId() == produk.getId()) {
                namaBahan.add(pb.getBahan().getNama());
            }
        }

        return String.join(", ", namaBahan);
    }

    public Produk getProdukAt(int row) {
        return daftarProduk.get(row);
    }

    @Override
    public int getRowCount() {
        return daftarProduk.size();
    }

    @Override
    public int getColumnCount() {
        return kolom.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Produk produk = daftarProduk.get(row);

        switch (column) {
            case 0:
                return produk.getNama();
            case 1:
                return RupiahUtil.format(produk.getHarga());
            case 2:
                return getNamaBahan(produk);
            case 3:
                return RupiahUtil.format(hitungModal(produk));
            case 4:
                return RupiahUtil.format(hitungLaba(produk));
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
