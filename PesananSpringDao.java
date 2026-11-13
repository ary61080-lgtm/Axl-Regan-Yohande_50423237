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
    }

    // 2. AMBIL SEMUA DATA (SELECT + RowMapper)
    public List<Pesanan> getAll() {
        String sql = "SELECT * FROM pesanan ORDER BY id DESC";
        return jdbcTemplate.query(sql, new RowMapper<Pesanan>() {
            @Override
            public Pesanan mapRow(ResultSet rs, int rowNum) throws SQLException {
                Pesanan p = new Pesanan();
                p.setId(rs.getInt("id"));
                p.setNomorPesanan(rs.getString("nomor_pesanan"));
                p.setNamaSekolah(rs.getString("nama_sekolah"));
                p.setTanggal(rs.getDate("tanggal"));
                p.setSubtotal(rs.getDouble("subtotal"));
                p.setTarifPpnPersen(rs.getDouble("tarif_ppn_persen"));
                p.setNominalPpn(rs.getDouble("nominal_ppn"));
                p.setTotalNilai(rs.getDouble("total_nilai"));
                p.setStatusPengiriman(rs.getString("status_pengiriman"));
                p.setKeterangan(rs.getString("keterangan"));
                return p;
            }
        });
    }

    // 3. HAPUS DATA (DELETE)
    public int hapus(int id) {
        String sql = "DELETE FROM pesanan WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
