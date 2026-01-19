package com.rental.dao;

import com.rental.config.DatabaseConnection;
import com.rental.model.Sewa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SewaDAO {

    // 1. TAMBAH SEWA BARU (RentServlet)
    public boolean addSewa(int idKendaraan, int idUser, String tglSewaStr, int durasiHari, double hargaPerHari) {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); 
            
            // Hitung Tanggal Kembali
            java.sql.Date tglSewa = java.sql.Date.valueOf(tglSewaStr);
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(tglSewa);
            c.add(java.util.Calendar.DATE, durasiHari);
            java.sql.Date tglKembali = new java.sql.Date(c.getTimeInMillis());
            
            double totalHarga = hargaPerHari * durasiHari;

            // Insert ke tabel sewa (Default denda = 0)
            String sql = "INSERT INTO sewa (id_kendaraan, id_user, tgl_sewa, tgl_pengembalian, ttl_harga, stts, denda) VALUES (?, ?, ?, ?, ?, 'aktif', 0)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idKendaraan);
            ps.setInt(2, idUser);
            ps.setDate(3, tglSewa);
            ps.setDate(4, tglKembali);
            ps.setDouble(5, totalHarga);
            ps.executeUpdate();
            
            // Update Status Kendaraan
            String sqlUpdate = "UPDATE kendaraan SET status = 'disewa' WHERE id_kendaraan = ?";
            PreparedStatement psUp = con.prepareStatement(sqlUpdate);
            psUp.setInt(1, idKendaraan);
            psUp.executeUpdate();
            
            con.commit();
            return true;
        } catch (Exception e) {
            try { if(con!=null) con.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        }
    }

    // 2. AMBIL HISTORY USER (History.jsp)
    public List<Sewa> getHistoryByUser(int idUser) {
        List<Sewa> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT s.id_sewa, k.merk, k.model, k.no_plat, s.tgl_sewa, s.tgl_pengembalian, s.ttl_harga, s.stts, s.denda " +
                         "FROM sewa s " +
                         "JOIN kendaraan k ON s.id_kendaraan = k.id_kendaraan " +
                         "WHERE s.id_user = ? " + 
                         "ORDER BY s.id_sewa DESC";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                String info = rs.getString("merk") + " " + rs.getString("model");
                Sewa s = new Sewa(
                    rs.getInt("id_sewa"), "-", info, rs.getString("no_plat"),
                    rs.getDate("tgl_pengembalian"), rs.getDouble("ttl_harga"), rs.getString("stts")
                );
                s.setTglSewa(rs.getDate("tgl_sewa"));
                s.setDenda(rs.getDouble("denda")); // Ambil Denda
                list.add(s);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. AMBIL SEWA AKTIF (Admin Dashboard)
    public List<Sewa> getActiveSewa() {
        List<Sewa> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT s.id_sewa, u.nama_lengkap, k.merk, k.model, k.no_plat, s.tgl_pengembalian, s.ttl_harga, s.stts " +
                         "FROM sewa s " +
                         "JOIN users u ON s.id_user = u.id " +
                         "JOIN kendaraan k ON s.id_kendaraan = k.id_kendaraan " +
                         "WHERE s.stts = 'aktif' ORDER BY s.tgl_pengembalian ASC";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                String info = rs.getString("merk") + " " + rs.getString("model");
                Sewa s = new Sewa(
                    rs.getInt("id_sewa"),
                    rs.getString("nama_lengkap"), 
                    info,
                    rs.getString("no_plat"),
                    rs.getDate("tgl_pengembalian"),
                    rs.getDouble("ttl_harga"),
                    rs.getString("stts")
                );
                // Denda belum ada karena masih aktif
                list.add(s);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // 4. PROCESS RETURN (DENDA DINAMIS - PILIHANMU) 🔥
    public double processReturn(int idSewa, String tglKembaliRealStr) {
        double denda = 0;
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);
            
            // JOIN ke kendaraan untuk ambil HARGA SEWA PER HARI (Logic Dinamis)
            String sqlCek = "SELECT s.tgl_pengembalian, s.id_kendaraan, s.ttl_harga, k.harga_sewa_per_hari " +
                            "FROM sewa s " +
                            "JOIN kendaraan k ON s.id_kendaraan = k.id_kendaraan " +
                            "WHERE s.id_sewa = ?";
            
            PreparedStatement psCek = con.prepareStatement(sqlCek);
            psCek.setInt(1, idSewa);
            ResultSet rs = psCek.executeQuery();
            
            if(rs.next()) {
                Date tglRencana = rs.getDate("tgl_pengembalian");
                int idKendaraan = rs.getInt("id_kendaraan");
                double hargaAwal = rs.getDouble("ttl_harga");
                double hargaPerHari = rs.getDouble("harga_sewa_per_hari"); // Ambil Harga Asli Kendaraan
                
                // Hitung Selisih Hari
                Date tglReal = Date.valueOf(tglKembaliRealStr);
                long diff = tglReal.getTime() - tglRencana.getTime();
                long hariTelat = java.util.concurrent.TimeUnit.DAYS.convert(diff, java.util.concurrent.TimeUnit.MILLISECONDS);
                
                if(hariTelat > 0) {
                    // RUMUS ADIL: Denda = Hari Telat x Harga Sewa Mobilnya
                    denda = hariTelat * hargaPerHari; 
                }
                
                // Update Total Harga (Harga Awal + Denda)
                double totalBaru = hargaAwal + denda;

                // Update Tabel Sewa
                String sqlUp = "UPDATE sewa SET stts = 'selesai', tgl_pengembalian = ?, denda = ?, ttl_harga = ? WHERE id_sewa = ?";
                PreparedStatement psUp = con.prepareStatement(sqlUp);
                psUp.setDate(1, tglReal);
                psUp.setDouble(2, denda);
                psUp.setDouble(3, totalBaru);
                psUp.setInt(4, idSewa);
                psUp.executeUpdate();
                
                // Update Kendaraan jadi Tersedia
                String sqlKendaraan = "UPDATE kendaraan SET status = 'tersedia' WHERE id_kendaraan = ?";
                PreparedStatement psK = con.prepareStatement(sqlKendaraan);
                psK.setInt(1, idKendaraan);
                psK.executeUpdate();
            }
            con.commit();
        } catch(Exception e) {
            try { if(con!=null) con.rollback(); } catch(Exception ex){}
            e.printStackTrace();
        }
        return denda;
    }

    // 5. REPORT 
    public List<Sewa> getSewaByDate(String startDate, String endDate) {
    List<Sewa> list = new ArrayList<>();
    try {
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT s.id_sewa, u.nama_lengkap, k.merk, k.model, k.no_plat, s.tgl_sewa, s.tgl_pengembalian, s.ttl_harga, s.stts, s.denda " +
                     "FROM sewa s " +
                     "JOIN users u ON s.id_user = u.id " +
                     "JOIN kendaraan k ON s.id_kendaraan = k.id_kendaraan " +
                     "WHERE (s.tgl_sewa BETWEEN ? AND ?) " +
                     "ORDER BY s.tgl_sewa DESC";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, startDate);
        ps.setString(2, endDate);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            String info = rs.getString("merk") + " " + rs.getString("model");
            Sewa s = new Sewa(
                rs.getInt("id_sewa"),
                rs.getString("nama_lengkap"),
                info,
                rs.getString("no_plat"),
                rs.getDate("tgl_pengembalian"),
                rs.getDouble("ttl_harga"),
                rs.getString("stts")
            );
            
            s.setTglSewa(rs.getDate("tgl_sewa")); 
            s.setDenda(rs.getDouble("denda")); 
            list.add(s);
        }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
}
}