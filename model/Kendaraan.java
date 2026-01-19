package com.rental.model;

public class Kendaraan {
    protected int idKendaraan;
    protected String merk;
    protected String model;
    protected int tahun;
    protected String noPlat;
    protected String status;
    protected double hargaSewa;
    protected double hargaBeli;
    
    
    public Kendaraan() {}

    public Kendaraan(int id, String merk, String model, int tahun, String noPlat, String status, double hargaSewa, double hargaBeli) {
        this.idKendaraan = id;
        this.merk = merk;
        this.model = model;
        this.tahun = tahun;
        this.noPlat = noPlat;
        this.status = status;
        this.hargaSewa = hargaSewa;
        this.hargaBeli = hargaBeli;
    }

    // Method abstract-like (Bisa di-override anak-anaknya)
    public String getJenis() {
        return "Umum"; 
    }

    // Getter Setter (Sama seperti sebelumnya, copas aja semua)
    public int getIdKendaraan() { return idKendaraan; }
    public void setIdKendaraan(int idKendaraan) { this.idKendaraan = idKendaraan; }
    public String getMerk() { return merk; }
    public void setMerk(String merk) { this.merk = merk; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getTahun() { return tahun; }
    public void setTahun(int tahun) { this.tahun = tahun; }
    public String getNoPlat() { return noPlat; }
    public void setNoPlat(String noPlat) { this.noPlat = noPlat; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getHargaSewa() { return hargaSewa; }
    public void setHargaSewa(double hargaSewa) { this.hargaSewa = hargaSewa; }
    public double getHargaBeli() { return hargaBeli; }
    public void setHargaBeli(double hargaBeli) { this.hargaBeli = hargaBeli; }
}