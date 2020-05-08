package com.example.thofarm3;

import java.io.Serializable;

public class sanpham implements Serializable {
    public int id;
    public String Ten;
    public int Gia;
    public String Donvi;
    public String xuatxu;
    public String mota;
    public String Hinhanh;
    public int id_loai;

    public sanpham(int id, String ten, int gia, String donvi, String xuatxu, String mota, String hinhanh, int id_loai) {
        this.id = id;
        Ten = ten;
        Gia = gia;
        Donvi = donvi;
        this.xuatxu = xuatxu;
        this.mota = mota;
        Hinhanh = hinhanh;
        this.id_loai = id_loai;
    }
    public String getXuatxu() {
        return xuatxu;
    }

    public void setXuatxu(String xuatxu) {
        this.xuatxu = xuatxu;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public int getGia() {
        return Gia;
    }

    public void setGia(int gia) {
        Gia = gia;
    }

    public String getDonvi() {
        return Donvi;
    }

    public void setDonvi(String donvi) {
        Donvi = donvi;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getHinhanh() {
        return Hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        Hinhanh = hinhanh;
    }

    public int getId_loai() {
        return id_loai;
    }

    public void setId_loai(int id_loai) {
        this.id_loai = id_loai;
    }
}
