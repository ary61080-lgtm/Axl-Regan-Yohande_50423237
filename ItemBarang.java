package com.pengadaan.salesorder.model;

import java.io.Serializable;

/**
 * Model untuk rincian item barang atau jasa pengadaan sekolah.
 */
public class ItemBarang implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaBarang;
    private int qty;
    private String satuan;
    private double hargaSatuan;
    private double jumlah;

    public ItemBarang() {
        this.qty = 1;
        this.satuan = "Unit";
    }

    public ItemBarang(String namaBarang, int qty, String satuan, double hargaSatuan) {
        this.namaBarang = namaBarang;
        this.qty = qty;
        this.satuan = satuan;
        this.hargaSatuan = hargaSatuan;
        this.jumlah = hitungJumlah();
    }

    public double hitungJumlah() {
        this.jumlah = this.qty * this.hargaSatuan;
        return this.jumlah;
    }

    // Getter dan Setter
    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }

    public int getQty() { return qty; }
    public void setQty(int qty) { 
        this.qty = qty; 
        hitungJumlah(); 
    }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(double hargaSatuan) { 
        this.hargaSatuan = hargaSatuan; 
        hitungJumlah(); 
    }

    public double getJumlah() { return hitungJumlah(); }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }
}