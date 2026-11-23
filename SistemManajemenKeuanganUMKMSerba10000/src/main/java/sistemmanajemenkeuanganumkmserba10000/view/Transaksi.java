package sistemmanajemenkeuanganumkmserba10000.view;

import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.ProdukBahan;
import sistemmanajemenkeuanganumkmserba10000.model.RupiahUtil;
import sistemmanajemenkeuanganumkmserba10000.repository.ProdukBahanRepository;
import sistemmanajemenkeuanganumkmserba10000.repository.ProdukRepository;
import sistemmanajemenkeuanganumkmserba10000.repository.TransaksiRepository;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ALIYYAH PUTRI
 */
public class Transaksi extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Transaksi.class.getName());
    private final ProdukRepository produkRepository = new ProdukRepository();
    private final ProdukBahanRepository resepRepository = new ProdukBahanRepository();
    private final TransaksiRepository transaksiRepository = new TransaksiRepository();

    /**
     * Creates new form Transaksi
     */
    public Transaksi() {
        initComponents();
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        txtTanggal.setText(LocalDate.now().toString());
        txtHargaSatuan.setEditable(false);
        txtTotal.setEditable(false);
        cbProduk.setModel(new DefaultComboBoxModel<>());
        txtJumlah.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) {
                perbaruiTotal();
            }
        });
        Navigasi.pasang(this);
        muatProduk();
    }

    private void muatProduk() {
        try {
            List<Produk> produk = produkRepository.findAll();
            DefaultComboBoxModel<Produk> model = new DefaultComboBoxModel<>();
            for (Produk item : produk) model.addElement(item);
            cbProduk.setModel(model);
            cbProdukActionPerformed(null);
            btnSimpan.setEnabled(!produk.isEmpty());
        } catch (java.sql.SQLException ex) {
            UiUtil.showError(this, ex);
            btnSimpan.setEnabled(false);
        }
    }

    private void perbaruiTotal() {
        Produk produk = (Produk) cbProduk.getSelectedItem();
        if (produk == null) {
            txtHargaSatuan.setText("");
            txtTotal.setText("");
            return;
        }
        txtHargaSatuan.setText(RupiahUtil.format(produk.getHarga()));
        try {
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());
            txtTotal.setText(jumlah > 0
                    ? RupiahUtil.format(produk.getHarga().multiply(BigDecimal.valueOf(jumlah)))
                    : "");
        } catch (NumberFormatException ex) {
            txtTotal.setText("");
        }
    }

    private void simpanTransaksi() {
        Produk produk = (Produk) cbProduk.getSelectedItem();
        if (produk == null) {
            JOptionPane.showMessageDialog(this, "Tambahkan produk dan resep terlebih dahulu.");
            return;
        }
        try {
            LocalDate tanggal = LocalDate.parse(txtTanggal.getText().trim());
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());
            if (jumlah <= 0) throw new NumberFormatException();
            List<ProdukBahan> resep = resepRepository.findByProdukId(produk.getId());
            if (resep.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Produk belum memiliki bahan/resep.",
                        "Resep belum lengkap", JOptionPane.WARNING_MESSAGE);
                return;
            }
            BigDecimal modalPerProduk = resep.stream()
                    .map(item -> item.getBahan().getHarga().multiply(item.getJumlahPemakaian()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            sistemmanajemenkeuanganumkmserba10000.entity.Transaksi transaksi =
                    new sistemmanajemenkeuanganumkmserba10000.entity.Transaksi(0, tanggal, produk,
                            jumlah, txtKeterangan.getText().trim(), produk.getHarga(), modalPerProduk);
            transaksiRepository.save(transaksi);
            JOptionPane.showMessageDialog(this,
                    "Transaksi tersimpan. Penjualan produk yang sama pada tanggal tersebut dijumlahkan.",
                    "Transaksi tersimpan", JOptionPane.INFORMATION_MESSAGE);
            txtJumlah.setText("");
            txtKeterangan.setText("");
            perbaruiTotal();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Tanggal harus memakai format YYYY-MM-DD.",
                    "Tanggal tidak valid", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah produk harus berupa bilangan bulat lebih dari nol.",
                    "Jumlah tidak valid", JOptionPane.WARNING_MESSAGE);
        } catch (java.sql.SQLException ex) {
            UiUtil.showError(this, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTanggal = new javax.swing.JTextField();
        cbProduk = new javax.swing.JComboBox<>();
        txtJumlah = new javax.swing.JTextField();
        txtHargaSatuan = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        btnSimpan = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(253, 253, 235));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setToolTipText("");
        jPanel1.setLayout(null);

        jLabel1.setBackground(new java.awt.Color(51, 153, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(7, 61, 0, 0);

        jPanel2.setBackground(new java.awt.Color(162, 203, 139));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(253, 253, 235));
        jLabel2.setText("Dashboard");

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(253, 253, 235));
        jLabel3.setText("Transaksi");

        jLabel4.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(253, 253, 235));
        jLabel4.setText("Produk");

        jLabel5.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(253, 253, 235));
        jLabel5.setText("Bahan");

        jLabel6.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(253, 253, 235));
        jLabel6.setText("Laporan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel2)
                .addGap(28, 28, 28)
                .addComponent(jLabel3)
                .addGap(33, 33, 33)
                .addComponent(jLabel4)
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addGap(31, 31, 31)
                .addComponent(jLabel6)
                .addContainerGap(258, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 60, 110, 490);

        jLabel7.setFont(new java.awt.Font("Goudy Old Style", 1, 28)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(91, 126, 60));
        jLabel7.setText("Transaksi");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(210, 70, 120, 40);

        jLabel8.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel8.setText("Tanggal");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(150, 120, 60, 15);

        jLabel9.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel9.setText("Produk");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(150, 160, 50, 15);

        jLabel10.setFont(new java.awt.Font("Lucida Bright", 1, 11)); // NOI18N
        jLabel10.setText("Jumlah Produk");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(150, 200, 100, 14);

        jLabel11.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel11.setText("Harga Satuan");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(150, 240, 90, 15);

        jLabel12.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel12.setText("Total");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(150, 280, 60, 15);

        jLabel13.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel13.setText("Keterangan");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(150, 320, 80, 15);

        txtTanggal.addActionListener(this::txtTanggalActionPerformed);
        jPanel1.add(txtTanggal);
        txtTanggal.setBounds(250, 120, 105, 22);

        cbProduk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbProduk.addActionListener(this::cbProdukActionPerformed);
        jPanel1.add(cbProduk);
        cbProduk.setBounds(250, 160, 105, 22);
        jPanel1.add(txtJumlah);
        txtJumlah.setBounds(250, 200, 105, 22);

        txtHargaSatuan.setEditable(false);
        txtHargaSatuan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHargaSatuan.setText("10.000");
        txtHargaSatuan.addActionListener(this::txtHargaSatuanActionPerformed);
        jPanel1.add(txtHargaSatuan);
        txtHargaSatuan.setBounds(250, 240, 105, 22);
        jPanel1.add(txtTotal);
        txtTotal.setBounds(250, 280, 105, 22);

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane2.setViewportView(txtKeterangan);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(250, 320, 156, 76);

        btnSimpan.setBackground(new java.awt.Color(91, 126, 60));
        btnSimpan.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(253, 253, 235));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);
        jPanel1.add(btnSimpan);
        btnSimpan.setBounds(148, 420, 80, 22);

        jPanel3.setBackground(new java.awt.Color(91, 126, 60));

        jLabel14.setFont(new java.awt.Font("Goudy Old Style", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(253, 253, 235));
        jLabel14.setText("Sistem Manajemen Keuangan UMKM Serba 10.000");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(42, 42, 42))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(-8, 1, 460, 60);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTanggalActionPerformed
    }//GEN-LAST:event_txtTanggalActionPerformed

    private void cbProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProdukActionPerformed
        perbaruiTotal();
    }//GEN-LAST:event_cbProdukActionPerformed

    private void txtHargaSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaSatuanActionPerformed
    }//GEN-LAST:event_txtHargaSatuanActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpanTransaksi();
    }//GEN-LAST:event_btnSimpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Transaksi().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<Produk> cbProduk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtHargaSatuan;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtTanggal;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
