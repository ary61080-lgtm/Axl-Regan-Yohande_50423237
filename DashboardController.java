package com.pengadaan.salesorder.controller;

import com.pengadaan.salesorder.model.Pesanan;
import com.pengadaan.salesorder.model.StatusPengiriman;
import java.util.List;

/**
 * Controller untuk menghitung metrik ringkasan pimpinan:
 * - Omzet kotor total penjualan
 * - Total biaya operasional
 * - Saldo bersih (Laba Bersih = Omzet - Biaya Operasional)
 * - Persentase margin profit
 * - Ringkasan jumlah transaksi per status pengiriman
 */
public class DashboardController {

    private final PesananController pesananController;
    private final PengeluaranController pengeluaranController;

    public DashboardController(PesananController pesananController, PengeluaranController pengeluaranController) {
        this.pesananController = pesananController;
        this.pengeluaranController = pengeluaranController;
    }

    public double hitungOmzet() {
        double omzet = 0;
        List<Pesanan> pesananList = pesananController.getSemuaPesanan();
        if (pesananList != null) {
            for (Pesanan p : pesananList) {
                omzet += p.getTotal();
            }
        }
        return omzet;
    }

    public double hitungTotalBiayaOperasional() {
        return pengeluaranController.hitungTotalPengeluaran();
    }

    public double hitungSaldoBersih() {
        return hitungOmzet() - hitungTotalBiayaOperasional();
    }

    public double hitungMarginProfitPersen() {
        double omzet = hitungOmzet();
        if (omzet == 0) return 0.0;
        double saldoBersih = hitungSaldoBersih();
        return (saldoBersih / omzet) * 100.0;
    }

    public int hitungPesananPerStatus(StatusPengiriman status) {
        int count = 0;
        List<Pesanan> list = pesananController.getSemuaPesanan();
        if (list != null) {
            for (Pesanan p : list) {
                if (p.getStatusPengiriman() == status) {
                    count++;
                }
            }
        }
        return count;
    }

    public int hitungTotalPesanan() {
        List<Pesanan> list = pesananController.getSemuaPesanan();
        return list != null ? list.size() : 0;
    }
}