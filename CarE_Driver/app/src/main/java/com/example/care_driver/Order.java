package com.example.care_driver;

public class Order extends OrderId{
    String Proyek,  Perjalanan, Alamat_Asal, Alamat_Tujuan, Nama, Tanggal_Berangkat, Tanggal_Selesai;
    String mobil;
    public Order() {
    }

    public Order(String proyek, String perjalanan, String alamat_Asal, String alamat_Tujuan, String nama, String tanggal_Berangkat, String tanggal_Selesai) {
        Proyek = proyek;
        Perjalanan = perjalanan;
        Alamat_Asal = alamat_Asal;
        Alamat_Tujuan = alamat_Tujuan;
        Nama = nama;
        Tanggal_Berangkat = tanggal_Berangkat;
        Tanggal_Selesai = tanggal_Selesai;
    }

    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public String getProyek() {
        return Proyek;
    }

    public void setProyek(String proyek) {
        Proyek = proyek;
    }

    public String getPerjalanan() {
        return Perjalanan;
    }

    public void setPerjalanan(String perjalanan) {
        Perjalanan = perjalanan;
    }

    public String getAlamat_Asal() {
        return Alamat_Asal;
    }

    public void setAlamat_Asal(String alamat_Asal) {
        Alamat_Asal = alamat_Asal;
    }

    public String getAlamat_Tujuan() {
        return Alamat_Tujuan;
    }

    public void setAlamat_Tujuan(String alamat_Tujuan) {
        Alamat_Tujuan = alamat_Tujuan;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getTanggal_Berangkat() {
        return Tanggal_Berangkat;
    }

    public void setTanggal_Berangkat(String tanggal_Berangkat) {
        Tanggal_Berangkat = tanggal_Berangkat;
    }

    public String getTanggal_Selesai() {
        return Tanggal_Selesai;
    }

    public void setTanggal_Selesai(String tanggal_Selesai) {
        Tanggal_Selesai = tanggal_Selesai;
    }
}
