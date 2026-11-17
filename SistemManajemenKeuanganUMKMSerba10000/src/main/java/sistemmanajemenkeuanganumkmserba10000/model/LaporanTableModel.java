
package sistemmanajemenkeuanganumkmserba10000.model;

import sistemmanajemenkeuanganumkmserba10000.entity.Transaksi;
import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LaporanTableModel extends AbstractTableModel {

    private final String[] kolom = {
        "Tanggal",
        "Nama Produk",
        "Harga",
        "Jumlah",
        "Total Pemasukan",
        "Total Pengeluaran",
        "Total Laba"
    };

    private List<Transaksi> daftarTransaksi = new ArrayList<>();

    public void setData(List<Transaksi> transaksi) {
        daftarTransaksi = new ArrayList<>(transaksi);
        fireTableDataChanged();
    }

    private BigDecimal hitungPemasukan(Transaksi transaksi) {
        return transaksi.getHargaJualSatuan()
                .multiply(BigDecimal.valueOf(transaksi.getJumlah()));
    }

    private BigDecimal hitungPengeluaran(Transaksi transaksi) {
        return transaksi.getModalSatuan()
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
                return transaksi.getTanggal();
            case 1:
                return transaksi.getProduk().getNama();
            case 2:
                return RupiahUtil.format(transaksi.getHargaJualSatuan());
            case 3:
                return transaksi.getJumlah();
            case 4:
                return RupiahUtil.format(hitungPemasukan(transaksi));
            case 5:
                return RupiahUtil.format(hitungPengeluaran(transaksi));
            case 6:
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

    public Transaksi getTransaksiAt(int row) {
        return daftarTransaksi.get(row);
    }
}
