package com.pengadaan.salesorder.model;

import java.io.Serializable;

/**
 * Model data identitas sekolah pelanggan pengadaan barang/jasa.
 * Diinput langsung pada setiap transaksi sesuai ketentuan tanpa data master.
 */
public class Sekolah implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaSekolah;
    private String alamatSekolah;
    private String namaPenerima; // Kepala Sekolah atau Bendahara BOS
    private String nipPenerima;
    private String teleponSekolah;

    public Sekolah() {}

    public Sekolah(String namaSekolah, String alamatSekolah, String namaPenerima, String nipPenerima, String teleponSekolah) {
        this.namaSekolah = namaSekolah;
        this.alamatSekolah = alamatSekolah;
        this.namaPenerima = namaPenerima;
        this.nipPenerima = nipPenerima;
        this.teleponSekolah = teleponSekolah;
    }

    public String getNamaSekolah() { return namaSekolah; }
    public void setNamaSekolah(String namaSekolah) { this.namaSekolah = namaSekolah; }

    public String getAlamatSekolah() { return alamatSekolah; }
    public void setAlamatSekolah(String alamatSekolah) { this.alamatSekolah = alamatSekolah; }

    public String getNamaPenerima() { return namaPenerima; }
    public void setNamaPenerima(String namaPenerima) { this.namaPenerima = namaPenerima; }

    public String getNipPenerima() { return nipPenerima; }
    public void setNipPenerima(String nipPenerima) { this.nipPenerima = nipPenerima; }

    public String getTeleponSekolah() { return teleponSekolah; }
    public void setTeleponSekolah(String teleponSekolah) { this.teleponSekolah = teleponSekolah; }
}