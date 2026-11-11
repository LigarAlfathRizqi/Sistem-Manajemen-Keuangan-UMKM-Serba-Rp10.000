
package sistemmanajemenkeuanganumkmserba10000.model;

import sistemmanajemenkeuanganumkmserba10000.entity.Transaksi;
import sistemmanajemenkeuanganumkmserba10000.entity.ProdukBahan;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LaporanTableModel extends AbstractTableModel {

    private final String[] kolom = {
        "Nama Produk",
        "Harga",
        "Jumlah",
        "Total Pemasukan",
        "Total Pengeluaran",
        "Total Laba"
    };

    private List<Transaksi> daftarTransaksi = new ArrayList<>();
    private List<ProdukBahan> daftarProdukBahan = new ArrayList<>();

    public void setData(
            List<Transaksi> transaksi,
            List<ProdukBahan> produkBahan) {

        daftarTransaksi = new ArrayList<>(transaksi);
        daftarProdukBahan = new ArrayList<>(produkBahan);

        fireTableDataChanged();
    }

    private BigDecimal hitungModal(Transaksi transaksi) {
        BigDecimal modal = BigDecimal.ZERO;

        for (ProdukBahan pb : daftarProdukBahan) {
            if (pb.getProduk().getId()
                    == transaksi.getProduk().getId()) {

                modal = modal.add(pb.getBahan().getHarga());
            }
        }

        return modal;
    }

    private BigDecimal hitungPemasukan(Transaksi transaksi) {
        return transaksi.getProduk().getHarga()
                .multiply(BigDecimal.valueOf(transaksi.getJumlah()));
    }

    private BigDecimal hitungPengeluaran(Transaksi transaksi) {
        return hitungModal(transaksi)
                .multiply(BigDecimal.valueOf(transaksi.getJumlah()));
    }

    private BigDecimal hitungLaba(Transaksi transaksi) {
        return hitungPemasukan(transaksi)
                .subtract(hitungPengeluaran(transaksi));
    }

    @Override
    public int getRowCount() {
        return daftarTransaksi.size();
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
        Transaksi transaksi = daftarTransaksi.get(row);

        switch (column) {
            case 0:
                return transaksi.getProduk().getNama();
            case 1:
                return RupiahUtil.format(
                        transaksi.getProduk().getHarga());
            case 2:
                return transaksi.getJumlah();
            case 3:
                return RupiahUtil.format(hitungPemasukan(transaksi));
            case 4:
                return RupiahUtil.format(hitungPengeluaran(transaksi));
            case 5:
                return RupiahUtil.format(hitungLaba(transaksi));
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public BigDecimal getTotalPemasukan() {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaksi transaksi : daftarTransaksi) {
            total = total.add(hitungPemasukan(transaksi));
        }

        return total;
    }

    public BigDecimal getTotalPengeluaran() {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaksi transaksi : daftarTransaksi) {
            total = total.add(hitungPengeluaran(transaksi));
        }

        return total;
    }

    public BigDecimal getTotalLaba() {
        return getTotalPemasukan().subtract(getTotalPengeluaran());
    }
}
