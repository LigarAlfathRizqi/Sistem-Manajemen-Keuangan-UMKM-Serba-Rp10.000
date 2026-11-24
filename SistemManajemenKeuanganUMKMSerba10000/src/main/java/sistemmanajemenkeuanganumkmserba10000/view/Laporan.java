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
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(253, 253, 235));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(162, 203, 139));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(104, 430));
        jPanel2.setLayout(null);

        jLabel8.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(253, 253, 235));
        jLabel8.setText("Dashboard");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(20, 49, 70, 15);

        jLabel7.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(253, 253, 235));
        jLabel7.setText("Transaksi");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(20, 97, 70, 15);

        jLabel9.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(253, 253, 235));
        jLabel9.setText("Produk");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(20, 143, 70, 15);

        jLabel10.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(253, 253, 235));
        jLabel10.setText("Bahan");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(20, 191, 60, 15);

        jLabel11.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(253, 253, 235));
        jLabel11.setText("Laporan");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(20, 237, 60, 15);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 70, 110, 410);

        jLabel2.setFont(new java.awt.Font("Goudy Old Style", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(91, 126, 60));
        jLabel2.setText("Laporan Detail");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(330, 80, 200, 30);

        cbFilterMinggu.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        cbFilterMinggu.addActionListener(this::cbFilterMingguActionPerformed);
        jPanel1.add(cbFilterMinggu);
        cbFilterMinggu.setBounds(440, 110, 250, 20);

        jPanel3.setBackground(new java.awt.Color(243, 243, 231));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalPemasukan.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        lblTotalPemasukan.setForeground(new java.awt.Color(102, 0, 204));
        lblTotalPemasukan.setText("     Pemasukan");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalPemasukan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalPemasukan)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(170, 140, 120, 57);

        jPanel6.setBackground(new java.awt.Color(243, 243, 231));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalLaba.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        lblTotalLaba.setForeground(new java.awt.Color(0, 153, 51));
        lblTotalLaba.setText("Laba");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(lblTotalLaba)
                .addGap(45, 45, 45))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalLaba)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6);
        jPanel6.setBounds(530, 140, 120, 57);

        jPanel7.setBackground(new java.awt.Color(243, 243, 231));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTotalPengeluaran.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        lblTotalPengeluaran.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalPengeluaran.setText("    Pengeluaran");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalPengeluaran)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalPengeluaran)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7);
        jPanel7.setBounds(350, 140, 120, 57);

        tblLaporan.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
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

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(130, 230, 560, 180);

        jPanel4.setBackground(new java.awt.Color(91, 126, 60));

        jLabel14.setFont(new java.awt.Font("Goudy Old Style", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(253, 253, 235));
        jLabel14.setText("Sistem Manajemen Keuangan UMKM Serba 10.000");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4);
        jPanel4.setBounds(-9, 1, 730, 70);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(730, 510));
        setLocationRelativeTo(null);
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalLaba;
    private javax.swing.JLabel lblTotalPemasukan;
    private javax.swing.JLabel lblTotalPengeluaran;
    private javax.swing.JTable tblLaporan;
    // End of variables declaration//GEN-END:variables
}
