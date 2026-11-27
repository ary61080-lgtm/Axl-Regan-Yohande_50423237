package com.pengadaan.salesorder.service;

import com.pengadaan.salesorder.model.Pesanan;
import com.pengadaan.salesorder.util.DateUtil;
import com.pengadaan.salesorder.util.FormatRupiah;

/**
 * Layanan penyedia format teks resmi untuk 4 dokumen pengadaan:
 * 1. Surat Pesanan (PO)
 * 2. Surat Perintah Kerja (SPK)
 * 3. Kwitansi Pembayaran Resmi
 * 4. Berita Acara Serah Terima (BAST)
 */
public class DokumenService {

    public static final String NAMA_PERUSAHAAN = "CV. AXL SARANA MANDIRI";
    public static final String BIDANG_USAHA = "Pengadaan Barang dan Jasa Sarana Pendidikan";
    public static final String ALAMAT_PERUSAHAAN = "Jl. Percetakan Negara No. 45, Jakarta Pusat";
    public static final String TELEPON_PERUSAHAAN = "(021) 4248899 / 0812-9876-5432";
    public static final String EMAIL_PERUSAHAAN = "axl.sarana.mandiri@gmail.com";
    public static final String NPWP_PERUSAHAAN = "01.234.567.8-024.000";
    public static final String NAMA_DIREKTUR = "Axl Pratama, S.T.";
    public static final String JABATAN_DIREKTUR = "Direktur Utama";

    public static String getKopSuratTeks() {
        return NAMA_PERUSAHAAN + "\n" +
               BIDANG_USAHA + "\n" +
               ALAMAT_PERUSAHAAN + " | Telp: " + TELEPON_PERUSAHAAN + "\n" +
               "NPWP: " + NPWP_PERUSAHAAN + " | Email: " + EMAIL_PERUSAHAAN;
    }

    public static String generateTeksSpk(Pesanan p) {
        StringBuilder sb = new StringBuilder();
        sb.append("SURAT PERINTAH KERJA (SPK)\n");
        sb.append("Nomor: SPK/").append(p.getNomorPesanan()).append("\n\n");
        sb.append("Pada hari ini, tanggal ").append(DateUtil.formatKeIndonesia(p.getTanggalPesanan()));
        sb.append(", kami yang bertanda tangan di bawah ini:\n");
        sb.append("1. PIHAK KESATU (Sekolah)   : ").append(p.getSekolah().getNamaPenerima()).append(" (Kepala Sekolah)\n");
        sb.append("   Instansi                 : ").append(p.getSekolah().getNamaSekolah()).append("\n");
        sb.append("   Alamat                   : ").append(p.getSekolah().getAlamatSekolah()).append("\n\n");
        sb.append("2. PIHAK KEDUA (Penyedia)  : ").append(NAMA_DIREKTUR).append(" (").append(JABATAN_DIREKTUR).append(")\n");
        sb.append("   Perusahaan               : ").append(NAMA_PERUSAHAAN).append("\n");
        sb.append("   Alamat                   : ").append(ALAMAT_PERUSAHAAN).append("\n\n");
        sb.append("Kedua belah pihak sepakat mengadakan Perjanjian Pengadaan Barang dengan ketentuan:\n");
        sb.append("Pasal 1: PIHAK KEDUA wajib menyediakan barang sesuai rincian Surat Pesanan No. ").append(p.getNomorPesanan()).append(".\n");
        sb.append("Pasal 2: Total nilai pengadaan sebesar ").append(FormatRupiah.format(p.getTotal()));
        sb.append(" (").append(p.getTerbilang()).append(").\n");
        sb.append("Pasal 3: Barang diserahkan dalam keadaan baru, lengkap, dan bergaransi resmi.\n");
        sb.append("Pasal 4: Pembayaran dilakukan melalui transfer rekening Kas Sekolah Dana BOS.\n");
        return sb.toString();
    }

    public static String generateTeksBast(Pesanan p) {
        StringBuilder sb = new StringBuilder();
        sb.append("BERITA ACARA SERAH TERIMA BARANG (BAST)\n");
        sb.append("Nomor: BAST/").append(p.getNomorPesanan()).append("\n\n");
        sb.append("Pada hari ini, tanggal ").append(DateUtil.formatKeIndonesia(p.getTanggalPesanan()));
        sb.append(", telah dilakukan serah terima pekerjaan pengadaan barang antara:\n");
        sb.append("PIHAK KEDUA telah menyerahkan barang kepada PIHAK KESATU dalam jumlah dan spesifikasi lengkap sesuai pesanan.\n");
        sb.append("PIHAK KESATU telah memeriksa secara fisik dan menyatakan barang diterima dalam keadaan baik 100% dan berfungsi normal.\n\n");
        sb.append("Total Nilai Barang: ").append(FormatRupiah.format(p.getTotal())).append("\n");
        sb.append("Terbilang: ").append(p.getTerbilang()).append("\n");
        return sb.toString();
    }
}