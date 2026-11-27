package com.pengadaan.salesorder;

import com.formdev.flatlaf.FlatLightLaf;
import com.pengadaan.salesorder.view.MainFrame;

import javax.swing.*;

/**
 * Titik Masuk Utama (Main Class) Aplikasi Desktop Sales Order Pengadaan Sekolah.
 * Menginisialisasi FlatLaf Look and Feel modern dan menampilkan MainFrame.
 */
public class App {

    public static void main(String[] args) {
        // Pasang FlatLaf Look and Feel agar UI Swing tampak modern & profesional
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 6);
            UIManager.put("Component.arc", 6);
            UIManager.put("TextComponent.arc", 6);
        } catch (Exception e) {
            System.err.println("Gagal menerapkan FlatLaf Look and Feel: " + e.getMessage());
        }

        // Jalankan UI di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Terjadi kesalahan saat memulai aplikasi:\n" + e.getMessage(),
                        "Kesalahan Sistem",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}