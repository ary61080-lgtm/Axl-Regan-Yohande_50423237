package com.mycompany.cobamvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Spring DAO (Data Access Object) menggunakan JdbcTemplate
 * Package: com.mycompany.cobamvc
 * Database: db_pengadaan_sekolah
 */
public class PesananSpringDao {

    private JdbcTemplate jdbcTemplate;

    // Setter Injection oleh Spring applicationContext.xml
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. SIMPAN DATA (INSERT)
    public int simpan(Pesanan p) {
        try {
            String sql = "INSERT INTO pesanan (nomor_pesanan, nama_sekolah, tanggal, subtotal, tarif_ppn_persen, nominal_ppn, total_nilai, status_pengiriman, keterangan) "
                       + "VALUES (?, ?, CURDATE(), ?, ?, ?, ?, ?, ?)";
            return jdbcTemplate.update(sql,
                    p.getNomorPesanan(),
                    p.getNamaSekolah(),
                    p.getSubtotal(),
                    p.getTarifPpnPersen(),
                    p.getNominalPpn(),
                    p.getTotalNilai(),
                    p.getStatusPengiriman(),
                    p.getKeterangan());
        } catch (Exception e) {
            try {
                // Fallback ke skema tabel sederhana dari Main.java
                String sqlSimple = "INSERT INTO pesanan (nomor_pesanan, nama_sekolah, tanggal, total_nilai, status_pengiriman) "
                                 + "VALUES (?, ?, CURDATE(), ?, ?)";
                return jdbcTemplate.update(sqlSimple,
                        p.getNomorPesanan(),
                        p.getNamaSekolah(),
                        p.getTotalNilai(),
                        p.getStatusPengiriman());
            } catch (Exception ex) {
                // Fallback ke skema tabel lengkap dari database_pengadaan_sekolah.sql
                String sqlResmi = "INSERT INTO pesanan (id, nomor_pesanan, tanggal_pesanan, keterangan, nama_sekolah, subtotal, tarif_ppn_persen, nominal_ppn, total, status_pengiriman) "
                                + "VALUES (?, ?, CURDATE(), ?, ?, ?, ?, ?, ?, ?)";
                return jdbcTemplate.update(sqlResmi,
                        p.getNomorPesanan(),
                        p.getNomorPesanan(),
                        p.getKeterangan(),
                        p.getNamaSekolah(),
                        p.getSubtotal(),
                        p.getTarifPpnPersen(),
                        p.getNominalPpn(),
                        p.getTotalNilai(),
                        p.getStatusPengiriman());
            }
        }
    }

    // 2. AMBIL SEMUA DATA (SELECT + RowMapper)
    public List<Pesanan> getAll() {
        String sql = "SELECT * FROM pesanan ORDER BY id DESC";
        return jdbcTemplate.query(sql, new RowMapper<Pesanan>() {
            @Override
            public Pesanan mapRow(ResultSet rs, int rowNum) throws SQLException {
                Pesanan p = new Pesanan();
                try { p.setId(rs.getInt("id")); } catch (Exception ignored) {}
                p.setNomorPesanan(rs.getString("nomor_pesanan"));
                p.setNamaSekolah(rs.getString("nama_sekolah"));
                try { p.setTanggal(rs.getDate("tanggal")); } catch (Exception e) {
                    try { p.setTanggal(rs.getDate("tanggal_pesanan")); } catch (Exception ignored) {}
                }
                try { p.setSubtotal(rs.getDouble("subtotal")); } catch (Exception ignored) {}
                try { p.setTarifPpnPersen(rs.getDouble("tarif_ppn_persen")); } catch (Exception ignored) {}
                try { p.setNominalPpn(rs.getDouble("nominal_ppn")); } catch (Exception ignored) {}
                try { p.setTotalNilai(rs.getDouble("total_nilai")); } catch (Exception e) {
                    try { p.setTotalNilai(rs.getDouble("total")); } catch (Exception ignored) {}
                }
                try { p.setStatusPengiriman(rs.getString("status_pengiriman")); } catch (Exception ignored) {}
                try { p.setKeterangan(rs.getString("keterangan")); } catch (Exception ignored) {}
                return p;
            }
        });
    }

    // 3. HAPUS DATA (DELETE)
    public int hapus(String nomorPesanan) {
        String sql = "DELETE FROM pesanan WHERE nomor_pesanan = ?";
        return jdbcTemplate.update(sql, nomorPesanan);
    }

    // 4. METHOD MAIN UNTUK LANGSUNG RUN FILE (SHIFT + F6) DI NETBEANS
    public static void main(String[] args) {
        System.out.println("=== MENJALANKAN SPRING DAO (JdbcTemplate) ===");
        org.springframework.context.ApplicationContext context =
            new org.springframework.context.support.ClassPathXmlApplicationContext("applicationContext.xml");

        PesananSpringDao dao = context.getBean("pesananSpringDao", PesananSpringDao.class);
        System.out.println("Bean Spring DAO berhasil dimuat dari applicationContext.xml!");

        List<Pesanan> daftar = dao.getAll();
        System.out.println("Total data di MySQL (db_pengadaan_sekolah): " + daftar.size());
        for (Pesanan item : daftar) {
            System.out.println(" - " + item.getNomorPesanan() + " | " + item.getNamaSekolah()
                    + " | Rp " + String.format("%,.0f", item.getTotalNilai())
                    + " | " + item.getStatusPengiriman());
        }
    }
}
