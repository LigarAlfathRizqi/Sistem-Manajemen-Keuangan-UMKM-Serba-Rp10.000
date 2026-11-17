package sistemmanajemenkeuanganumkmserba10000.view;

import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;
import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.ProdukBahan;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopUpTambahProduk extends JDialog {

    private final JTextField txtNama = new JTextField(24);
    private final JTextField txtHarga = new JTextField("10000", 12);
    private final List<Bahan> daftarBahan;
    private final Produk produkAwal;
    private final Map<Integer, BigDecimal> jumlahAwal = new HashMap<>();
    private final DefaultTableModel modelBahan;
    private Produk produkHasil;
    private List<ProdukBahan> komposisiHasil = new ArrayList<>();

    public PopUpTambahProduk() {
        this(null, List.of(), null, List.of());
    }

    public PopUpTambahProduk(Window owner, List<Bahan> bahan,
            Produk produk, List<ProdukBahan> komposisi) {
        super(owner, produk == null ? "Tambah Produk" : "Ubah Produk", ModalityType.APPLICATION_MODAL);
        this.daftarBahan = new ArrayList<>(bahan);
        this.produkAwal = produk;
        for (ProdukBahan relation : komposisi) {
            jumlahAwal.put(relation.getBahan().getId(), relation.getJumlahPemakaian());
        }
        modelBahan = new DefaultTableModel(new Object[]{"Pakai", "Bahan", "Satuan", "Harga/unit", "Jumlah"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : (column == 4 ? BigDecimal.class : String.class);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 4;
            }
        };
        buatTampilan();
        muatBahan();
        if (produk != null) {
            txtNama.setText(produk.getNama());
            txtHarga.setText(produk.getHarga().stripTrailingZeros().toPlainString());
        }
        pack();
        setMinimumSize(new java.awt.Dimension(700, 430));
        setLocationRelativeTo(owner);
    }

    public Produk getProdukHasil() {
        return produkHasil;
    }

    public List<ProdukBahan> getKomposisiHasil() {
        return komposisiHasil;
    }

    private void buatTampilan() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 7, 5, 7);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        fields.add(new JLabel("Nama produk"), c);
        c.gridx = 1;
        fields.add(txtNama, c);
        c.gridx = 0;
        c.gridy++;
        fields.add(new JLabel("Harga jual (Rp)"), c);
        c.gridx = 1;
        txtHarga.setEditable(false);
        txtHarga.setToolTipText("Harga produk mengikuti konsep Serba Rp10.000.");
        fields.add(txtHarga, c);

        JTable table = new JTable(modelBahan);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(false);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(new JTextField()));
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Pilih bahan dan jumlah yang dipakai untuk satu produk", JLabel.CENTER), BorderLayout.NORTH);
        top.add(fields, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnSimpan = new JButton(produkAwal == null ? "Tambah" : "Simpan perubahan");
        JButton btnBatal = new JButton("Batal");
        btnSimpan.addActionListener(event -> simpan(table));
        btnBatal.addActionListener(event -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnBatal);
        buttons.add(btnSimpan);
        add(buttons, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnSimpan);
    }

    private void muatBahan() {
        for (Bahan bahan : daftarBahan) {
            BigDecimal quantity = jumlahAwal.getOrDefault(bahan.getId(), BigDecimal.ONE);
            modelBahan.addRow(new Object[]{jumlahAwal.containsKey(bahan.getId()), bahan.getNama(),
                bahan.getSatuan(), "Rp " + bahan.getHarga().toPlainString(), quantity});
        }
    }

    private void simpan(JTable table) {
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama produk wajib diisi.",
                    "Periksa isian", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<ProdukBahan> chosen = new ArrayList<>();
        for (int row = 0; row < modelBahan.getRowCount(); row++) {
            if (!Boolean.TRUE.equals(modelBahan.getValueAt(row, 0))) {
                continue;
            }
            try {
                BigDecimal quantity = new BigDecimal(
                        String.valueOf(modelBahan.getValueAt(row, 4)).trim().replace(',', '.'));
                if (quantity.signum() <= 0) {
                    throw new NumberFormatException();
                }
                chosen.add(new ProdukBahan(0, produkAwal, daftarBahan.get(row), quantity));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah bahan harus berupa angka lebih dari nol.",
                        "Jumlah tidak valid", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        if (chosen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih sedikitnya satu bahan untuk resep produk.",
                    "Resep belum lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }
        produkHasil = new Produk(produkAwal == null ? 0 : produkAwal.getId(), nama, new BigDecimal("10000"));
        komposisiHasil = chosen;
        dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new PopUpTambahProduk().setVisible(true));
    }
}
