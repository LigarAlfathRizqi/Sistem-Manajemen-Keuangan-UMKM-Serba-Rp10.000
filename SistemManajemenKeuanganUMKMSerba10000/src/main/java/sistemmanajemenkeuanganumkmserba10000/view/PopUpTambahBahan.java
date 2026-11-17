package sistemmanajemenkeuanganumkmserba10000.view;

import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.math.BigDecimal;

public class PopUpTambahBahan extends JDialog {

    private final JTextField txtNama = new JTextField(22);
    private final JTextField txtSatuan = new JTextField("unit", 12);
    private final JTextField txtHarga = new JTextField(14);
    private final Bahan bahanAwal;
    private Bahan hasil;

    public PopUpTambahBahan() {
        this(null, null);
    }

    public PopUpTambahBahan(Window owner, Bahan bahan) {
        super(owner, bahan == null ? "Tambah Bahan/Barang" : "Ubah Bahan/Barang",
                ModalityType.APPLICATION_MODAL);
        this.bahanAwal = bahan;
        buatTampilan();
        if (bahan != null) {
            txtNama.setText(bahan.getNama());
            txtSatuan.setText(bahan.getSatuan());
            txtHarga.setText(bahan.getHarga().stripTrailingZeros().toPlainString());
        }
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    public Bahan getHasil() {
        return hasil;
    }

    private void buatTampilan() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 8, 7, 8);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        fields.add(new JLabel("Nama bahan/barang"), c);
        c.gridx = 1;
        fields.add(txtNama, c);
        c.gridx = 0;
        c.gridy++;
        fields.add(new JLabel("Satuan pembelian"), c);
        c.gridx = 1;
        fields.add(txtSatuan, c);
        c.gridx = 0;
        c.gridy++;
        fields.add(new JLabel("Harga per satuan (Rp)"), c);
        c.gridx = 1;
        fields.add(txtHarga, c);

        JButton btnSimpan = new JButton(bahanAwal == null ? "Tambah" : "Simpan perubahan");
        JButton btnBatal = new JButton("Batal");
        btnSimpan.addActionListener(event -> simpan());
        btnBatal.addActionListener(event -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnBatal);
        buttons.add(btnSimpan);
        add(new JLabel(bahanAwal == null ? "Tambah bahan/barang" : "Ubah bahan/barang",
                JLabel.CENTER), BorderLayout.NORTH);
        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnSimpan);
    }

    private void simpan() {
        String nama = txtNama.getText().trim();
        String satuan = txtSatuan.getText().trim();
        if (nama.isEmpty() || satuan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan satuan wajib diisi.",
                    "Periksa isian", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            BigDecimal harga = UiUtil.parseRupiah(txtHarga.getText());
            if (harga.signum() < 0) {
                throw new NumberFormatException("Harga tidak boleh negatif.");
            }
            hasil = new Bahan(bahanAwal == null ? 0 : bahanAwal.getId(), nama, satuan, harga);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka nol atau lebih.",
                    "Harga tidak valid", JOptionPane.WARNING_MESSAGE);
            txtHarga.requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new PopUpTambahBahan().setVisible(true));
    }
}
