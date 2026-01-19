package com.rental.dao;

import com.rental.config.DatabaseConnection;
import com.rental.model.Kendaraan;
import com.rental.model.Mobil;
import com.rental.model.Motor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KendaraanDAO {
    public List<Kendaraan> getAllKendaraan() {
        List<Kendaraan> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM kendaraan WHERE status != 'dibeli' ORDER BY id_kendaraan DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                String jenisDB = rs.getString("jenis");
                Kendaraan k = null;

                if(jenisDB != null && jenisDB.equalsIgnoreCase("mobil")) {
                    k = new Mobil(
                        rs.getInt("id_kendaraan"), rs.getString("merk"), rs.getString("model"),
                        rs.getInt("tahun"), rs.getString("no_plat"), rs.getString("status"),
                        rs.getDouble("harga_sewa_per_hari"), rs.getDouble("harga_beli")
                    );
                } else {
                    k = new Motor(
                        rs.getInt("id_kendaraan"), rs.getString("merk"), rs.getString("model"),
                        rs.getInt("tahun"), rs.getString("no_plat"), rs.getString("status"),
                        rs.getDouble("harga_sewa_per_hari"), rs.getDouble("harga_beli")
                    );
                }
                list.add(k);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // 2. Tambah Kendaraan
    public boolean addKendaraan(Kendaraan k) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "INSERT INTO kendaraan (jenis, merk, model, tahun, no_plat, harga_sewa_per_hari, harga_beli, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'tersedia')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, k.getJenis()); 
            ps.setString(2, k.getMerk());
            ps.setString(3, k.getModel());
            ps.setInt(4, k.getTahun());
            ps.setString(5, k.getNoPlat());
            ps.setDouble(6, k.getHargaSewa());
            ps.setDouble(7, k.getHargaBeli());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. FITUR BELI (PERBAIKAN BUG: Cek Status Dulu!)
    public boolean beliKendaraan(int idKendaraan, int idUser, double harga) {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            
            // CEK STATUS DULU: Kalau sedang disewa, JANGAN BOLEH DIBELI
            String sqlCek = "SELECT status FROM kendaraan WHERE id_kendaraan = ?";
            PreparedStatement psCek = con.prepareStatement(sqlCek);
            psCek.setInt(1, idKendaraan);
            ResultSet rs = psCek.executeQuery();
            
            if(rs.next()) {
                String statusSaatIni = rs.getString("status");
                // Kalau status bukan 'tersedia' (misal: 'disewa'), batalkan!
                if(!statusSaatIni.equalsIgnoreCase("tersedia")) {
                    return false; 
                }
            }

            con.setAutoCommit(false); // Mulai Transaksi
            
            // Insert Penjualan
            String sqlJual = "INSERT INTO penjualan (id_kendaraan, id_user, tgl_penjualan, harga) VALUES (?, ?, CURDATE(), ?)";
            PreparedStatement psJual = con.prepareStatement(sqlJual);
            psJual.setInt(1, idKendaraan);
            psJual.setInt(2, idUser);
            psJual.setDouble(3, harga);
            psJual.executeUpdate();
            
            // Update Status jadi 'dibeli'
            String sqlUpdate = "UPDATE kendaraan SET status = 'dibeli' WHERE id_kendaraan = ?";
            PreparedStatement psUp = con.prepareStatement(sqlUpdate);
            psUp.setInt(1, idKendaraan);
            psUp.executeUpdate();
            
            con.commit();
            return true;
        } catch (Exception e) {
            try { if(con != null) con.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        }
    }

    // 4. Get By ID
    public Kendaraan getKendaraanById(int id) {
        Kendaraan k = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM kendaraan WHERE id_kendaraan = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                String jenis = rs.getString("jenis");
                if(jenis != null && jenis.equalsIgnoreCase("mobil")){
                     k = new Mobil(
                        rs.getInt("id_kendaraan"), rs.getString("merk"), rs.getString("model"),
                        rs.getInt("tahun"), rs.getString("no_plat"), rs.getString("status"),
                        rs.getDouble("harga_sewa_per_hari"), rs.getDouble("harga_beli")
                    );
                } else {
                     k = new Motor(
                        rs.getInt("id_kendaraan"), rs.getString("merk"), rs.getString("model"),
                        rs.getInt("tahun"), rs.getString("no_plat"), rs.getString("status"),
                        rs.getDouble("harga_sewa_per_hari"), rs.getDouble("harga_beli")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return k;
    }

    // 5. Update Kendaraan
    public boolean updateKendaraan(Kendaraan k) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "UPDATE kendaraan SET jenis=?, merk=?, model=?, tahun=?, no_plat=?, harga_sewa_per_hari=?, harga_beli=? WHERE id_kendaraan=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, k.getJenis()); 
            ps.setString(2, k.getMerk());
            ps.setString(3, k.getModel());
            ps.setInt(4, k.getTahun());
            ps.setString(5, k.getNoPlat());
            ps.setDouble(6, k.getHargaSewa());
            ps.setDouble(7, k.getHargaBeli());
            ps.setInt(8, k.getIdKendaraan());
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 6. DELETE KENDARAAN (PERBAIKAN BUG: Hapus Dependensi Dulu!)
    public boolean deleteKendaraan(int id) {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); // Matikan autocommit biar aman
            
            // A. Hapus riwayat SEWA dulu (Foreign Key)
            String sqlDelSewa = "DELETE FROM sewa WHERE id_kendaraan = ?";
            PreparedStatement psSewa = con.prepareStatement(sqlDelSewa);
            psSewa.setInt(1, id);
            psSewa.executeUpdate();
            
            // B. Hapus riwayat PENJUALAN dulu (Foreign Key) - INI SOLUSI BUG KAMU
            String sqlDelJual = "DELETE FROM penjualan WHERE id_kendaraan = ?";
            PreparedStatement psJual = con.prepareStatement(sqlDelJual);
            psJual.setInt(1, id);
            psJual.executeUpdate();
            
            // C. Baru Hapus KENDARAAN
            String sql = "DELETE FROM kendaraan WHERE id_kendaraan = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            
            con.commit(); // Simpan
            return rows > 0;
        } catch (Exception e) {
            try { if(con!=null) con.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        }
    }
    
    // 7. Laporan Penjualan (Sama seperti sebelumnya)
    public List<com.rental.model.Penjualan> getPenjualanByDate(String startDate, String endDate) {
        List<com.rental.model.Penjualan> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT p.id, k.merk, k.model, u.nama_lengkap, p.tgl_penjualan, p.harga " +
                         "FROM penjualan p " +
                         "JOIN kendaraan k ON p.id_kendaraan = k.id_kendaraan " +
                         "JOIN users u ON p.id_user = u.id " +
                         "WHERE p.tgl_penjualan BETWEEN ? AND ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String info = rs.getString("merk") + " " + rs.getString("model");
                list.add(new com.rental.model.Penjualan(
                    rs.getInt("id"), info, rs.getString("nama_lengkap"),
                    rs.getDate("tgl_penjualan"), rs.getDouble("harga")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    public List<com.rental.model.Penjualan> getPenjualanByUser(int idUser) {
        List<com.rental.model.Penjualan> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT p.id, k.merk, k.model, u.nama_lengkap, p.tgl_penjualan, p.harga " +
                         "FROM penjualan p " +
                         "JOIN kendaraan k ON p.id_kendaraan = k.id_kendaraan " +
                         "JOIN users u ON p.id_user = u.id " +
                         "WHERE p.id_user = ? ORDER BY p.tgl_penjualan DESC";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String info = rs.getString("merk") + " " + rs.getString("model");
                list.add(new com.rental.model.Penjualan(
                    rs.getInt("id"), info, rs.getString("nama_lengkap"),
                    rs.getDate("tgl_penjualan"), rs.getDouble("harga")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Ambil SEMUA Penjualan (Buat admin.jsp)
    public List<com.rental.model.Penjualan> getAllPenjualan() {
        List<com.rental.model.Penjualan> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT p.id, k.merk, k.model, u.nama_lengkap, p.tgl_penjualan, p.harga " +
                         "FROM penjualan p " +
                         "JOIN kendaraan k ON p.id_kendaraan = k.id_kendaraan " +
                         "JOIN users u ON p.id_user = u.id " +
                         "ORDER BY p.tgl_penjualan DESC";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String info = rs.getString("merk") + " " + rs.getString("model");
                list.add(new com.rental.model.Penjualan(
                    rs.getInt("id"), info, rs.getString("nama_lengkap"),
                    rs.getDate("tgl_penjualan"), rs.getDouble("harga")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}