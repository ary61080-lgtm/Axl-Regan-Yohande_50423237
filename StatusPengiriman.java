package com.pengadaan.salesorder.model;

/**
 * Enum status pengiriman pesanan barang ke sekolah.
 */
public enum StatusPengiriman {
    DIPROSES("Diproses"),
    DIKIRIM("Dikirim"),
    SELESAI("Selesai");

    private final String label;

    StatusPengiriman(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static StatusPengiriman fromLabel(String label) {
        for (StatusPengiriman status : values()) {
            if (status.label.equalsIgnoreCase(label) || status.name().equalsIgnoreCase(label)) {
                return status;
            }
        }
        return DIPROSES;
    }
}