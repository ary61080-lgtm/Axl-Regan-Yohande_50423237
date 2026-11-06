package com.mycompany.cobamvc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ============================================================================
 * FULL PROJECT SISTEM SALES ORDER PENGADAAN SEKOLAH (DANA BOS)
 * CV. AXL SARANA MANDIRI - Direktur: Axl Regan Yohande
 * Java Swing GUI + JDBC MySQL XAMPP (NetBeans 17 / JDK 17-22)
 * ============================================================================
 */
public class Main extends JFrame {

    // Komponen GUI Form Input
    private JTextField txtNomorPesanan;
    private JComboBox<String> cmbSekolah;
    private JComboBox<String> cmbBarang;
    private JTextField txtQty;
    private JTextField txtHargaSatuan;
    private JTextField txtSubtotal;
    private JTextField txtPajakPpn;
    private JTextField txtGrandTotal;
    private JComboBox<String> cmbStatus;

    // Komponen Tabel & Database
    private JTable tabelPesanan;
    private DefaultTableModel tableModel;
    private JLabel lblStatusKoneksi;

    // Formatter Rupiah
    private final DecimalFormat df = new DecimalFormat("#,###");

    // Daftar Harga Barang Katalog Pengadaan Sekolah
    private final String[][] KATALOG_BARANG = {
        {"Laptop Chromebook ASUS C214", "6500000", "Unit"},
        {"Proyektor Epson EB-X500", "5800000", "Unit"},
        {"PC Desktop Lab Core i5 16GB", "8200000", "Unit"},
        {"Interactive Smart Board 65 Inch", "24500000", "Unit"},
        {"Meja & Kursi Siswa Ergonomis", "850000", "Set"}
    };

    public Main() {
        super("Dashboard Sales Order Pengadaan Sekolah - CV. AXL SARANA MANDIRI");
        initComponents();
        persiapkanDatabase();
        muatDataDariDatabase();
    }

    public static void main(String[] args) {
        // Set Look and Feel Modern
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    public void bukaAplikasi() {
        this.setVisible(true);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // -------------------------------------------------------------
        // 1. HEADER ATAS
        // -------------------------------------------------------------
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(30, 41, 59));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblJudul = new JLabel("SISTEM SALES ORDER PENGADAAN SEKOLAH (DANA BOS)");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblJudul.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Penyedia: CV. AXL SARANA MANDIRI  |  Direktur: Axl Regan Yohande");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(203, 213, 225));

        JPanel pnlTitle = new JPanel(new GridLayout(2, 1, 2, 2));
        pnlTitle.setOpaque(false);
        pnlTitle.add(lblJudul);
        pnlTitle.add(lblSub);

        lblStatusKoneksi = new JLabel("● Menghubungkan ke MySQL XAMPP...");
        lblStatusKoneksi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatusKoneksi.setForeground(new Color(250, 204, 21));

        pnlHeader.add(pnlTitle, BorderLayout.WEST);
        pnlHeader.add(lblStatusKoneksi, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // -------------------------------------------------------------
        // 2. KONTEN TENGAH (FORM INPUT + TABEL)
        // -------------------------------------------------------------
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(420);
        splitPane.setContinuousLayout(true);

        // --- Panel Kiri: Form Input Transaksi Sales Order ---
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Form Input Pesanan Baru (Sales Order)", 
            0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(15, 23, 42)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);

        // No Pesanan
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlForm.add(new JLabel("No. Pesanan:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNomorPesanan = new JTextField("SO-" + System.currentTimeMillis() % 100000);
        pnlForm.add(txtNomorPesanan, gbc);

        // Sekolah Tujuan (Bisa Pilih atau Ketik Sendiri)
        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Sekolah Penerima:"), gbc);
        gbc.gridx = 1;
        cmbSekolah = new JComboBox<>(new String[]{
            "SMKN 1 Jakarta (NPSN: 20100123)",
            "SMAN 3 Bandung (NPSN: 20219876)",
            "SMPN 5 Surabaya (NPSN: 20534567)",
            "SDN Cibubur 01 Pagi (NPSN: 20108990)",
            "SMKN 2 Yogyakarta (NPSN: 20401245)"
        });
        cmbSekolah.setEditable(true); // User bisa pilih atau ketik nama sekolah baru bebas
        pnlForm.add(cmbSekolah, gbc);

        // Barang Pengadaan (Bisa Pilih atau Ketik Sendiri)
        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(new JLabel("Pilih / Ketik Barang:"), gbc);
        gbc.gridx = 1;
        String[] namaBarangList = new String[KATALOG_BARANG.length];
        for (int i = 0; i < KATALOG_BARANG.length; i++) {
            namaBarangList[i] = KATALOG_BARANG[i][0];
        }
        cmbBarang = new JComboBox<>(namaBarangList);
        cmbBarang.setEditable(true); // User bisa pilih atau ketik nama barang baru bebas
        pnlForm.add(cmbBarang, gbc);

        // Qty
        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("Jumlah (Qty):"), gbc);
        gbc.gridx = 1;
        txtQty = new JTextField("10");
        pnlForm.add(txtQty, gbc);

        // Harga Satuan (Bisa diubah/diketik bebas)
        gbc.gridx = 0; gbc.gridy = 4;
        pnlForm.add(new JLabel("Harga Satuan (Rp):"), gbc);
        gbc.gridx = 1;
        txtHargaSatuan = new JTextField(KATALOG_BARANG[0][1]);
        pnlForm.add(txtHargaSatuan, gbc);

        // Subtotal
        gbc.gridx = 0; gbc.gridy = 5;
        pnlForm.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 1;
        txtSubtotal = new JTextField();
        txtSubtotal.setEditable(false);
        txtSubtotal.setBackground(new Color(241, 245, 249));
        pnlForm.add(txtSubtotal, gbc);

        // PPN 11%
        gbc.gridx = 0; gbc.gridy = 6;
        pnlForm.add(new JLabel("PPN 11%:"), gbc);
        gbc.gridx = 1;
        txtPajakPpn = new JTextField();
        txtPajakPpn.setEditable(false);
        txtPajakPpn.setBackground(new Color(241, 245, 249));
        pnlForm.add(txtPajakPpn, gbc);

        // Grand Total
        gbc.gridx = 0; gbc.gridy = 7;
        pnlForm.add(new JLabel("Grand Total (Rp):"), gbc);
        gbc.gridx = 1;
        txtGrandTotal = new JTextField();
        txtGrandTotal.setEditable(false);
        txtGrandTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtGrandTotal.setForeground(new Color(21, 128, 61));
        txtGrandTotal.setBackground(new Color(240, 253, 244));
        pnlForm.add(txtGrandTotal, gbc);

        // Status Pengiriman
        gbc.gridx = 0; gbc.gridy = 8;
        pnlForm.add(new JLabel("Status Order:"), gbc);
        gbc.gridx = 1;
        cmbStatus = new JComboBox<>(new String[]{"DIPROSES", "DIKIRIM", "SELESAI", "MENUNGGU"});
        pnlForm.add(cmbStatus, gbc);

        // Event Listener: saat memilih barang dari daftar, update harga otomatis
        cmbBarang.addActionListener(e -> {
            int idx = cmbBarang.getSelectedIndex();
            if (idx >= 0 && idx < KATALOG_BARANG.length) {
                txtHargaSatuan.setText(KATALOG_BARANG[idx][1]);
            }
            hitungKalkulasi();
        });

        txtQty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungKalkulasi();
            }
        });

        txtHargaSatuan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungKalkulasi();
            }
        });

        // Tombol Simpan & Reset
        JPanel pnlBtnForm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnReset = new JButton("Reset Form");
        JButton btnSimpan = new JButton("💾 Simpan ke MySQL");
        btnSimpan.setBackground(new Color(37, 99, 235));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnReset.addActionListener(e -> resetForm());
        btnSimpan.addActionListener(e -> simpanKeDatabase());

        pnlBtnForm.add(btnReset);
        pnlBtnForm.add(btnSimpan);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        pnlForm.add(pnlBtnForm, gbc);

        splitPane.setLeftComponent(new JScrollPane(pnlForm));

        // --- Panel Kanan: Tabel Riwayat Data Pesanan ---
        JPanel pnlTabel = new JPanel(new BorderLayout(5, 5));
        pnlTabel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Data Pesanan di Database MySQL (db_pengadaan_sekolah)", 
            0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(15, 23, 42)
        ));

        String[] kolom = {"ID", "No. Pesanan", "Sekolah Tujuan", "Tanggal", "Total Nilai (Rp)", "Status"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelPesanan = new JTable(tableModel);
        tabelPesanan.setRowHeight(25);
        tabelPesanan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelPesanan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlTabel.add(new JScrollPane(tabelPesanan), BorderLayout.CENTER);

        // Tombol Aksi Tabel
        JPanel pnlAksiTabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton btnRefresh = new JButton("🔄 Muat Ulang / Refresh");
        JButton btnHapus = new JButton("🗑 Hapus Pesanan");
        JButton btnCetak = new JButton("📄 Cetak Bukti Invoice");

        btnRefresh.addActionListener(e -> muatDataDariDatabase());
        btnHapus.addActionListener(e -> hapusPesanan());
        btnCetak.addActionListener(e -> cetakBuktiInvoice());

        pnlAksiTabel.add(btnRefresh);
        pnlAksiTabel.add(btnHapus);
        pnlAksiTabel.add(btnCetak);
        pnlTabel.add(pnlAksiTabel, BorderLayout.SOUTH);

        splitPane.setRightComponent(pnlTabel);
        add(splitPane, BorderLayout.CENTER);

        // Hitung awal
        hitungKalkulasi();
    }

    private void hitungKalkulasi() {
        try {
            int qty = Integer.parseInt(txtQty.getText().trim());
            double harga = Double.parseDouble(txtHargaSatuan.getText().trim());
            if (qty < 0 || harga < 0) return;

            double subtotal = qty * harga;
            double ppn = subtotal * 0.11;
            double grandTotal = subtotal + ppn;

            txtSubtotal.setText("Rp " + df.format(subtotal));
            txtPajakPpn.setText("Rp " + df.format(ppn));
            txtGrandTotal.setText("Rp " + df.format(grandTotal));
        } catch (Exception ignored) {
            txtSubtotal.setText("Rp 0");
            txtPajakPpn.setText("Rp 0");
            txtGrandTotal.setText("Rp 0");
        }
    }

    private void resetForm() {
        txtNomorPesanan.setText("SO-" + System.currentTimeMillis() % 100000);
        cmbSekolah.setSelectedIndex(0);
        cmbBarang.setSelectedIndex(0);
        txtQty.setText("10");
        txtHargaSatuan.setText(KATALOG_BARANG[0][1]);
        cmbStatus.setSelectedIndex(0);
        hitungKalkulasi();
    }

    /**
     * Otomatis membuat tabel di MySQL jika belum ada
     */
    private void persiapkanDatabase() {
        try (Connection conn = KoneksiDB.getConnection()) {
            lblStatusKoneksi.setText("● MySQL XAMPP Terhubung (db_pengadaan_sekolah)");
            lblStatusKoneksi.setForeground(new Color(74, 222, 128));

            String sql = "CREATE TABLE IF NOT EXISTS pesanan (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "nomor_pesanan VARCHAR(50) NOT NULL, " +
                         "nama_sekolah VARCHAR(100) NOT NULL, " +
                         "tanggal DATE NOT NULL, " +
                         "total_nilai DOUBLE NOT NULL, " +
                         "status_pengiriman VARCHAR(50) NOT NULL" +
                         ")";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            // Cek jika tabel kosong, isi 2 data contoh
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM pesanan");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate("INSERT INTO pesanan (nomor_pesanan, nama_sekolah, tanggal, total_nilai, status_pengiriman) VALUES " +
                    "('SO-2026-001', 'SMKN 1 Jakarta', CURDATE(), 108225000, 'SELESAI'), " +
                    "('SO-2026-002', 'SMAN 3 Bandung', CURDATE(), 64380000, 'DIKIRIM')");
            }
        } catch (Exception e) {
            lblStatusKoneksi.setText("● Gagal Terhubung ke MySQL XAMPP!");
            lblStatusKoneksi.setForeground(new Color(248, 113, 113));
            System.err.println("Gagal koneksi database: " + e.getMessage());
        }
    }

    /**
     * Membaca tabel pesanan dari MySQL ke JTable
     */
    private void muatDataDariDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = KoneksiDB.getConnection()) {
            String sql = "SELECT * FROM pesanan ORDER BY id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = 0;
                try { id = rs.getInt("id"); } catch (Exception ignored) {}

                String noPesanan = rs.getString("nomor_pesanan");
                String sekolah = rs.getString("nama_sekolah");

                String tgl = "";
                try {
                    tgl = rs.getString("tanggal_pesanan");
                } catch (Exception e) {
                    try { tgl = rs.getString("tanggal"); } catch (Exception ignored) {}
                }
                if (tgl == null || tgl.isEmpty()) {
                    tgl = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                }

                double total = 0.0;
                try {
                    total = rs.getDouble("total");
                } catch (Exception e) {
                    try { total = rs.getDouble("total_nilai"); } catch (Exception ignored) {}
                }

                String status = "";
                try {
                    status = rs.getString("status_pengiriman");
                } catch (Exception ignored) {}

                tableModel.addRow(new Object[]{
                    id, noPesanan, sekolah, tgl, "Rp " + df.format(total), status
                });
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat data: " + e.getMessage());
        }
    }

    /**
     * Simpan data pesanan baru ke MySQL melalui JDBC
     */
    private void simpanKeDatabase() {
        String noPesanan = txtNomorPesanan.getText().trim();
        String sekolah = cmbSekolah.getSelectedItem() != null ? cmbSekolah.getSelectedItem().toString().trim() : "";
        String barang = cmbBarang.getSelectedItem() != null ? cmbBarang.getSelectedItem().toString().trim() : "";
        String status = (String) cmbStatus.getSelectedItem();

        if (noPesanan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nomor Pesanan tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (sekolah.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama sekolah tidak boleh kosong! (Pilih atau ketik langsung)", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (barang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama barang tidak boleh kosong! (Pilih atau ketik langsung)", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int qty = Integer.parseInt(txtQty.getText().trim());
            double harga = Double.parseDouble(txtHargaSatuan.getText().trim());
            double subtotal = qty * harga;
            double ppn = subtotal * 0.11;
            double grandTotal = subtotal + ppn;

            try (Connection conn = KoneksiDB.getConnection()) {
                // Deteksi apakah skema tabel menggunakan kolom tanggal_pesanan (dari file SQL pengadaan)
                boolean isSchemaResmi = false;
                try (ResultSet rsCols = conn.getMetaData().getColumns(null, null, "pesanan", "tanggal_pesanan")) {
                    if (rsCols.next()) {
                        isSchemaResmi = true;
                    }
                }

                if (isSchemaResmi) {
                    String sql = "INSERT INTO pesanan (id, nomor_pesanan, tanggal_pesanan, keterangan, nama_sekolah, " +
                                 "subtotal, tarif_ppn_persen, nominal_ppn, total, status_pengiriman) " +
                                 "VALUES (?, ?, CURDATE(), ?, ?, ?, 11.0, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, noPesanan);
                        ps.setString(2, noPesanan);
                        ps.setString(3, "Pengadaan: " + barang + " (" + qty + " Unit)");
                        ps.setString(4, sekolah);
                        ps.setDouble(5, subtotal);
                        ps.setDouble(6, ppn);
                        ps.setDouble(7, grandTotal);
                        ps.setString(8, status);
                        ps.executeUpdate();
                    }
                } else {
                    String sql = "INSERT INTO pesanan (nomor_pesanan, nama_sekolah, tanggal, total_nilai, status_pengiriman) " +
                                 "VALUES (?, ?, CURDATE(), ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, noPesanan);
                        ps.setString(2, sekolah);
                        ps.setDouble(3, grandTotal);
                        ps.setString(4, status);
                        ps.executeUpdate();
                    }
                }

                JOptionPane.showMessageDialog(this,
                    "DATA BERHASIL DISIMPAN KE MYSQL!\n\n" +
                    "No. Pesanan : " + noPesanan + "\n" +
                    "Sekolah     : " + sekolah + "\n" +
                    "Barang      : " + barang + "\n" +
                    "Jumlah (Qty): " + qty + "\n" +
                    "Harga Satuan: Rp " + df.format(harga) + "\n" +
                    "Subtotal    : Rp " + df.format(subtotal) + "\n" +
                    "PPN 11%     : Rp " + df.format(ppn) + "\n" +
                    "Grand Total : Rp " + df.format(grandTotal) + "\n" +
                    "Status      : " + status,
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

                resetForm();
                muatDataDariDatabase();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Qty dan Harga harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal simpan ke MySQL: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Hapus baris pesanan dari MySQL
     */
    private void hapusPesanan() {
        int selectedRow = tabelPesanan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu baris di tabel yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String noPesanan = (String) tableModel.getValueAt(selectedRow, 1);

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus pesanan " + noPesanan + " (ID: " + id + ") dari database?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try (Connection conn = KoneksiDB.getConnection()) {
                String sql = "DELETE FROM pesanan WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Pesanan " + noPesanan + " berhasil dihapus dari MySQL!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                muatDataDariDatabase();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Cetak Bukti Invoice Resmi CV. AXL SARANA MANDIRI
     */
    private void cetakBuktiInvoice() {
        int selectedRow = tabelPesanan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu pesanan di tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String noPesanan = (String) tableModel.getValueAt(selectedRow, 1);
        String sekolah = (String) tableModel.getValueAt(selectedRow, 2);
        String tanggal = (String) tableModel.getValueAt(selectedRow, 3);
        String total = (String) tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 5);

        String bukti = "================================================================\n" +
                       "         BUKTI SALES ORDER PENGADAAN SEKOLAH (DANA BOS)        \n" +
                       "                    CV. AXL SARANA MANDIRI                     \n" +
                       "================================================================\n" +
                       " No. Pesanan     : " + noPesanan + "\n" +
                       " Sekolah Penerima: " + sekolah + "\n" +
                       " Tanggal Order   : " + tanggal + "\n" +
                       " Total Tagihan   : " + total + " (Termasuk Pajak PPN 11%)\n" +
                       " Status Order    : " + status + "\n" +
                       "================================================================\n" +
                       " Mengetahui / Disetujui Oleh:\n\n\n" +
                       " ( Axl Regan Yohande )\n" +
                       " Direktur CV. AXL SARANA MANDIRI\n" +
                       "================================================================";

        JTextArea ta = new JTextArea(bukti);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Cetak Bukti Invoice: " + noPesanan, JOptionPane.INFORMATION_MESSAGE);
    }
}
