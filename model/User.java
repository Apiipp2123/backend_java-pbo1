package com.rental.model;

public class User {
    private int id;
    private String username;
    private String email;       
    private String role;        
    private String namaLengkap; 
    private String alamat;      
    private String telepon;    

    // Constructor Kosong (Penting untuk framework tertentu)
    public User() {}

    // Constructor Lengkap (Dipakai saat Login/Ambil data dari DB)
    public User(int id, String username, String email, String role, String namaLengkap, String alamat, String telepon) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.namaLengkap = namaLengkap;
        this.alamat = alamat;
        this.telepon = telepon;
    }
    
    // --- GETTER & SETTER LENGKAP ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
}