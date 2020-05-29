package com.example.care_driver;

public class Car extends CarId {
    String no_polisi, nama_mobil, OrderID, Proyek;

    public Car() {
    }

    public Car(String no_polisi, String nama_mobil, String orderID, String proyek) {
        this.no_polisi = no_polisi;
        this.nama_mobil = nama_mobil;
        this.OrderID = orderID;
        this.Proyek = proyek;
    }

    public String getProyek() {
        return Proyek;
    }

    public void setProyek(String proyek) {
        Proyek = proyek;
    }

    public String getNo_polisi() {
        return no_polisi;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public void setNo_polisi(String no_polisi) {
        this.no_polisi = no_polisi;
    }

    public String getNama_mobil() {
        return nama_mobil;
    }

    public void setNama_mobil(String nama_mobil) {
        this.nama_mobil = nama_mobil;
    }
}
