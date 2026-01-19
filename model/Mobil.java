package com.rental.model;

public class Mobil extends Kendaraan {
    
    public Mobil() {}

    // Constructor 'nembak' ke Induk (super)
    public Mobil(int id, String merk, String model, int tahun, String noPlat, String status, double hargaSewa, double hargaBeli) {
        super(id, merk, model, tahun, noPlat, status, hargaSewa, hargaBeli);
    }
    
    @Override
    public String getJenis() {
        return "mobil"; // Huruf kecil biar sesuai database ENUM
    }
}