
package sistemmanajemenkeuanganumkmserba10000.model;

import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BahanTableModel extends AbstractTableModel {

    private final String[] kolom = {
        "Nama Bahan/Barang",
        "Harga"
    };

    private List<Bahan> daftarBahan = new ArrayList<>();

    public void setData(List<Bahan> data) {
        daftarBahan = new ArrayList<>(data);
        fireTableDataChanged();
    }

    public void tambahData(Bahan bahan) {
        int row = daftarBahan.size();
        daftarBahan.add(bahan);
        fireTableRowsInserted(row, row);
    }

    public Bahan getBahanAt(int row) {
        return daftarBahan.get(row);
    }

    @Override
    public int getRowCount() {
        return daftarBahan.size();
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
        Bahan bahan = daftarBahan.get(row);

        switch (column) {
            case 0:
                return bahan.getNama();
            case 1:
                return RupiahUtil.format(bahan.getHarga());
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
