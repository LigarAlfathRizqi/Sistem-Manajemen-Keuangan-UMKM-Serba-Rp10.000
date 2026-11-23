/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemmanajemenkeuanganumkmserba10000.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import sistemmanajemenkeuanganumkmserba10000.entity.Transaksi;
import sistemmanajemenkeuanganumkmserba10000.model.LaporanTableModel;
import sistemmanajemenkeuanganumkmserba10000.model.RupiahUtil;
import sistemmanajemenkeuanganumkmserba10000.repository.TransaksiRepository;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import java.awt.event.ActionListener;

/**
 *
 * @author ASUS ROG
 */
public class DashboardFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardFrame.class.getName());
    private final TransaksiRepository transaksiRepository = new TransaksiRepository();
    private final JLabel lblProdukTerlaris = new JLabel("Belum ada transaksi", JLabel.CENTER);
    private final JLabel lblPengeluaranTerbesar = new JLabel("Belum ada transaksi", JLabel.CENTER);

    /**
     * Creates new form DashboardFrame
     */
    public DashboardFrame() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        for (ActionListener listener : cbFilterMinggu.getActionListeners()) {
            cbFilterMinggu.removeActionListener(listener);
        }
        cbFilterMinggu.removeAllItems();
        for (int i = 0; i < 12; i++) {
            cbFilterMinggu.addItem(i == 0 ? "Minggu ini" : "" + i + " minggu lalu");
        }
        cbFilterMinggu.addActionListener(this::cbFilterMingguActionPerformed);
        jPanel12.setLayout(new BorderLayout());
        jPanel12.add(jLabel8, BorderLayout.NORTH);
        jPanel12.add(lblProdukTerlaris, BorderLayout.CENTER);
        jPanel14.setLayout(new BorderLayout());
        jPanel14.add(jLabel9, BorderLayout.NORTH);
        jPanel14.add(lblPengeluaranTerbesar, BorderLayout.CENTER);
        jLabel9.setText("Pengeluaran Terbesar");
        Navigasi.pasang(this);
        muatDashboard();
    }

    private LocalDate awalMingguTerpilih() {
        int index = Math.max(0, cbFilterMinggu.getSelectedIndex());
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(index);
    }

    private void muatDashboard() {
        LocalDate mulai = awalMingguTerpilih();
        LocalDate sampai = mulai.plusDays(6);
        try {
            List<Transaksi> transaksi = transaksiRepository.findByRange(mulai, sampai);
            LaporanTableModel ringkasan = new LaporanTableModel();
            ringkasan.setData(transaksi);
            jLabel10.setText("<html><center>Pemasukan<br>" + RupiahUtil.format(ringkasan.getTotalPemasukan()) + "</center></html>");
            jLabel11.setText("<html><center>Pengeluaran<br>" + RupiahUtil.format(ringkasan.getTotalPengeluaran()) + "</center></html>");
            jLabel12.setText("<html><center>Laba<br>" + RupiahUtil.format(ringkasan.getTotalLaba()) + "</center></html>");
            tampilkanProdukTerlaris(transaksi);
            tampilkanPengeluaranTerbesar(transaksi);
            tampilkanGrafik(transaksi, mulai);
        } catch (java.sql.SQLException ex) {
            UiUtil.showError(this, ex);
            jLabel10.setText("<html><center>Pemasukan<br>Rp 0</center></html>");
            jLabel11.setText("<html><center>Pengeluaran<br>Rp 0</center></html>");
            jLabel12.setText("<html><center>Laba<br>Rp 0</center></html>");
            lblProdukTerlaris.setText("Database belum tersedia");
            lblPengeluaranTerbesar.setText("Database belum tersedia");
            tampilkanGrafik(List.of(), mulai);
        }
    }

    private void tampilkanProdukTerlaris(List<Transaksi> transaksi) {
        Map<String, Integer> jumlahPerProduk = new HashMap<>();
        for (Transaksi item : transaksi) {
            jumlahPerProduk.merge(item.getProduk().getNama(), item.getJumlah(), Integer::sum);
        }
        String best = jumlahPerProduk.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " — " + entry.getValue() + " terjual")
                .orElse("Belum ada transaksi");
        lblProdukTerlaris.setText(best);
    }

    private void tampilkanPengeluaranTerbesar(List<Transaksi> transaksi) {
        Map<String, java.math.BigDecimal> biayaPerProduk = new HashMap<>();
        for (Transaksi item : transaksi) {
            java.math.BigDecimal biaya = item.getModalSatuan().multiply(java.math.BigDecimal.valueOf(item.getJumlah()));
            biayaPerProduk.merge(item.getProduk().getNama(), biaya, java.math.BigDecimal::add);
        }
        String largest = biayaPerProduk.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " — " + RupiahUtil.format(entry.getValue()))
                .orElse("Belum ada transaksi");
        lblPengeluaranTerbesar.setText(largest);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        lblTotalPemasukan = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblTotalLaba = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lblTotalPengeluaran = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        cbFilterMinggu = new javax.swing.JComboBox<>();
        panelGrafik = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblTotalPemasukan1 = new javax.swing.JLabel();

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

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(253, 253, 235));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(162, 203, 139));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setBackground(new java.awt.Color(253, 253, 235));
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
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addGap(33, 33, 33)
                .addComponent(jLabel3)
                .addGap(33, 33, 33)
                .addComponent(jLabel4)
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addGap(31, 31, 31)
                .addComponent(jLabel6)
                .addContainerGap(498, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 80, 110, 730);

        jPanel4.setBackground(new java.awt.Color(243, 243, 231));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 0, 204));
        jLabel10.setText("Pemasukan");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel10)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);
        jPanel4.setBounds(130, 150, 130, 81);

        jPanel5.setBackground(new java.awt.Color(243, 243, 231));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 0, 51));
        jLabel11.setText("Pengeluaran");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(19, 19, 19))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5);
        jPanel5.setBounds(260, 150, 120, 81);

        jPanel8.setBackground(new java.awt.Color(243, 243, 231));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 153, 0));
        jLabel12.setText("Laba");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(43, 43, 43))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8);
        jPanel8.setBounds(380, 150, 130, 81);

        cbFilterMinggu.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        cbFilterMinggu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbFilterMinggu.addActionListener(this::cbFilterMingguActionPerformed);
        jPanel1.add(cbFilterMinggu);
        cbFilterMinggu.setBounds(410, 260, 90, 21);

        panelGrafik.setBackground(new java.awt.Color(190, 218, 174));
        panelGrafik.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelGrafikLayout = new javax.swing.GroupLayout(panelGrafik);
        panelGrafik.setLayout(panelGrafikLayout);
        panelGrafikLayout.setHorizontalGroup(
            panelGrafikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 378, Short.MAX_VALUE)
        );
        panelGrafikLayout.setVerticalGroup(
            panelGrafikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        jPanel1.add(panelGrafik);
        panelGrafik.setBounds(130, 300, 380, 279);

        jPanel12.setBackground(new java.awt.Color(243, 243, 231));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(91, 126, 60));
        jLabel8.setText("Produk Terlaris");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(52, 52, 52))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(129, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel12);
        jPanel12.setBounds(130, 630, 200, 152);

        jPanel14.setBackground(new java.awt.Color(243, 243, 231));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(91, 126, 60));
        jLabel9.setText("Penjualan Terbesar");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(27, 27, 27))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(129, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel14);
        jPanel14.setBounds(300, 630, 210, 152);

        jLabel13.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(91, 126, 60));
        jLabel13.setText("Lihat Detail Mingguan");
        jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDetailMingguan(evt);
            }
        });
        jPanel1.add(jLabel13);
        jLabel13.setBounds(130, 260, 140, 15);

        jPanel9.setBackground(new java.awt.Color(91, 126, 60));

        jLabel14.setFont(new java.awt.Font("Goudy Old Style", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(253, 253, 235));
        jLabel14.setText("Sistem Manajemen Keuangan UMKM Serba 10.000");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel9);
        jPanel9.setBounds(-9, 1, 550, 80);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 255));
        jLabel7.setText("Dashboard");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(238, 57, 71, 20);

        jLabel1.setFont(new java.awt.Font("Goudy Old Style", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(91, 126, 60));
        jLabel1.setText("Dashboard");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(260, 100, 130, 30);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 540, 810);

        lblTotalPemasukan1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalPemasukan1.setForeground(new java.awt.Color(102, 0, 204));
        lblTotalPemasukan1.setText("Pemasukan");
        getContentPane().add(lblTotalPemasukan1);
        lblTotalPemasukan1.setBounds(209, 362, 65, 16);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblDetailMingguan(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDetailMingguan
        new Laporan().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_lblDetailMingguan

    private void cbFilterMingguActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterMingguActionPerformed
        muatDashboard();
    }//GEN-LAST:event_cbFilterMingguActionPerformed

    private void tampilkanGrafik(List<Transaksi> transaksi, LocalDate mulai) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] hari = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
        java.math.BigDecimal[] pemasukan = new java.math.BigDecimal[7];
        java.math.BigDecimal[] pengeluaran = new java.math.BigDecimal[7];
        java.util.Arrays.fill(pemasukan, java.math.BigDecimal.ZERO);
        java.util.Arrays.fill(pengeluaran, java.math.BigDecimal.ZERO);
        for (Transaksi item : transaksi) {
            int index = (int) java.time.temporal.ChronoUnit.DAYS.between(mulai, item.getTanggal());
            if (index < 0 || index >= 7) continue;
            java.math.BigDecimal jumlah = java.math.BigDecimal.valueOf(item.getJumlah());
            pemasukan[index] = pemasukan[index].add(item.getHargaJualSatuan().multiply(jumlah));
            pengeluaran[index] = pengeluaran[index].add(item.getModalSatuan().multiply(jumlah));
        }
        for (int i = 0; i < hari.length; i++) {
            dataset.addValue(pemasukan[i], "Pemasukan", hari[i]);
            dataset.addValue(pengeluaran[i], "Pengeluaran", hari[i]);
            dataset.addValue(pemasukan[i].subtract(pengeluaran[i]), "Laba", hari[i]);
        }

        // 2. Buat Diagram JFreeChart
        JFreeChart chart = ChartFactory.createBarChart(
                "",                     
                "",                     
                "",                     
                dataset, 
                PlotOrientation.VERTICAL, 
                true,
                true, 
                false
        );

        // 3. Kustomisasi Warna Sesuai Contoh Gambar
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesPaint(2, new Color(34, 139, 34));
        renderer.setItemMargin(0.0);                        // Merapatkan batang

        // 4. Masukkan Grafik ke dalam panelGrafik dari Design
        ChartPanel chartPanel = new ChartPanel(chart);
        panelGrafik.setLayout(new BorderLayout());
        panelGrafik.removeAll();
        panelGrafik.add(chartPanel, BorderLayout.CENTER);
        panelGrafik.validate();
    }
    
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
        java.awt.EventQueue.invokeLater(() -> new DashboardFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbFilterMinggu;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblTotalLaba;
    private javax.swing.JLabel lblTotalPemasukan;
    private javax.swing.JLabel lblTotalPemasukan1;
    private javax.swing.JLabel lblTotalPengeluaran;
    private javax.swing.JPanel panelGrafik;
    // End of variables declaration//GEN-END:variables
}
