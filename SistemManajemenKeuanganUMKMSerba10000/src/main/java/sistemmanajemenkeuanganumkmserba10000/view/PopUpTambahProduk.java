package sistemmanajemenkeuanganumkmserba10000.view;

import sistemmanajemenkeuanganumkmserba10000.entity.Bahan;
import sistemmanajemenkeuanganumkmserba10000.entity.Produk;
import sistemmanajemenkeuanganumkmserba10000.entity.ProdukBahan;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Window;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopUpTambahProduk extends javax.swing.JDialog {

    private final List<Bahan> daftarBahan;
    private final Produk produkAwal;
    private final Map<Integer, BigDecimal> jumlahAwal = new HashMap<>();
    private final DefaultTableModel modelBahan = new DefaultTableModel(
            new Object[]{"Pakai", "Bahan", "Satuan", "Harga/unit", "Jumlah"}, 0) {
        @Override
        public Class<?> getColumnClass(int column) {
            return column == 0 ? Boolean.class : (column == 4 ? BigDecimal.class : String.class);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0 || column == 4;
        }
    };
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
        initComponents();
        tblBahan.setModel(modelBahan);
        tblBahan.getColumnModel().getColumn(0).setMaxWidth(60);
        tblBahan.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()));
        muatBahan();
        if (produk != null) {
            lblJudul.setText("Ubah Produk");
            btnSimpan.setText("Simpan perubahan");
            txtNama.setText(produk.getNama());
            txtHarga.setText(produk.getHarga().stripTrailingZeros().toPlainString());
        }
        getRootPane().setDefaultButton(btnSimpan);
        pack();
        setMinimumSize(new java.awt.Dimension(700, 520));
        setLocationRelativeTo(owner);
    }

    public Produk getProdukHasil() {
        return produkHasil;
    }

    public List<ProdukBahan> getKomposisiHasil() {
        return komposisiHasil;
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
        lblHarga = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        lblBahan = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBahan = new javax.swing.JTable();
        btnBatal = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlHeader.setBackground(new java.awt.Color(91, 126, 60));

        lblJudul.setFont(new java.awt.Font("Goudy Old Style", 1, 22)); // NOI18N
        lblJudul.setForeground(new java.awt.Color(253, 253, 235));
        lblJudul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul.setText("Tambah Produk");

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
        lblNama.setText("Nama produk");

        txtNama.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        txtNama.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(162, 203, 139)), javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        lblHarga.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        lblHarga.setForeground(new java.awt.Color(91, 126, 60));
        lblHarga.setText("Harga jual (Rp)");

        txtHarga.setEditable(false);
        txtHarga.setBackground(new java.awt.Color(238, 244, 222));
        txtHarga.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        txtHarga.setForeground(new java.awt.Color(91, 126, 60));
        txtHarga.setText("10000");
        txtHarga.setToolTipText("Harga produk mengikuti konsep Serba Rp10.000.");
        txtHarga.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(162, 203, 139)), javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        lblBahan.setFont(new java.awt.Font("Lucida Bright", 1, 12)); // NOI18N
        lblBahan.setForeground(new java.awt.Color(91, 126, 60));
        lblBahan.setText("Pilih bahan dan jumlah yang dipakai untuk satu produk");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(162, 203, 139)));

        tblBahan.setFillsViewportHeight(true);
        tblBahan.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        tblBahan.setGridColor(new java.awt.Color(214, 230, 200));
        tblBahan.setRowHeight(26);
        tblBahan.setSelectionBackground(new java.awt.Color(162, 203, 139));
        tblBahan.setSelectionForeground(new java.awt.Color(40, 60, 25));
        tblBahan.setShowGrid(true);
        jScrollPane1.setViewportView(tblBahan);

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
                            .addComponent(lblHarga))
                        .addGap(18, 18, 18)
                        .addGroup(pnlIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblBahan)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
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
                    .addComponent(lblHarga)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(lblBahan)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addGap(20, 20, 20)
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

    private void muatBahan() {
        for (Bahan bahan : daftarBahan) {
            BigDecimal quantity = jumlahAwal.getOrDefault(bahan.getId(), BigDecimal.ONE);
            modelBahan.addRow(new Object[]{jumlahAwal.containsKey(bahan.getId()), bahan.getNama(),
                bahan.getSatuan(), "Rp " + bahan.getHarga().toPlainString(), quantity});
        }
    }

    private void simpan() {
        if (tblBahan.isEditing()) {
            tblBahan.getCellEditor().stopCellEditing();
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBahan;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JLabel lblNama;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlIsi;
    private javax.swing.JTable tblBahan;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
