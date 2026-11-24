/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemmanajemenkeuanganumkmserba10000.view;

import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;
import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.ProdukBahan;
import sistemmanajemenkeuanganumkmserba10000.model.ProdukTableModel;
import sistemmanajemenkeuanganumkmserba10000.repository.BahanRepository;
import sistemmanajemenkeuanganumkmserba10000.repository.ProdukBahanRepository;
import sistemmanajemenkeuanganumkmserba10000.repository.ProdukRepository;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ASUS ROG
 */
public class ProdukFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProdukFrame.class.getName());
    private final ProdukRepository produkRepository = new ProdukRepository();
    private final BahanRepository bahanRepository = new BahanRepository();
    private final ProdukBahanRepository resepRepository = new ProdukBahanRepository();
    private final ProdukTableModel tableModel = new ProdukTableModel();

    /**
     * Creates new form ProdukFrame
     */
    public ProdukFrame() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        tblProduk.setModel(tableModel);
        pasangMenuTabel();
        Navigasi.pasang(this);
        muatData();
    }

    private void muatData() {
        try {
            tableModel.setData(produkRepository.findAll(), resepRepository.findAll());
        } catch (SQLException ex) {
            UiUtil.showError(this, ex);
        }
    }

    private void pasangMenuTabel() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem ubah = new JMenuItem("Ubah produk/resep");
        JMenuItem hapus = new JMenuItem("Hapus produk");
        ubah.addActionListener(event -> ubahTerpilih());
        hapus.addActionListener(event -> hapusTerpilih());
        menu.add(ubah);
        menu.add(hapus);
        tblProduk.setComponentPopupMenu(menu);
        tblProduk.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (javax.swing.SwingUtilities.isRightMouseButton(event)) {
                    int row = tblProduk.rowAtPoint(event.getPoint());
                    if (row >= 0) tblProduk.setRowSelectionInterval(row, row);
                }
                if (event.getClickCount() == 2 && javax.swing.SwingUtilities.isLeftMouseButton(event)) {
                    int row = tblProduk.rowAtPoint(event.getPoint());
                    if (row >= 0) {
                        tblProduk.setRowSelectionInterval(row, row);
                        ubahTerpilih();
                    }
                }
            }
        });
    }

    private Produk produkTerpilih() {
        int row = tblProduk.getSelectedRow();
        return row < 0 ? null : tableModel.getProdukAt(tblProduk.convertRowIndexToModel(row));
    }

    private void tambahProduk() {
        try {
            List<Bahan> bahan = bahanRepository.findAll();
            if (bahan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tambahkan bahan/barang terlebih dahulu.",
                        "Bahan belum tersedia", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            PopUpTambahProduk dialog = new PopUpTambahProduk(this, bahan, null, List.of());
            dialog.setVisible(true);
            if (dialog.getProdukHasil() == null) return;
            produkRepository.save(dialog.getProdukHasil());
            try {
                resepRepository.replaceForProduk(dialog.getProdukHasil().getId(), dialog.getKomposisiHasil());
            } catch (SQLException ex) {
                produkRepository.delete(dialog.getProdukHasil().getId());
                throw ex;
            }
            muatData();
        } catch (SQLException ex) {
            UiUtil.showError(this, ex);
        }
    }

    private void ubahTerpilih() {
        Produk produk = produkTerpilih();
        if (produk == null) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang ingin diubah.");
            return;
        }
        try {
            PopUpTambahProduk dialog = new PopUpTambahProduk(this, bahanRepository.findAll(),
                    produk, resepRepository.findByProdukId(produk.getId()));
            dialog.setVisible(true);
            if (dialog.getProdukHasil() == null) return;
            produkRepository.update(dialog.getProdukHasil());
            try {
                resepRepository.replaceForProduk(produk.getId(), dialog.getKomposisiHasil());
            } catch (SQLException ex) {
                produkRepository.update(produk);
                throw ex;
            }
            muatData();
        } catch (SQLException ex) {
            UiUtil.showError(this, ex);
        }
    }

    private void hapusTerpilih() {
        Produk produk = produkTerpilih();
        if (produk == null) {
            JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Hapus produk " + produk.getNama() + "? Produk yang pernah dijual tidak dapat dihapus.",
                "Konfirmasi hapus", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) return;
        try {
            produkRepository.delete(produk.getId());
            muatData();
        } catch (SQLException ex) {
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
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduk = new javax.swing.JTable();
        btnTambahProduk = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(253, 253, 235));

        jPanel2.setBackground(new java.awt.Color(162, 203, 139));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(253, 253, 235));
        jLabel8.setText("Dashboard");

        jLabel7.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(253, 253, 235));
        jLabel7.setText("Transaksi");

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
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel8)
                .addGap(28, 28, 28)
                .addComponent(jLabel7)
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addGap(28, 28, 28)
                .addComponent(jLabel5)
                .addGap(26, 26, 26)
                .addComponent(jLabel6)
                .addContainerGap(119, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Goudy Old Style", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(91, 126, 60));
        jLabel2.setText("Daftar Produk");

        tblProduk.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        tblProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Nama Produk", "Harga", "Bahan & Barang", "Modal", "Laba"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProduk.setShowGrid(true);
        jScrollPane1.setViewportView(tblProduk);

        btnTambahProduk.setBackground(new java.awt.Color(91, 126, 60));
        btnTambahProduk.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        btnTambahProduk.setForeground(new java.awt.Color(253, 253, 235));
        btnTambahProduk.setText("Tambah Produk");
        btnTambahProduk.addActionListener(this::btnTambahProdukActionPerformed);

        jPanel3.setBackground(new java.awt.Color(91, 126, 60));

        jLabel14.setFont(new java.awt.Font("Goudy Old Style", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(253, 253, 235));
        jLabel14.setText("Sistem Manajemen Keuangan UMKM Serba 10.000");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(143, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(92, 92, 92))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnTambahProduk)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(jLabel2)))
                .addContainerGap(51, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTambahProduk)
                .addGap(123, 123, 123))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 349, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahProdukActionPerformed
        tambahProduk();
    }//GEN-LAST:event_btnTambahProdukActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ProdukFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTambahProduk;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProduk;
    // End of variables declaration//GEN-END:variables
}
