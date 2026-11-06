package com.mycompany.cobamvc;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity Class ORM Hibernate: Mapping ke Tabel 'pesanan'
 * Package: com.mycompany.cobamvc
 */
@Entity
@Table(name = "pesanan")
public class Pesanan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nomor_pesanan", nullable = false, length = 50)
    private String nomorPesanan;

    @Column(name = "nama_sekolah", nullable = false, length = 150)
    private String namaSekolah;

    @Temporal(TemporalType.DATE)
    @Column(name = "tanggal", nullable = false)
    private Date tanggal = new Date();

    @Column(name = "subtotal")
    private double subtotal;

    @Column(name = "tarif_ppn_persen")
    private double tarifPpnPersen = 11.0;

    @Column(name = "nominal_ppn")
    private double nominalPpn;

    @Column(name = "total_nilai")
    private double totalNilai;

    @Column(name = "status_pengiriman", length = 50)
    private String statusPengiriman = "DIPROSES";

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    public Pesanan() {
    }

    public Pesanan(String nomorPesanan, String namaSekolah, Date tanggal, double subtotal, double nominalPpn, double totalNilai, String statusPengiriman, String keterangan) {
        this.nomorPesanan = nomorPesanan;
        this.namaSekolah = namaSekolah;
        this.tanggal = tanggal;
        this.subtotal = subtotal;
        this.nominalPpn = nominalPpn;
        this.totalNilai = totalNilai;
        this.statusPengiriman = statusPengiriman;
        this.keterangan = keterangan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomorPesanan() { return nomorPesanan; }
    public void setNomorPesanan(String nomorPesanan) { this.nomorPesanan = nomorPesanan; }

    public String getNamaSekolah() { return namaSekolah; }
    public void setNamaSekolah(String namaSekolah) { this.namaSekolah = namaSekolah; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getTarifPpnPersen() { return tarifPpnPersen; }
    public void setTarifPpnPersen(double tarifPpnPersen) { this.tarifPpnPersen = tarifPpnPersen; }

    public double getNominalPpn() { return nominalPpn; }
    public void setNominalPpn(double nominalPpn) { this.nominalPpn = nominalPpn; }

    public double getTotalNilai() { return totalNilai; }
    public void setTotalNilai(double totalNilai) { this.totalNilai = totalNilai; }

    public String getStatusPengiriman() { return statusPengiriman; }
    public void setStatusPengiriman(String statusPengiriman) { this.statusPengiriman = statusPengiriman; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}
