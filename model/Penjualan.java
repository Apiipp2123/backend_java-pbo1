package com.rental.model;

import java.sql.Date;

public class Penjualan {
    private int id;
    private String infoKendaraan;
    private String namaPelanggan;
    private Date tglPenjualan;
    private double harga;

    public Penjualan(int id, String infoKendaraan, String namaPelanggan, Date tglPenjualan, double harga) {
        this.id = id;
        this.infoKendaraan = infoKendaraan;
        this.namaPelanggan = namaPelanggan;
        this.tglPenjualan = tglPenjualan;
        this.harga = harga;
    }

    // Getter
    public int getId() { return id; }
    public String getInfoKendaraan() { return infoKendaraan; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public Date getTglPenjualan() { return tglPenjualan; }
    public double getHarga() { return harga; }
}