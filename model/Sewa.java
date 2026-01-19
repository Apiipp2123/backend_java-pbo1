package com.rental.model;

import java.sql.Date;

public class Sewa {
    private int idSewa;
    private int idKendaraan;
    private int idPelanggan;
    private Date tglSewa;
    private Date tglPengembalian;
    private double ttlHarga;
    private double denda;
    private String stts; 

    // Kolom Tambahan (Hasil Join untuk Tampilan Admin)
    private String namaPelanggan;
    private String infoKendaraan;
    private String noPlat;

    public Sewa() {}

    // Constructor untuk List di Admin
    public Sewa(int idSewa, String namaPelanggan, String infoKendaraan, String noPlat, Date tglPengembalian, double ttlHarga, String stts) {
        this.idSewa = idSewa;
        this.namaPelanggan = namaPelanggan;
        this.infoKendaraan = infoKendaraan;
        this.noPlat = noPlat;
        this.tglPengembalian = tglPengembalian;
        this.ttlHarga = ttlHarga;
        this.stts = stts;
    }

    // Getter Setter Lengkap (Wajib Ada)
    public int getIdSewa() { return idSewa; }
    public void setIdSewa(int idSewa) { this.idSewa = idSewa; }
    
    public int getIdKendaraan() { return idKendaraan; }
    public void setIdKendaraan(int idKendaraan) { this.idKendaraan = idKendaraan; }
    
    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }
    
    public Date getTglSewa() { return tglSewa; }
    public void setTglSewa(Date tglSewa) { this.tglSewa = tglSewa; }
    
    public Date getTglPengembalian() { return tglPengembalian; }
    public void setTglPengembalian(Date tglPengembalian) { this.tglPengembalian = tglPengembalian; }
    
    public double getTtlHarga() { return ttlHarga; }
    public void setTtlHarga(double ttlHarga) { this.ttlHarga = ttlHarga; }
    
    public double getDenda() { return denda; }
    public void setDenda(double denda) { this.denda = denda; }
    
    public String getStts() { return stts; }
    public void setStts(String stts) { this.stts = stts; }
    
    public String getNamaPelanggan() { return namaPelanggan; }
    public String getInfoKendaraan() { return infoKendaraan; }
    public String getNoPlat() { return noPlat; }
}