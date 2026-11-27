package com.pengadaan.salesorder.service;

import com.pengadaan.salesorder.model.ItemBarang;
import com.pengadaan.salesorder.model.Pesanan;
import com.pengadaan.salesorder.util.DateUtil;
import com.pengadaan.salesorder.util.FormatRupiah;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Generator Dokumen Microsoft Word (.docx) resmi menggunakan Apache POI (poi-ooxml).
 */
public class WordGenerator {

    public static void generateWord(Pesanan pesanan, String jenisDokumen, File fileTarget) throws Exception {
        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(fileTarget)) {

            // 1. KOP SURAT
            XWPFParagraph pKop = doc.createParagraph();
            pKop.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun rKop1 = pKop.createRun();
            rKop1.setText(DokumenService.NAMA_PERUSAHAAN);
            rKop1.setBold(true);
            rKop1.setFontSize(14);
            rKop1.setFontFamily("Arial");
            rKop1.addBreak();

            XWPFRun rKop2 = pKop.createRun();
            rKop2.setText(DokumenService.BIDANG_USAHA);
            rKop2.setFontSize(10);
            rKop2.setFontFamily("Arial");
            rKop2.addBreak();

            XWPFRun rKop3 = pKop.createRun();
            rKop3.setText(DokumenService.ALAMAT_PERUSAHAAN + " | Telp: " + DokumenService.TELEPON_PERUSAHAAN);
            rKop3.setFontSize(9);
            rKop3.setFontFamily("Arial");
            rKop3.addBreak();

            // 2. JUDUL DOKUMEN
            XWPFParagraph pJudul = doc.createParagraph();
            pJudul.setAlignment(ParagraphAlignment.CENTER);
            pJudul.setSpacingBefore(200);

            String judulTeks = "SURAT PESANAN (PURCHASE ORDER)";
            if ("SPK".equalsIgnoreCase(jenisDokumen)) judulTeks = "SURAT PERINTAH KERJA (SPK)";
            else if ("KWITANSI".equalsIgnoreCase(jenisDokumen)) judulTeks = "KWITANSI PEMBAYARAN RESMI";
            else if ("BAST".equalsIgnoreCase(jenisDokumen)) judulTeks = "BERITA ACARA SERAH TERIMA BARANG (BAST)";

            XWPFRun rJudul = pJudul.createRun();
            rJudul.setText(judulTeks);
            rJudul.setBold(true);
            rJudul.setUnderline(UnderlinePatterns.SINGLE);
            rJudul.setFontSize(12);
            rJudul.setFontFamily("Arial");
            rJudul.addBreak();

            XWPFRun rNo = pJudul.createRun();
            rNo.setText("Nomor: " + jenisDokumen + "/" + pesanan.getNomorPesanan());
            rNo.setBold(true);
            rNo.setFontSize(10);
            rNo.setFontFamily("Arial");

            // 3. IDENTITAS SEKOLAH
            XWPFParagraph pIdentitas = doc.createParagraph();
            pIdentitas.setSpacingBefore(150);
            XWPFRun rId = pIdentitas.createRun();
            rId.setText("Instansi Sekolah : " + pesanan.getSekolah().getNamaSekolah());
            rId.addBreak();
            rId.setText("Alamat           : " + pesanan.getSekolah().getAlamatSekolah());
            rId.addBreak();
            rId.setText("Penerima/Kepsek  : " + pesanan.getSekolah().getNamaPenerima() + " (NIP: " + pesanan.getSekolah().getNipPenerima() + ")");
            rId.addBreak();
            rId.setText("Tanggal Pesanan  : " + DateUtil.formatKeIndonesia(pesanan.getTanggalPesanan()));

            // 4. TABEL BARANG
            XWPFTable table = doc.createTable();
            table.setWidth("100%");

            // Header Baris
            XWPFTableRow headerRow = table.getRow(0);
            headerRow.getCell(0).setText("No");
            headerRow.addNewTableCell().setText("Nama Barang / Spesifikasi");
            headerRow.addNewTableCell().setText("Kuantitas");
            headerRow.addNewTableCell().setText("Harga Satuan");
            headerRow.addNewTableCell().setText("Jumlah (Rp)");

            int no = 1;
            for (ItemBarang item : pesanan.getItems()) {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(String.valueOf(no++));
                row.getCell(1).setText(item.getNamaBarang());
                row.getCell(2).setText(item.getQty() + " " + item.getSatuan());
                row.getCell(3).setText(FormatRupiah.formatAngka(item.getHargaSatuan()));
                row.getCell(4).setText(FormatRupiah.formatAngka(item.getJumlah()));
            }

            // Summary Subtotal & Total
            XWPFParagraph pSummary = doc.createParagraph();
            pSummary.setSpacingBefore(150);
            XWPFRun rSum = pSummary.createRun();
            rSum.setText("Subtotal          : " + FormatRupiah.format(pesanan.getSubtotal()));
            rSum.addBreak();
            rSum.setText("PPN (" + pesanan.getTarifPpnPersen() + "%)   : " + FormatRupiah.format(pesanan.getNominalPpn()));
            rSum.addBreak();
            rSum.setText("PPh (" + pesanan.getTarifPphPersen() + "%)   : " + FormatRupiah.format(pesanan.getNominalPph()));
            rSum.addBreak();
            rSum.setBold(true);
            rSum.setText("TOTAL PEMBAYARAN : " + FormatRupiah.format(pesanan.getTotal()));
            rSum.addBreak();
            rSum.setItalic(true);
            rSum.setText("Terbilang: # " + pesanan.getTerbilang() + " #");

            // 5. TANDA TANGAN
            XWPFParagraph pTtd = doc.createParagraph();
            pTtd.setSpacingBefore(300);
            XWPFRun rTtd = pTtd.createRun();
            rTtd.setText("Pihak Pertama,                                                                     Pihak Kedua,");
            rTtd.addBreak();
            rTtd.setText(pesanan.getSekolah().getNamaSekolah() + "                                  " + DokumenService.NAMA_PERUSAHAAN);
            rTtd.addBreak();
            rTtd.addBreak();
            rTtd.addBreak();
            rTtd.setText("(" + pesanan.getSekolah().getNamaPenerima() + ")                                             (" + DokumenService.NAMA_DIREKTUR + ")");

            doc.write(fos);
        }
    }
}