package com.mycompany.cobamvc;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

/**
 * ============================================================================
 * PERTEMUAN 6: JASPERREPORTS COMMUNITY v6 (TANPA PERLU INSTALL JASPERSOFT STUDIO)
 * ============================================================================
 * Fitur:
 * 1. Rekap Laporan Sales Order: Bisa SEMUA PESANAN atau PILIH SALAH SATU pesanan.
 * 2. Invoice Resmi Sales Order: Tombol Cetak Invoice per pesanan via JasperReports.
 * 3. Format Output Lengkap: JasperViewer, PDF (.pdf), Excel (.xlsx), Word (.docx), HTML (.html).
 * ============================================================================
 */
public class LaporanJasperService {

    private static final Locale LOCALE_ID = new Locale.Builder().setLanguage("id").setRegion("ID").build();

    /**
     * Mengambil daftar pilihan pesanan untuk filter dropdown (Semua vs Pilih Satu)
     */
    public static List<String> muatDaftarOpsiPesanan() {
        List<String> opsi = new ArrayList<>();
        opsi.add("SEMUA PESANAN (Rekapitulasi Lengkap)");

        try (Connection conn = KoneksiDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nomor_pesanan, nama_sekolah FROM pesanan ORDER BY tanggal_pesanan DESC, nomor_pesanan DESC")) {
            while (rs.next()) {
                String no = rs.getString("nomor_pesanan");
                String sek = rs.getString("nama_sekolah");
                if (no != null && !no.isEmpty()) {
                    opsi.add(no + " — " + (sek != null ? sek : ""));
                }
            }
        } catch (Exception e) {
            System.err.println("Info muat opsi pesanan: " + e.getMessage());
        }

        if (opsi.size() == 1) {
            opsi.add("SO-2026-001 — SMKN 1 Jakarta");
            opsi.add("SO-2026-002 — SMAN 3 Jakarta");
        }
        return opsi;
    }

    /**
     * Mengkompilasi LaporanSalesOrder.jrxml & mengisi data LANGSUNG lewat JDBC Connection (queryString di dalam .jrxml)
     */
    public static JasperPrint buildJasperPrint(String filterNomorPesanan) throws Exception {
        InputStream jrxmlStream = LaporanJasperService.class.getResourceAsStream("/LaporanSalesOrder.jrxml");
        if (jrxmlStream == null) {
            throw new IllegalStateException("Template /LaporanSalesOrder.jrxml tidak ditemukan di src/main/resources!");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        Map<String, Object> params = new HashMap<>();
        params.put("NAMA_PERUSAHAAN", "CV. AXL SARANA MANDIRI");
        params.put("DIREKTUR", "Axl Regan Yohande");
        params.put("TANGGAL_CETAK", new SimpleDateFormat("dd MMMM yyyy HH:mm", LOCALE_ID).format(new Date()));
        params.put("FILTER_PESANAN", filterNomorPesanan != null ? filterNomorPesanan.trim() : "");

        try (Connection conn = KoneksiDB.getConnection()) {
            return JasperFillManager.fillReport(jasperReport, params, conn);
        }
    }

    public static JasperPrint buildJasperPrint() throws Exception {
        return buildJasperPrint(null);
    }

    /**
     * Mengkompilasi InvoiceSalesOrder.jrxml & mengisi data LANGSUNG lewat JDBC Connection (queryString di dalam .jrxml)
     */
    public static JasperPrint buildInvoicePrint(String nomorPesanan) throws Exception {
        InputStream jrxmlStream = LaporanJasperService.class.getResourceAsStream("/InvoiceSalesOrder.jrxml");
        if (jrxmlStream == null) {
            throw new IllegalStateException("Template /InvoiceSalesOrder.jrxml tidak ditemukan di src/main/resources!");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        Map<String, Object> params = new HashMap<>();
        params.put("NAMA_PERUSAHAAN", "CV. AXL SARANA MANDIRI");
        params.put("DIREKTUR", "Axl Regan Yohande");
        params.put("NO_INVOICE", "INV-" + nomorPesanan);
        params.put("NO_PESANAN", nomorPesanan);
        params.put("TANGGAL_CETAK", new SimpleDateFormat("dd MMMM yyyy HH:mm", LOCALE_ID).format(new Date()));

        try (Connection conn = KoneksiDB.getConnection()) {
            return JasperFillManager.fillReport(jasperReport, params, conn);
        }
    }

    /**
     * Helper umum: jalankan ekspor atau viewer dari objek JasperPrint mana pun (Laporan atau Invoice)
     */
    private static void prosesOutputJasper(Component parent, JasperPrint jp, int modeIndex, String baseFileName, String viewerTitle) throws Exception {
        if (modeIndex == 0) {
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setTitle(viewerTitle);
            viewer.setVisible(true);
        } else if (modeIndex == 1) {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(baseFileName + ".pdf"));
            if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pdf")) f = new File(f.getAbsolutePath() + ".pdf");
                JasperExportManager.exportReportToPdfFile(jp, f.getAbsolutePath());
                JOptionPane.showMessageDialog(parent, "File PDF berhasil disimpan di:\n" + f.getAbsolutePath(), "Ekspor PDF Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (modeIndex == 2) {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(baseFileName + ".xlsx"));
            if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".xlsx")) f = new File(f.getAbsolutePath() + ".xlsx");
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jp));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(f));
                SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
                config.setOnePagePerSheet(false);
                config.setDetectCellType(true);
                config.setCollapseRowSpan(false);
                exporter.setConfiguration(config);
                exporter.exportReport();
                JOptionPane.showMessageDialog(parent, "File Excel (.xlsx) berhasil disimpan di:\n" + f.getAbsolutePath(), "Ekspor Excel Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (modeIndex == 3) {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(baseFileName + ".docx"));
            if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".docx")) f = new File(f.getAbsolutePath() + ".docx");
                JRDocxExporter exporter = new JRDocxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jp));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(f));
                exporter.exportReport();
                JOptionPane.showMessageDialog(parent, "File Word (.docx) berhasil disimpan di:\n" + f.getAbsolutePath(), "Ekspor Word Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (modeIndex == 4) {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(baseFileName + ".html"));
            if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".html")) f = new File(f.getAbsolutePath() + ".html");
                JasperExportManager.exportReportToHtmlFile(jp, f.getAbsolutePath());
                JOptionPane.showMessageDialog(parent, "File HTML (.html) berhasil disimpan di:\n" + f.getAbsolutePath(), "Ekspor HTML Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void tampilkanJasperViewer() throws Exception {
        JasperPrint jp = buildJasperPrint(null);
        JasperViewer viewer = new JasperViewer(jp, false);
        viewer.setTitle("JasperViewer v6 — Laporan Sales Order CV. AXL SARANA MANDIRI");
        viewer.setVisible(true);
    }

    public static void eksporKePdf(File targetFile) throws Exception {
        JasperExportManager.exportReportToPdfFile(buildJasperPrint(null), targetFile.getAbsolutePath());
    }

    public static void eksporKeExcel(File targetFile) throws Exception {
        JasperPrint jp = buildJasperPrint(null);
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jp));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(targetFile));
        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setOnePagePerSheet(false);
        config.setDetectCellType(true);
        config.setCollapseRowSpan(false);
        exporter.setConfiguration(config);
        exporter.exportReport();
    }

    public static void eksporKeWord(File targetFile) throws Exception {
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(buildJasperPrint(null)));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(targetFile));
        exporter.exportReport();
    }

    public static void eksporKeHtml(File targetFile) throws Exception {
        JasperExportManager.exportReportToHtmlFile(buildJasperPrint(null), targetFile.getAbsolutePath());
    }

    /**
     * Menu Cetak INVOICE Resmi (per 1 Nomor Pesanan terpilih di tabel) via JasperReports v6
     */
    public static void tampilkanMenuInvoice(Component parent, String nomorPesanan) {
        String[] opsi = {
            "Pratinjau Invoice (JasperViewer)",
            "Ekspor PDF (.pdf)",
            "Ekspor Excel (.xlsx)",
            "Ekspor Word (.docx)",
            "Ekspor HTML (.html)"
        };

        int pilihan = JOptionPane.showOptionDialog(
            parent,
            "Cetak INVOICE Resmi via JasperReports v6 untuk Pesanan: " + nomorPesanan,
            "Invoice JasperReports v6 — " + nomorPesanan,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opsi,
            opsi[0]
        );

        if (pilihan < 0) return;

        try {
            JasperPrint jp = buildInvoicePrint(nomorPesanan);
            String safeNo = nomorPesanan.replaceAll("[^a-zA-Z0-9_-]", "_");
            prosesOutputJasper(parent, jp, pilihan, "Invoice_" + safeNo, "Invoice Resmi " + nomorPesanan + " — CV. AXL SARANA MANDIRI");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Gagal mencetak Invoice JasperReports: " + ex.getMessage(), "Error JasperReports", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Menu Laporan Rekap: Bisa pilih SEMUA pesanan ATAU SALAH SATU pesanan saja!
     */
    public static void tampilkanMenuLaporan(Component parent, String defaultNomorPesanan) {
        List<String> daftarOpsi = muatDaftarOpsiPesanan();
        JComboBox<String> cmbFilter = new JComboBox<>(daftarOpsi.toArray(new String[0]));

        if (defaultNomorPesanan != null && !defaultNomorPesanan.isEmpty()) {
            for (int i = 1; i < daftarOpsi.size(); i++) {
                if (daftarOpsi.get(i).startsWith(defaultNomorPesanan + " ")) {
                    cmbFilter.setSelectedIndex(i);
                    break;
                }
            }
        }

        String[] formatOpsi = {
            "Pratinjau JasperViewer",
            "File PDF (.pdf)",
            "File Excel (.xlsx)",
            "File Word (.docx)",
            "File HTML (.html)"
        };
        JComboBox<String> cmbFormat = new JComboBox<>(formatOpsi);

        JPanel panel = new JPanel(new GridLayout(4, 1, 6, 6));
        panel.add(new JLabel("Pilih Data Pesanan yang Ingin Dicetak (Semua atau Pilih Satu):"));
        panel.add(cmbFilter);
        panel.add(new JLabel("Pilih Format Output JasperReports v6:"));
        panel.add(cmbFormat);

        int res = JOptionPane.showConfirmDialog(
            parent,
            panel,
            "Laporan Resmi JasperReports v6 — Filter & Format",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (res != JOptionPane.OK_OPTION) return;

        String selectedFilter = (String) cmbFilter.getSelectedItem();
        String filterNo = null;
        String fileSuffix = "Semua_Pesanan";
        if (cmbFilter.getSelectedIndex() > 0 && selectedFilter != null) {
            int dashIdx = selectedFilter.indexOf(" — ");
            filterNo = dashIdx > 0 ? selectedFilter.substring(0, dashIdx).trim() : selectedFilter.trim();
            fileSuffix = filterNo.replaceAll("[^a-zA-Z0-9_-]", "_");
        }

        int mode = cmbFormat.getSelectedIndex();

        try {
            JasperPrint jp = buildJasperPrint(filterNo);
            String title = filterNo == null
                    ? "JasperViewer v6 — Laporan Semua Pesanan CV. AXL SARANA MANDIRI"
                    : "JasperViewer v6 — Laporan Pesanan " + filterNo;
            prosesOutputJasper(parent, jp, mode, "Laporan_Sales_Order_" + fileSuffix, title);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Gagal memproses JasperReports: " + ex.getMessage(), "Error JasperReports", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void tampilkanMenuLaporan(Component parent) {
        tampilkanMenuLaporan(parent, null);
    }

    /**
     * Method main() agar bisa langsung di-Run File (Shift + F6) dari NetBeans!
     */
    public static void main(String[] args) {
        System.out.println("=== MENJALANKAN JASPERREPORTS v6 COMMUNITY (TANPA INSTALL JASPERSOFT STUDIO) ===");
        try {
            File pdfOut = new File("Laporan_Sales_Order_AXL.pdf");
            File xlsxOut = new File("Laporan_Sales_Order_AXL.xlsx");
            File docxOut = new File("Laporan_Sales_Order_AXL.docx");
            File htmlOut = new File("Laporan_Sales_Order_AXL.html");

            eksporKePdf(pdfOut);
            System.out.println("[OK] Ekspor PDF berhasil  : " + pdfOut.getAbsolutePath());

            eksporKeExcel(xlsxOut);
            System.out.println("[OK] Ekspor Excel berhasil: " + xlsxOut.getAbsolutePath());

            eksporKeWord(docxOut);
            System.out.println("[OK] Ekspor Word berhasil : " + docxOut.getAbsolutePath());

            eksporKeHtml(htmlOut);
            System.out.println("[OK] Ekspor HTML berhasil : " + htmlOut.getAbsolutePath());

            tampilkanJasperViewer();
            System.out.println("[OK] Jendela JasperViewer v6 terbuka!");
        } catch (Exception e) {
            System.err.println("[ERROR] Gagal menjalankan JasperReports: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
