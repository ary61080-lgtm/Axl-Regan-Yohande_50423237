package com.mycompany.cobamvc;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Main Class Proyek cobamvc
 * CV. AXL SARANA MANDIRI
 */
public class Main {

    public static void main(String[] args) {
        // Luncurkan LoginForm sebagai form pertama
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }

    public void bukaAplikasi() {
        JFrame mainFrame = new JFrame("Dashboard Sales Order Pengadaan Sekolah - CV. AXL SARANA MANDIRI");
        mainFrame.setSize(750, 480);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><center><h2>SELAMAT DATANG DI SISTEM SALES ORDER SEKOLAH</h2><p>CV. AXL SARANA MANDIRI - Direktur: Axl Regan Yohande</p><br><p style='color:green;'><b>Koneksi JDBC MySQL XAMPP Terhubung Sukses!</b></p></center></html>", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label, BorderLayout.CENTER);

        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }
}
