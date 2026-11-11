
package sistemmanajemenkeuanganumkmserba10000.model;

import sistemmanajemenkeuanganumkmserba10000.entity.Transaksi;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransaksiTableModel extends AbstractTableModel {

    private final String[] kolom = {
        "Tanggal",
        "Nama Produk",
        "Jumlah",
        "Harga Satuan",
        "Total",
        "Keterangan"
    };

    private List<Transaksi> daftarTransaksi = new ArrayList<>();

    public void setData(List<Transaksi> data) {
        daftarTransaksi = new ArrayList<>(data);
        fireTableDataChanged();
    }

    public void tambahData(Transaksi transaksi) {
        int row = daftarTransaksi.size();
        daftarTransaksi.add(transaksi);
        fireTableRowsInserted(row, row);
    }

    public Transaksi getTransaksiAt(int row) {
        return daftarTransaksi.get(row);
    }

    private BigDecimal hitungTotal(Transaksi transaksi) {
        return transaksi.getProduk().getHarga()
                .multiply(BigDecimal.valueOf(transaksi.getJumlah()));
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
                return transaksi.getJumlah();
            case 3:
                return RupiahUtil.format(
                        transaksi.getProduk().getHarga());
            case 4:
                return RupiahUtil.format(hitungTotal(transaksi));
            case 5:
                return transaksi.getKeterangan();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
