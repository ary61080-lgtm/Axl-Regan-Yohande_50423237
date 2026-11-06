# ERP Sales Order Pengadaan Sekolah (Java Swing - JDK 17)

Aplikasi Desktop ERP Sales Order pengadaan barang dan jasa untuk instansi sekolah (Dana BOS / APBD) berbasis **Java Swing**, dikembangkan menggunakan **Apache NetBeans** dan **Maven**, dengan runtime **Java 17**.

---

## Fitur Utama
1. **Sales Order**: Input data sekolah, tabel dinamis item barang, auto-kalkulasi Subtotal, PPN (default 11%), PPh (default 1.5%), Total, dan Terbilang Bahasa Indonesia.
2. **Dokumen Siap Cetak**:
   - Surat Pesanan (PO)
   - Surat Perintah Kerja (SPK)
   - Kwitansi Pembayaran Resmi
   - Berita Acara Serah Terima (BAST)
   - Ekspor dalam 3 format: **PDF** (OpenPDF), **Word .docx** (Apache POI), **Excel .xlsx** (Apache POI).
3. **Riwayat Penjualan**: Filter tanggal, cari nama sekolah, klik baris untuk cetak ulang dokumen dan ubah status pengiriman.
4. **Biaya Operasional**: Form pencatatan pengeluaran logistik, tenaga kerja, packing, dan materai.
5. **Dashboard Pimpinan**: Real-time KPI omzet, biaya, margin %, laba bersih, dan counter status pesanan.
6. **Penyimpanan Lokal JSON**: Tanpa database MySQL/Postgres. Data tersimpan di folder `data/pesanan.json` dan `data/pengeluaran.json` menggunakan Google Gson.

---

## Cara Menjalankan di Apache NetBeans
1. Buka **Apache NetBeans (versi 17 ke atas)**.
2. Pastikan JDK 17 sudah terpilih di: `Tools > Java Platforms`.
3. Klik menu **File > Open Project...**.
4. Arahkan ke folder project ini (tempat file `pom.xml` berada). NetBeans akan mengenali ikon Maven Project secara otomatis.
5. Klik kanan pada project > **Clean and Build**. NetBeans akan otomatis mengunduh dependency:
   - `poi-ooxml` (Apache POI)
   - `openpdf` (OpenPDF)
   - `gson` (Google Gson)
   - `flatlaf` (FlatLaf Modern UI)
6. Klik tombol **Run Project (F6)**.
7. Aplikasi siap digunakan!
