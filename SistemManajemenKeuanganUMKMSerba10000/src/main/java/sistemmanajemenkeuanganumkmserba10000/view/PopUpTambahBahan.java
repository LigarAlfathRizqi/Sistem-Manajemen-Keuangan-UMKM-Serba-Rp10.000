package sistemmanajemenkeuanganumkmserba10000.view;

import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.math.BigDecimal;

public class PopUpTambahBahan extends javax.swing.JDialog {

    private final Bahan bahanAwal;
    private Bahan hasil;

    public PopUpTambahBahan() {
        this(null, null);
    }

    public PopUpTambahBahan(Window owner, Bahan bahan) {
        super(owner, bahan == null ? "Tambah Bahan/Barang" : "Ubah Bahan/Barang",
                ModalityType.APPLICATION_MODAL);
        this.bahanAwal = bahan;
        initComponents();
        if (bahan != null) {
            lblJudul.setText("Ubah Bahan / Barang");
            btnSimpan.setText("Simpan perubahan");
            txtNama.setText(bahan.getNama());
            txtSatuan.setText(bahan.getSatuan());
            txtHarga.setText(bahan.getHarga().stripTrailingZeros().toPlainString());
        }
        getRootPane().setDefaultButton(btnSimpan);
        pack();
        setLocationRelativeTo(owner);
    }

    public Bahan getHasil() {
        return hasil;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        pnlIsi = new javax.swing.JPanel();
        lblKeterangan = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblSatuan = new javax.swing.JLabel();
        txtSatuan = new javax.swing.JTextField();
        lblHarga = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        btnBatal = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        pnlHeader.setBackground(new java.awt.Color(91, 126, 60));

        lblJudul.setFont(new java.awt.Font("Goudy Old Style", 1, 22)); // NOI18N
        lblJudul.setForeground(new java.awt.Color(253, 253, 235));
        lblJudul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul.setText("Tambah Bahan / Barang");

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblJudul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblJudul, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
        );

        pnlIsi.setBackground(new java.awt.Color(253, 253, 235));

        lblKeterangan.setFont(new java.awt.Font("Lucida Bright", 2, 12)); // NOI18N
        lblKeterangan.setForeground(new java.awt.Color(120, 150, 95));

        lblNama.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        lblNama.setForeground(new java.awt.Color(91, 126, 60));
        lblNama.setText("Nama bahan/barang");

        txtNama.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        txtNama.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(162, 203, 139)), javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        lblSatuan.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        lblSatuan.setForeground(new java.awt.Color(91, 126, 60));
        lblSatuan.setText("Satuan pembelian");

        txtSatuan.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        txtSatuan.setText("unit");
        txtSatuan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(162, 203, 139)), javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        lblHarga.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        lblHarga.setForeground(new java.awt.Color(91, 126, 60));
        lblHarga.setText("Harga per satuan (Rp)");

        txtHarga.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        txtHarga.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(162, 203, 139)), javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        btnBatal.setBackground(new java.awt.Color(162, 203, 139));
        btnBatal.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(253, 253, 235));
        btnBatal.setText("Batal");
        btnBatal.addActionListener(this::btnBatalActionPerformed);

        btnSimpan.setBackground(new java.awt.Color(91, 126, 60));
        btnSimpan.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(253, 253, 235));
        btnSimpan.setText("Tambah");
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);

        javax.swing.GroupLayout pnlIsiLayout = new javax.swing.GroupLayout(pnlIsi);
        pnlIsi.setLayout(pnlIsiLayout);
        pnlIsiLayout.setHorizontalGroup(
            pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIsiLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKeterangan)
                    .addGroup(pnlIsiLayout.createSequentialGroup()
                        .addGroup(pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNama)
                            .addComponent(lblSatuan)
                            .addComponent(lblHarga))
                        .addGap(18, 18, 18)
                        .addGroup(pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(txtSatuan, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIsiLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );
        pnlIsiLayout.setVerticalGroup(
            pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIsiLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblKeterangan)
                .addGap(18, 18, 18)
                .addGroup(pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNama)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSatuan)
                    .addComponent(txtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHarga)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlIsi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlIsi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        dispose();
    }//GEN-LAST:event_btnBatalActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblSatuan;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlIsi;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtSatuan;
    // End of variables declaration//GEN-END:variables
}
