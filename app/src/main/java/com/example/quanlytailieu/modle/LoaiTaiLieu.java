package com.example.quanlytailieu.modle;

public class LoaiTaiLieu {
    private int id;
    private String maLoai;
    private String tenLoai;
    private String moTa;
    private boolean isDelete;

    public LoaiTaiLieu(int id, String maLoai, String tenLoai, String moTa) {
        this.id = id;
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.isDelete = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }
    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public boolean isDelete() { return isDelete; }
    public void setDelete(boolean delete) { isDelete = delete; }

    @Override
    public String toString() {
        return tenLoai; // Hiển thị tenLoai trong Spinner
    }
}