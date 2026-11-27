package com.pengadaan.salesorder.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pengadaan.salesorder.model.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Layanan penyimpanan lokal berbasis JSON menggunakan Google Gson.
 * Menyimpan data di folder "data" di samping aplikasi atau fallback ke user.home.
 */
public class StorageService {

    private final Gson gson;
    private final File filePesanan;
    private final File filePengeluaran;

    public StorageService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Cek folder data lokal atau user.home
        File dir = new File("data");
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                dir = new File(System.getProperty("user.home"), ".salesorder-sekolah");
                dir.mkdirs();
            }
        }

        this.filePesanan = new File(dir, "pesanan.json");
        this.filePengeluaran = new File(dir, "pengeluaran.json");

        inisialisasiDataAwalJikaKosong();
    }

    /**
     * Memuat seluruh daftar pesanan dari file JSON.
     */
    public synchronized List<Pesanan> muatPesanan() throws IOException {
        if (!filePesanan.exists() || filePesanan.length() == 0) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(filePesanan), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<ArrayList<Pesanan>>() {}.getType();
            List<Pesanan> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        }
    }

    /**
     * Menyimpan seluruh daftar pesanan ke file JSON.
     */
    public synchronized void simpanPesanan(List<Pesanan> pesananList) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePesanan), StandardCharsets.UTF_8)) {
            gson.toJson(pesananList, writer);
        }
    }

    /**
     * Memuat seluruh daftar pengeluaran operasional dari file JSON.
     */
    public synchronized List<Pengeluaran> muatPengeluaran() throws IOException {
        if (!filePengeluaran.exists() || filePengeluaran.length() == 0) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(filePengeluaran), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<ArrayList<Pengeluaran>>() {}.getType();
            List<Pengeluaran> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        }
    }

    /**
     * Menyimpan seluruh daftar pengeluaran operasional ke file JSON.
     */
    public synchronized void simpanPengeluaran(List<Pengeluaran> pengeluaranList) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePengeluaran), StandardCharsets.UTF_8)) {
            gson.toJson(pengeluaranList, writer);
        }
    }

    /**
     * Menyiapkan file JSON kosong awal jika file baru pertama kali dijalankan.
     * Sesuai ketentuan, data dimulai bersih dari nol.
     */
    private void inisialisasiDataAwalJikaKosong() {
        try {
            if (!filePesanan.exists() || filePesanan.length() == 0) {
                simpanPesanan(new ArrayList<>());
            }
            if (!filePengeluaran.exists() || filePengeluaran.length() == 0) {
                simpanPengeluaran(new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Gagal menginisialisasi file JSON awal: " + e.getMessage());
        }
    }
}