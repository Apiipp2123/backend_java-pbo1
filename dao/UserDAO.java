package com.rental.dao;

import com.rental.config.DatabaseConnection;
import com.rental.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    // --- LOGIN VIA EMAIL ---
    public User login(String email, String password) {
        User user = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            // Query cek email & password
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                // Pastikan urutan parameter sesuai dengan Constructor User.java yang baru
                user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("nama_lengkap"),
                    rs.getString("alamat"),
                    rs.getString("telepon")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return user;
    }

    // --- REGISTER DENGAN EMAIL ---
    public boolean registerUser(String username, String email, String password, String nama, String alamat, String telepon) {
        try {
            Connection con = DatabaseConnection.getConnection();
            
            // 1. Cek Email Duplikat (Email harus unik)
            PreparedStatement psCheck = con.prepareStatement("SELECT * FROM users WHERE email = ?");
            psCheck.setString(1, email);
            if(psCheck.executeQuery().next()) return false; 
            
            // 2. Cek Username Duplikat (Opsional, tapi disarankan)
            PreparedStatement psCheckUser = con.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUser.setString(1, username);
            if(psCheckUser.executeQuery().next()) return false;

            // 3. Insert Data
            String sql = "INSERT INTO users (username, email, password, role, nama_lengkap, alamat, telepon) VALUES (?, ?, ?, 'USER', ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, nama);
            ps.setString(5, alamat);
            ps.setString(6, telepon);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- GET ALL USERS (Untuk Admin) ---
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE role != 'ADMIN'");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                list.add(new User(
                    rs.getInt("id"), rs.getString("username"), rs.getString("email"), 
                    rs.getString("role"), rs.getString("nama_lengkap"), 
                    rs.getString("alamat"), rs.getString("telepon")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // --- DELETE USER ---
    public void deleteUser(int id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            // Hapus data terkait dulu (Sewa & Penjualan) agar tidak error Foreign Key
            con.createStatement().executeUpdate("DELETE FROM sewa WHERE id_user=" + id);
            con.createStatement().executeUpdate("DELETE FROM penjualan WHERE id_user=" + id);
            con.createStatement().executeUpdate("DELETE FROM users WHERE id=" + id);
        } catch(Exception e) { e.printStackTrace(); }
    }
}