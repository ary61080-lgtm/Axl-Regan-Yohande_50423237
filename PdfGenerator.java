package com.pengadaan.salesorder.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.pengadaan.salesorder.model.ItemBarang;
import com.pengadaan.salesorder.model.Pesanan;
import com.pengadaan.salesorder.util.DateUtil;
import com.pengadaan.salesorder.util.FormatRupiah;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Generator Dokumen PDF Resmi (SPK, Surat Pesanan, Kwitansi, BAST)
 * menggunakan library OpenPDF (com.github.librepdf:openpdf).
 */
public class PdfGenerator {

    private static final Font FONT_HEADER_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
    private static final Font FONT_HEADER_SUB = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);
    private static final Font FONT_DOC_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
    private static final Font FONT_BODY = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
    private static final Font FONT_BODY_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);
    private static final Font FONT_TERBILANG = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, new Color(50, 50, 50));

    /**
     * Membuat file PDF sesuai jenis dokumen yang dipilih.
     * @param jenisDokumen: "SPK", "SURAT_PESANAN", "KWITANSI", "BAST"
     */
    public static void generateDokumen(Pesanan pesanan, String jenisDokumen, File fileTarget) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, new FileOutputStream(fileTarget));
        document.open();

        // 1. KOP SURAT PERUSAHAAN
        tambahKopSurat(document);

        // 2. JUDUL DOKUMEN & NOMOR
        Paragraph pJudul = new Paragraph();
        pJudul.setAlignment(Element.ALIGN_CENTER);
        pJudul.setSpacingBefore(10);
        pJudul.setSpacingAfter(15);

        String judulTeks = "SURAT PESANAN (PURCHASE ORDER)";
        String prefixNo = "PO";
        if ("SPK".equalsIgnoreCase(jenisDokumen)) {
            judulTeks = "SURAT PERINTAH KERJA (SPK)";
            prefixNo = "SPK";
        } else if ("KWITANSI".equalsIgnoreCase(jenisDokumen)) {
            judulTeks = "KWITANSI PEMBAYARAN RESMI";
            prefixNo = "KWT";
        } else if ("BAST".equalsIgnoreCase(jenisDokumen)) {
            judulTeks = "BERITA ACARA SERAH TERIMA BARANG (BAST)";
            prefixNo = "BAST";
        }

        Chunk cTitle = new Chunk(judulTeks, FONT_DOC_TITLE);
        cTitle.setUnderline(1.0f, -2f);
        pJudul.add(cTitle);
        pJudul.add(new Paragraph("Nomor: " + prefixNo + "/" + pesanan.getNomorPesanan(), FONT_BODY_BOLD));
        document.add(pJudul);

        // 3. IDENTITAS SEKOLAH & TANGGAL
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{60, 40});

        PdfPCell cSekolah = new PdfPCell();
        cSekolah.setBorder(Rectangle.NO_BORDER);
        cSolahParagraph(cSekolah, "Kepada Yth:", FONT_BODY_BOLD);
        cSolahParagraph(cSekolah, pesanan.getSekolah().getNamaSekolah(), FONT_BODY_BOLD);
        cSolahParagraph(cSekolah, pesanan.getSekolah().getAlamatSekolah(), FONT_BODY);
        cSolahParagraph(cSekolah, "U.p: " + pesanan.getSekolah().getNamaPenerima(), FONT_BODY);
        cSolahParagraph(cSekolah, "Telp: " + (pesanan.getSekolah().getTeleponSekolah() != null ? pesanan.getSekolah().getTeleponSekolah() : "-"), FONT_BODY);
        infoTable.addCell(cSekolah);

        PdfPCell cTanggal = new PdfPCell();
        cTanggal.setBorder(Rectangle.NO_BORDER);
        cSolahParagraph(cTanggal, "Tanggal Pesanan : " + DateUtil.formatKeIndonesia(pesanan.getTanggalPesanan()), FONT_BODY);
        cSolahParagraph(cTanggal, "Status          : " + pesanan.getStatusPengiriman().getLabel(), FONT_BODY);
        cSolahParagraph(cTanggal, "Sumber Anggaran : Dana BOS / APBD Sekolah", FONT_BODY);
        infoTable.addCell(cTanggal);

        document.add(infoTable);
        document.add(new Paragraph(" "));

        // 4. KONTEN KHUSUS KWITANSI ATAU TABEL BARANG
        if ("KWITANSI".equalsIgnoreCase(jenisDokumen)) {
            tambahKontenKwitansi(document, pesanan);
        } else {
            tambahTabelBarang(document, pesanan);
        }

        // 5. TANDA TANGAN DUA BELAH PIHAK
        tambahBlokTandaTangan(document, pesanan);

        document.close();
    }

    private static void cSolahParagraph(PdfPCell cell, String teks, Font font) {
        cell.addElement(new Paragraph(teks, font));
    }

    private static void tambahKopSurat(Document doc) throws DocumentException {
        PdfPTable kop = new PdfPTable(1);
        kop.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph pNama = new Paragraph(DokumenService.NAMA_PERUSAHAAN, FONT_HEADER_TITLE);
        pNama.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pNama);

        Paragraph pBidang = new Paragraph(DokumenService.BIDANG_USAHA, FONT_HEADER_SUB);
        pBidang.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pBidang);

        Paragraph pAlamat = new Paragraph(DokumenService.ALAMAT_PERUSAHAAN + " | Telp: " + DokumenService.TELEPON_PERUSAHAAN +
                "\nNPWP: " + DokumenService.NPWP_PERUSAHAAN + " | Email: " + DokumenService.EMAIL_PERUSAHAAN, FONT_HEADER_SUB);
        pAlamat.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pAlamat);

        kop.addCell(cell);
        doc.add(kop);

        // Garis Pembatas Kop
        Paragraph line = new Paragraph();
        line.setSpacingBefore(4);
        line.setSpacingAfter(4);
        Chunk cLine = new Chunk("________________________________________________________________________________");
        cLine.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK));
        line.add(cLine);
        line.setAlignment(Element.ALIGN_CENTER);
        doc.add(line);
    }

    private static void tambahTabelBarang(Document doc, Pesanan pesanan) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{8, 45, 12, 17, 18});

        // Header Tabel
        String[] headers = {"No", "Nama Barang / Spesifikasi", "Kuantitas", "Harga Satuan", "Jumlah (Rp)"};
        for (String h : headers) {
            PdfPCell c = new PdfPCell(new Phrase(h, FONT_BODY_BOLD));
            c.setBackgroundColor(new Color(240, 240, 240));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setPadding(5);
            table.addCell(c);
        }

        // Baris Item
        int no = 1;
        for (ItemBarang item : pesanan.getItems()) {
            PdfPCell cNo = new PdfPCell(new Phrase(String.valueOf(no++), FONT_BODY));
            cNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            cNo.setPadding(4);
            table.addCell(cNo);

            PdfPCell cNama = new PdfPCell(new Phrase(item.getNamaBarang(), FONT_BODY));
            cNama.setPadding(4);
            table.addCell(cNama);

            PdfPCell cQty = new PdfPCell(new Phrase(item.getQty() + " " + item.getSatuan(), FONT_BODY));
            cQty.setHorizontalAlignment(Element.ALIGN_CENTER);
            cQty.setPadding(4);
            table.addCell(cQty);

            PdfPCell cHarga = new PdfPCell(new Phrase(FormatRupiah.formatAngka(item.getHargaSatuan()), FONT_BODY));
            cHarga.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cHarga.setPadding(4);
            table.addCell(cHarga);

            PdfPCell cJumlah = new PdfPCell(new Phrase(FormatRupiah.formatAngka(item.getJumlah()), FONT_BODY));
            cJumlah.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cJumlah.setPadding(4);
            table.addCell(cJumlah);
        }

        // Subtotal
        PdfPCell cSubLabel = new PdfPCell(new Phrase("Subtotal :", FONT_BODY_BOLD));
        cSubLabel.setColspan(4);
        cSubLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cSubLabel.setPadding(4);
        table.addCell(cSubLabel);

        PdfPCell cSubVal = new PdfPCell(new Phrase(FormatRupiah.format(pesanan.getSubtotal()), FONT_BODY_BOLD));
        cSubVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cSubVal.setPadding(4);
        table.addCell(cSubVal);

        // PPN
        PdfPCell cPpnLabel = new PdfPCell(new Phrase("PPN (" + pesanan.getTarifPpnPersen() + "%) :", FONT_BODY));
        cPpnLabel.setColspan(4);
        cPpnLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cPpnLabel.setPadding(4);
        table.addCell(cPpnLabel);

        PdfPCell cPpnVal = new PdfPCell(new Phrase(FormatRupiah.format(pesanan.getNominalPpn()), FONT_BODY));
        cPpnVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cPpnVal.setPadding(4);
        table.addCell(cPpnVal);

        // PPh (Info)
        PdfPCell cPphLabel = new PdfPCell(new Phrase("PPh (" + pesanan.getTarifPphPersen() + "%) :", FONT_BODY));
        cPphLabel.setColspan(4);
        cPphLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cPphLabel.setPadding(4);
        table.addCell(cPphLabel);

        PdfPCell cPphVal = new PdfPCell(new Phrase(FormatRupiah.format(pesanan.getNominalPph()), FONT_BODY));
        cPphVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cPphVal.setPadding(4);
        table.addCell(cPphVal);

        // Total Akhir
        PdfPCell cTotLabel = new PdfPCell(new Phrase("TOTAL KESELURUHAN :", FONT_BODY_BOLD));
        cTotLabel.setColspan(4);
        cTotLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cTotLabel.setBackgroundColor(new Color(230, 240, 255));
        cTotLabel.setPadding(5);
        table.addCell(cTotLabel);

        PdfPCell cTotVal = new PdfPCell(new Phrase(FormatRupiah.format(pesanan.getTotal()), FONT_BODY_BOLD));
        cTotVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cTotVal.setBackgroundColor(new Color(230, 240, 255));
        cTotVal.setPadding(5);
        table.addCell(cTotVal);

        doc.add(table);

        // Terbilang
        Paragraph pTerbilang = new Paragraph();
        pTerbilang.setSpacingBefore(6);
        pTerbilang.setSpacingAfter(10);
        pTerbilang.add(new Phrase("Terbilang: # ", FONT_BODY_BOLD));
        pTerbilang.add(new Phrase(pesanan.getTerbilang(), FONT_TERBILANG));
        pTerbilang.add(new Phrase(" #", FONT_BODY_BOLD));
        doc.add(pTerbilang);
    }

    private static void tambahKontenKwitansi(Document doc, Pesanan pesanan) throws DocumentException {
        PdfPTable box = new PdfPTable(2);
        box.setWidthPercentage(100);
        box.setWidths(new float[]{25, 75});

        String[][] baris = {
            {"Sudah Terima Dari", ": " + pesanan.getSekolah().getNamaSekolah()},
            {"Banyaknya Uang", ": " + pesanan.getTerbilang()},
            {"Untuk Pembayaran", ": Pengadaan Barang/Jasa Sarana Pendidikan sesuai PO No. " + pesanan.getNomorPesanan()},
            {"Jumlah Pembayaran", ": " + FormatRupiah.format(pesanan.getTotal())}
        };

        for (String[] r : baris) {
            PdfPCell c1 = new PdfPCell(new Phrase(r[0], FONT_BODY_BOLD));
            c1.setBorder(Rectangle.NO_BORDER);
            c1.setPadding(6);
            box.addCell(c1);

            PdfPCell c2 = new PdfPCell(new Phrase(r[1], FONT_BODY));
            c2.setBorder(Rectangle.NO_BORDER);
            c2.setPadding(6);
            box.addCell(c2);
        }

        doc.add(box);
    }

    private static void tambahBlokTandaTangan(Document doc, Pesanan pesanan) throws DocumentException {
        Paragraph sp = new Paragraph(" ");
        sp.setSpacingBefore(15);
        doc.add(sp);

        PdfPTable ttd = new PdfPTable(2);
        ttd.setWidthPercentage(100);
        ttd.setWidths(new float[]{50, 50});

        // Kolom Kiri: Sekolah
        PdfPCell cKiri = new PdfPCell();
        cKiri.setBorder(Rectangle.NO_BORDER);
        cKiri.setHorizontalAlignment(Element.ALIGN_CENTER);
        cSolahParagraph(cKiri, "PIHAK KESATU\n" + pesanan.getSekolah().getNamaSekolah(), FONT_BODY_BOLD);
        cSolahParagraph(cKiri, "\n\n\n\n", FONT_BODY);
        cSolahParagraph(cKiri, "(" + pesanan.getSekolah().getNamaPenerima() + ")", FONT_BODY_BOLD);
        cSolahParagraph(cKiri, "NIP: " + (pesanan.getSekolah().getNipPenerima() != null ? pesanan.getSekolah().getNipPenerima() : "-"), FONT_BODY);
        ttd.addCell(cKiri);

        // Kolom Kanan: Perusahaan
        PdfPCell cKanan = new PdfPCell();
        cKanan.setBorder(Rectangle.NO_BORDER);
        cKanan.setHorizontalAlignment(Element.ALIGN_CENTER);
        cSolahParagraph(cKanan, "PIHAK KEDUA\n" + DokumenService.NAMA_PERUSAHAAN, FONT_BODY_BOLD);
        cSolahParagraph(cKanan, "\n\n\n\n", FONT_BODY);
        cSolahParagraph(cKanan, "(" + DokumenService.NAMA_DIREKTUR + ")", FONT_BODY_BOLD);
        cSolahParagraph(cKanan, DokumenService.JABATAN_DIREKTUR, FONT_BODY);
        ttd.addCell(cKanan);

        doc.add(ttd);
    }
}