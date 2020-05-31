package com.example.car_e;

public class Request extends RequestId {
    String Nama_Pengirim, Tanggal_Berangkat, Tanggal_Selesai, Proyek, Perjalanan;
    String Alamat_Asal, Alamat_Tujuan;

    public String getNama_Pengirim() {
        return Nama_Pengirim;
    }

    public Request() {
    }



    public String getTanggal_Berangkat() {
        return Tanggal_Berangkat;
    }

    public String getTanggal_Selesai() {
        return Tanggal_Selesai;
    }

    public String getProyek() {
        return Proyek;
    }

    public String getPerjalanan() {
        return Perjalanan;
    }

    public String getAlamat_Asal() {
        return Alamat_Asal;
    }

    public String getAlamat_Tujuan() {
        return Alamat_Tujuan;
    }

    public void setNama_Pengirim(String nama_Pengirim) {
        Nama_Pengirim = nama_Pengirim;
    }

    public void setTanggal_Berangkat(String tanggal_Berangkat) {
        Tanggal_Berangkat = tanggal_Berangkat;
    }

    public void setTanggal_Selesai(String tanggal_Selesai) {
        Tanggal_Selesai = tanggal_Selesai;
    }

    public void setProyek(String proyek) {
        Proyek = proyek;
    }

    public void setPerjalanan(String perjalanan) {
        Perjalanan = perjalanan;
    }

    public void setAlamat_Asal(String alamat_Asal) {
        Alamat_Asal = alamat_Asal;
    }

    public void setAlamat_Tujuan(String alamat_Tujuan) {
        Alamat_Tujuan = alamat_Tujuan;
    }

    public Request(String nama_Pengirim, String tanggal_Berangkat, String tanggal_Selesai, String proyek, String perjalanan, String alamat_Asal, String alamat_Tujuan) {
        Nama_Pengirim = nama_Pengirim;
        Tanggal_Berangkat = tanggal_Berangkat;
        Tanggal_Selesai = tanggal_Selesai;
        Proyek = proyek;
        Perjalanan = perjalanan;
        Alamat_Asal = alamat_Asal;
        Alamat_Tujuan = alamat_Tujuan;
    }
}

