package com.pengadaan.salesorder.controller;

import com.pengadaan.salesorder.model.Pengeluaran;
import com.pengadaan.salesorder.service.StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk mencatat pengeluaran operasional perusahaan.
 */
public class PengeluaranController {

    private final StorageService storageService;
    private List<Pengeluaran> daftarPengeluaran;

    public PengeluaranController(StorageService storageService) {
        this.storageService = storageService;
        muatData();
    }

    public void muatData() {
        try {
            this.daftarPengeluaran = storageService.muatPengeluaran();
        } catch (IOException e) {
            this.daftarPengeluaran = new ArrayList<>();
        }
    }

    public List<Pengeluaran> getSemuaPengeluaran() {
        return daftarPengeluaran;
    }

    public void tambahPengeluaran(Pengeluaran p) throws IOException {
        daftarPengeluaran.add(0, p);
        storageService.simpanPengeluaran(daftarPengeluaran);
    }

    public void updatePengeluaran(Pengeluaran updated) throws IOException {
        for (int i = 0; i < daftarPengeluaran.size(); i++) {
            if (daftarPengeluaran.get(i).getId().equals(updated.getId())) {
                daftarPengeluaran.set(i, updated);
                break;
            }
        }
        storageService.simpanPengeluaran(daftarPengeluaran);
    }

    public void hapusPengeluaran(String id) throws IOException {
        daftarPengeluaran.removeIf(p -> p.getId().equals(id));
        storageService.simpanPengeluaran(daftarPengeluaran);
    }

    public double hitungTotalPengeluaran() {
        double total = 0;
        if (daftarPengeluaran != null) {
            for (Pengeluaran p : daftarPengeluaran) {
                total += p.getNominal();
            }
        }
        return total;
    }
}