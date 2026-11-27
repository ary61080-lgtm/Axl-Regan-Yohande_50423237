package com.pengadaan.salesorder.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Model untuk pengeluaran operasional pengadaan (logistik, tenaga kerja, packing, dll).
 */
public class Pengeluaran implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String tanggal; // YYYY-MM-DD
    private String keterangan;
    private double nominal;
    private String kategori;

    public Pengeluaran() {
        this.id = UUID.randomUUID().toString();
    }

    public Pengeluaran(String tanggal, String keterangan, double nominal, String kategori) {
        this.id = UUID.randomUUID().toString();
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.nominal = nominal;
        this.kategori = kategori;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public double getNominal() { return nominal; }
    public void setNominal(double nominal) { this.nominal = nominal; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
}