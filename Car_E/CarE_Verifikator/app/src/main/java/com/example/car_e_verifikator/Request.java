package com.example.car_e_verifikator;

import androidx.annotation.NonNull;

public class Request extends RequestId {
   String Nama, Tanggal_Berangkat, Tanggal_Selesai, Proyek, Perjalanan;

    public Request() {
    }

    public String getNama() {
        return Nama;
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

    public void setNama(String nama) {
        Nama = nama;
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
}
