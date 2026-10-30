-- ============================================================================
-- DATABASE SALES ORDER PENGADAAN SEKOLAH (DANA BOS)
-- PERUSAHAAN : CV. AXL SARANA MANDIRI
-- DIREKTUR   : AXL REGAN YOHANDE
-- PLATFORM   : XAMPP MySQL / MariaDB (phpMyAdmin)
-- PORT       : 3306 (Default XAMPP)
-- USER       : root
-- PASSWORD   : (kosong / empty string)
-- ============================================================================

CREATE DATABASE IF NOT EXISTS `db_pengadaan_sekolah` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `db_pengadaan_sekolah`;

-- 1. TABEL PESANAN (HEADER TRANSAKSI SALES ORDER)
DROP TABLE IF EXISTS `item_pesanan`;
DROP TABLE IF EXISTS `pesanan`;

CREATE TABLE `pesanan` (
  `id` VARCHAR(64) NOT NULL,
  `nomor_pesanan` VARCHAR(50) NOT NULL,
  `tanggal_pesanan` DATE NOT NULL,
  `keterangan` TEXT DEFAULT NULL,
  `nama_sekolah` VARCHAR(150) NOT NULL,
  `alamat_sekolah` TEXT DEFAULT NULL,
  `nama_penerima` VARCHAR(100) DEFAULT NULL,
  `nip_penerima` VARCHAR(50) DEFAULT NULL,
  `telepon_sekolah` VARCHAR(30) DEFAULT NULL,
  `subtotal` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `tarif_ppn_persen` DECIMAL(5,2) NOT NULL DEFAULT 11.00,
  `nominal_ppn` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `tarif_pph_persen` DECIMAL(5,2) NOT NULL DEFAULT 1.50,
  `nominal_pph` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `total` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `terbilang` TEXT DEFAULT NULL,
  `status_pengiriman` ENUM('Diproses', 'Dikirim', 'Selesai') NOT NULL DEFAULT 'Diproses',
  `catatan` TEXT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nomor_pesanan`),
  INDEX (`tanggal_pesanan`),
  INDEX (`status_pengiriman`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. TABEL ITEM PESANAN (RINCIAN BARANG/JASA)
CREATE TABLE `item_pesanan` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `nomor_pesanan` VARCHAR(50) NOT NULL,
  `nama_barang` VARCHAR(255) NOT NULL,
  `qty` INT NOT NULL DEFAULT 1,
  `satuan` VARCHAR(50) NOT NULL DEFAULT 'Unit',
  `harga_satuan` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `jumlah` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  INDEX (`nomor_pesanan`),
  CONSTRAINT `fk_item_pesanan` FOREIGN KEY (`nomor_pesanan`) REFERENCES `pesanan` (`nomor_pesanan`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. TABEL PENGELUARAN (BIAYA OPERASIONAL & LOGISTIK)
DROP TABLE IF EXISTS `pengeluaran`;

CREATE TABLE `pengeluaran` (
  `id` VARCHAR(64) NOT NULL PRIMARY KEY,
  `kode_transaksi` VARCHAR(50) DEFAULT NULL,
  `tanggal` DATE NOT NULL,
  `keterangan` TEXT NOT NULL,
  `nominal` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `kategori` VARCHAR(100) NOT NULL DEFAULT 'Operasional',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- SEED DATA AWAL (CONTOH DATA TRANSAKSI RESMI)
-- ============================================================================
INSERT INTO `pesanan` 
(`id`, `nomor_pesanan`, `tanggal_pesanan`, `keterangan`, `nama_sekolah`, `alamat_sekolah`, `nama_penerima`, `nip_penerima`, `telepon_sekolah`, `subtotal`, `tarif_ppn_persen`, `nominal_ppn`, `tarif_pph_persen`, `nominal_pph`, `total`, `terbilang`, `status_pengiriman`, `catatan`)
VALUES
('SO-2026-001', 'SO-2026-001', '2026-05-10', 'Pengadaan Lab Komputer Siswa Dana BOS Tahap I', 'SMKN 1 Jakarta', 'Jl. Budi Utomo No. 7, Jakarta Pusat', 'Drs. H. Mulyadi, M.Pd', '196805121994031004', '0812-3456-7890', 126500000.00, 11.00, 13915000.00, 1.50, 1897500.00, 140415000.00, 'Seratus Empat Puluh Juta Empat Ratus Lima Belas Ribu Rupiah', 'Selesai', 'Pengadaan resmi dana BOS terverifikasi'),
('SO-2026-002', 'SO-2026-002', '2026-05-14', 'Pengadaan Buku Pelajaran Kurikulum Merdeka Fase E', 'SMAN 3 Jakarta', 'Jl. Setiabudi II, Jakarta Selatan', 'Dra. Hj. Ratna Juwita, M.M', '197103251998022001', '0813-8877-6655', 48500000.00, 11.00, 5335000.00, 1.50, 727500.00, 53835000.00, 'Lima Puluh Tiga Juta Delapan Ratus Tiga Puluh Lima Ribu Rupiah', 'Dikirim', 'Armada kurir sedang menuju lokasi sekolah');

INSERT INTO `item_pesanan` (`nomor_pesanan`, `nama_barang`, `qty`, `satuan`, `harga_satuan`, `jumlah`) VALUES
('SO-2026-001', 'Laptop Chromebook ASUS C214 Intel N4020 4GB/32GB', 15, 'Unit', 6500000.00, 97500000.00),
('SO-2026-001', 'Proyektor Epson EB-X500 3600 Lumens XGA HDMI', 5, 'Unit', 5800000.00, 29000000.00),
('SO-2026-002', 'Paket Buku Tematik Kurikulum Merdeka Kelas X Lengkap', 200, 'Paket', 242500.00, 48500000.00);

INSERT INTO `pengeluaran` (`id`, `kode_transaksi`, `tanggal`, `keterangan`, `nominal`, `kategori`) VALUES
('EXP-001', 'TRX-EXP-001', '2026-05-11', 'Ongkos Angkut Truk Ekspedisi Pengiriman ke SMKN 1 Jakarta', 1500000.00, 'Logistik & Transport'),
('EXP-002', 'TRX-EXP-002', '2026-05-12', 'Biaya Materai 10000 & Legalisir Dokumen SPK BAST 5 Rangkap', 250000.00, 'Administrasi & Operasional'),
('EXP-003', 'TRX-EXP-003', '2026-05-15', 'Jasa Packing Kayu & Bubble Wrap Perangkat Elektronik Lab', 850000.00, 'Packaging & Keamanan');
