package sistemmanajemenkeuanganumkmserba10000.view;

import sistemmanajemenkeuanganumkmserba10000.entity.Transaksi;
import sistemmanajemenkeuanganumkmserba10000.model.LaporanTableModel;
import sistemmanajemenkeuanganumkmserba10000.model.RupiahUtil;
import sistemmanajemenkeuanganumkmserba10000.repository.TransaksiRepository;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTable;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ALIYYAH PUTRI
 */
public class Laporan extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Laporan.class.getName());
    private final TransaksiRepository transaksiRepository = new TransaksiRepository();
    private final LaporanTableModel tableModel = new LaporanTableModel();
    private boolean sedangMemuatPeriode;

    /**
     * Creates new form Laporan
     */
    public Laporan() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 650));
        setSize(1150, 720);
        setLocationRelativeTo(null);
        cbFilterMinggu.setPreferredSize(new Dimension(280, 28));
        tblLaporan.setModel(tableModel);
        tblLaporan.setAutoCreateRowSorter(true);
        tblLaporan.setFillsViewportHeight(true);
        tblLaporan.setRowHeight(24);
        int[] columnWidths = {105, 190, 105, 70, 145, 145, 130};
        for (int i = 0; i < columnWidths.length; i++) {
            tblLaporan.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        pasangMenuTabel();
        Navigasi.pasang(this);
        muatPilihanPeriode();
        tampilkanLaporan();
    }

    private void muatPilihanPeriode() {
        for (ActionListener listener : cbFilterMinggu.getActionListeners()) {
            cbFilterMinggu.removeActionListener(listener);
        }
        cbFilterMinggu.removeAllItems();
        LocalDate hariIni = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", java.util.Locale.forLanguageTag("id-ID"));
        for (int i = 0; i < 30; i++) {
            LocalDate date = hariIni.minusDays(i);
            cbFilterMinggu.addItem(new PeriodeLaporan("Harian: " + date.format(dateFormat), date, date));
        }
        LocalDate awalMinggu = hariIni.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (int i = 0; i < 12; i++) {
            LocalDate start = awalMinggu.minusWeeks(i);
            LocalDate end = start.plusDays(6);
            cbFilterMinggu.addItem(new PeriodeLaporan("Mingguan: " + start.format(dateFormat)
                    + " - " + end.format(dateFormat), start, end));
        }
        cbFilterMinggu.setSelectedIndex(30);
        cbFilterMinggu.addActionListener(this::cbFilterMingguActionPerformed);
    }

    private void tampilkanLaporan() {
        if (sedangMemuatPeriode) return;
        PeriodeLaporan periode = (PeriodeLaporan) cbFilterMinggu.getSelectedItem();
        if (periode == null) return;
        sedangMemuatPeriode = true;
        try {
            List<Transaksi> transaksi = transaksiRepository.findByRange(periode.mulai, periode.sampai);
            tableModel.setData(transaksi);
            lblTotalPemasukan.setText("<html><center>Pemasukan<br>" + RupiahUtil.format(tableModel.getTotalPemasukan()) + "</center></html>");
            lblTotalPengeluaran.setText("<html><center>Pengeluaran<br>" + RupiahUtil.format(tableModel.getTotalPengeluaran()) + "</center></html>");
            lblTotalLaba.setText("<html><center>Laba<br>" + RupiahUtil.format(tableModel.getTotalLaba()) + "</center></html>");
            jLabel2.setText("Laporan Detail — " + periode.nama);
        } catch (SQLException ex) {
            UiUtil.showError(this, ex);
            tableModel.setData(List.of());
            lblTotalPemasukan.setText("<html><center>Pemasukan<br>Rp 0</center></html>");
            lblTotalPengeluaran.setText("<html><center>Pengeluaran<br>Rp 0</center></html>");
            lblTotalLaba.setText("<html><center>Laba<br>Rp 0</center></html>");
        } finally {
            sedangMemuatPeriode = false;
        }
    }

    private void pasangMenuTabel() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem ubah = new JMenuItem("Ubah jumlah/keterangan");
        JMenuItem hapus = new JMenuItem("Hapus transaksi");
        ubah.addActionListener(event -> ubahTerpilih());
        hapus.addActionListener(event -> hapusTerpilih());
        menu.add(ubah);
        menu.add(hapus);
        tblLaporan.setComponentPopupMenu(menu);
        tblLaporan.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (javax.swing.SwingUtilities.isRightMouseButton(event)) {
                    int row = tblLaporan.rowAtPoint(event.getPoint());
                    if (row >= 0) tblLaporan.setRowSelectionInterval(row, row);
                }
            }
        });
    }

    private Transaksi transaksiTerpilih() {
        int viewRow = tblLaporan.getSelectedRow();
        return viewRow < 0 ? null : tableModel.getTransaksiAt(tblLaporan.convertRowIndexToModel(viewRow));
    }

    private void ubahTerpilih() {
        Transaksi transaksi = transaksiTerpilih();
        if (transaksi == null) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin diubah.");
            return;
        }
        JSpinner jumlah = new JSpinner(new SpinnerNumberModel(transaksi.getJumlah(), 1, 1_000_000, 1));
        JTextField keterangan = new JTextField(transaksi.getKeterangan(), 24);
        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 5, 5));
        form.add(new javax.swing.JLabel("Jumlah produk"));
        form.add(jumlah);
        form.add(new javax.swing.JLabel("Keterangan"));
        form.add(keterangan);
        if (JOptionPane.showConfirmDialog(this, form, "Ubah transaksi",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;
        transaksi.setJumlah((Integer) jumlah.getValue());
        transaksi.setKeterangan(keterangan.getText().trim());
        try {
            transaksiRepository.update(transaksi);
            tampilkanLaporan();
        } catch (SQLException ex) {
            UiUtil.showError(this, ex);
        }
    }

    private void hapusTerpilih() {
        Transaksi transaksi = transaksiTerpilih();
        if (transaksi == null) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Hapus transaksi " + transaksi.getProduk().getNama() + " tanggal " + transaksi.getTanggal() + "?",
                "Konfirmasi hapus", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) return;
        try {
            transaksiRepository.delete(transaksi.getId());
            tampilkanLaporan();
        } catch (SQLException ex) {
            UiUtil.showError(this, ex);
        }
    }

    private static final class PeriodeLaporan {
        private final String nama;
        private final LocalDate mulai;
        private final LocalDate sampai;

        private PeriodeLaporan(String nama, LocalDate mulai, LocalDate sampai) {
            this.nama = nama;
            this.mulai = mulai;
            this.sampai = sampai;
        }

        @Override
        public String toString() {
            return nama;
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
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbFilterMinggu = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        lblTotalPemasukan = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblTotalLaba = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lblTotalPengeluaran = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLaporan = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sistem Manajemen Keuangan UMKM Serba 10.000");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(104, 430));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 255));
        jLabel8.setText("Dashboard");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 255));
        jLabel7.setText("Transaksi");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 255));
        jLabel9.setText("Produk");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 255));
        jLabel10.setText("Bahan");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 255));
        jLabel11.setText("Laporan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel8)
                .addGap(32, 32, 32)
                .addComponent(jLabel7)
                .addGap(30, 30, 30)
                .addComponent(jLabel9)
                .addGap(32, 32, 32)
                .addComponent(jLabel10)
                .addGap(30, 30, 30)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 255));
        jLabel2.setText("Laporan Detail");

        cbFilterMinggu.setModel(new javax.swing.DefaultComboBoxModel<PeriodeLaporan>());
        cbFilterMinggu.addActionListener(this::cbFilterMingguActionPerformed);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalPemasukan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalPemasukan.setForeground(new java.awt.Color(102, 0, 204));
        lblTotalPemasukan.setText("Pemasukan");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTotalPemasukan)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalPemasukan)
                .addGap(19, 19, 19))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalLaba.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalLaba.setForeground(new java.awt.Color(0, 153, 51));
        lblTotalLaba.setText("Laba");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(lblTotalLaba)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(lblTotalLaba)
                .addGap(19, 19, 19))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalPengeluaran.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalPengeluaran.setText("Pengeluaran");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTotalPengeluaran)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblTotalPengeluaran)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Nasi Ayam Katsu", "10.000", "10", null, null, null},
                {"Ayam Geprek", "10.000", "2", null, null, null},
                {"Nasi Uduk", "10.000", null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nama Produk", "Harga", "Jumlah", "Total Pemasukkan", "Total Pengeluaran", "Total Laba"
            }
        ));
        tblLaporan.setShowGrid(true);
        jScrollPane1.setViewportView(tblLaporan);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbFilterMinggu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 25, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(58, 58, 58))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(12, 12, 12)
                        .addComponent(cbFilterMinggu, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 50, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbFilterMingguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterMingguActionPerformed
        tampilkanLaporan();
    }//GEN-LAST:event_cbFilterMingguActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Laporan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<PeriodeLaporan> cbFilterMinggu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblTotalLaba;
    private javax.swing.JLabel lblTotalPemasukan;
    private javax.swing.JLabel lblTotalPengeluaran;
    private javax.swing.JTable tblLaporan;
    // End of variables declaration//GEN-END:variables
}
