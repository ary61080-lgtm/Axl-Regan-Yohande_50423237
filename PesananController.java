package com.pengadaan.salesorder.controller;

import com.pengadaan.salesorder.model.Pesanan;
import com.pengadaan.salesorder.model.StatusPengiriman;
import com.pengadaan.salesorder.service.StorageService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk mengelola alur transaksi Sales Order:
 * - Generate nomor pesanan otomatis
 * - Tambah & update transaksi
 * - Filter berdasarkan rentang tanggal dan pencarian nama sekolah
 * - Update status pengiriman
 */
public class PesananController {

    private final StorageService storageService;
    private List<Pesanan> daftarPesanan;

    public PesananController(StorageService storageService) {
        this.storageService = storageService;
        muatData();
    }

    public void muatData() {
        try {
            this.daftarPesanan = storageService.muatPesanan();
        } catch (IOException e) {
            this.daftarPesanan = new ArrayList<>();
        }
    }

    public List<Pesanan> getSemuaPesanan() {
        return daftarPesanan;
    }

    /**
     * Membuat nomor pesanan unik otomatis dengan format: SO-YYYYMMDD-XXX
     */
    public String generateNomorPesananOtomatis() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int urutan = 1;

        if (daftarPesanan != null) {
            for (Pesanan p : daftarPesanan) {
                if (p.getNomorPesanan() != null && p.getNomorPesanan().startsWith("SO-" + today)) {
                    urutan++;
                }
            }
        }
        return String.format("SO-%s-%03d", today, urutan);
    }

    public void simpanPesananBaru(Pesanan pesanan) throws IOException {
        pesanan.hitungTotalOtomatis();
        daftarPesanan.add(0, pesanan); // Taruh di urutan paling atas
        storageService.simpanPesanan(daftarPesanan);
    }

    public void updatePesanan(Pesanan pesanan) throws IOException {
        pesanan.hitungTotalOtomatis();
        for (int i = 0; i < daftarPesanan.size(); i++) {
            if (daftarPesanan.get(i).getNomorPesanan().equals(pesanan.getNomorPesanan())) {
                daftarPesanan.set(i, pesanan);
                break;
            }
        }
        storageService.simpanPesanan(daftarPesanan);
    }

    public void updateStatusPengiriman(String nomorPesanan, StatusPengiriman statusBaru) throws IOException {
        for (Pesanan p : daftarPesanan) {
            if (p.getNomorPesanan().equalsIgnoreCase(nomorPesanan)) {
                p.setStatusPengiriman(statusBaru);
                break;
            }
        }
        storageService.simpanPesanan(daftarPesanan);
    }

    /**
     * Filter transaksi berdasarkan rentang tanggal dan nama sekolah.
     */
    public List<Pesanan> filterPesanan(String tglMulai, String tglSelesai, String keywordSekolah) {
        List<Pesanan> hasil = new ArrayList<>();
        for (Pesanan p : daftarPesanan) {
            boolean lolosTanggal = true;
            if (tglMulai != null && !tglMulai.trim().isEmpty()) {
                lolosTanggal = p.getTanggalPesanan().compareTo(tglMulai.trim()) >= 0;
            }
            if (lolosTanggal && tglSelesai != null && !tglSelesai.trim().isEmpty()) {
                lolosTanggal = p.getTanggalPesanan().compareTo(tglSelesai.trim()) <= 0;
            }

            boolean lolosNama = true;
            if (keywordSekolah != null && !keywordSekolah.trim().isEmpty()) {
                String target = p.getSekolah().getNamaSekolah() != null ? p.getSekolah().getNamaSekolah().toLowerCase() : "";
                lolosNama = target.contains(keywordSekolah.trim().toLowerCase());
            }

            if (lolosTanggal && lolosNama) {
                hasil.add(p);
            }
        }
        return hasil;
    }
}