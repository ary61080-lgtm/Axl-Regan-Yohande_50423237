package com.mycompany.cobamvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class Koneksi JDBC MySQL XAMPP
 * Database: db_pengadaan_sekolah
 * Package: com.mycompany.cobamvc
 */
public class KoneksiDB {
    private static final String URL = "jdbc:mysql://localhost:3306/db_pengadaan_sekolah?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL JDBC tidak ditemukan di pom.xml! " + e.getMessage());
            }
        }
        return connection;
    }
}
