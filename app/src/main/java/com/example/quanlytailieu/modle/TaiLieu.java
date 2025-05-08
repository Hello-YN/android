package com.example.quanlytailieu.modle;

public class TaiLieu {
    private String maTaiLieu;
    private String tenTaiLieu;
    private int idLoai;
    private String linkDown;
    private long kichThuoc;
    private boolean isDelete;

    public TaiLieu(String maTaiLieu, String tenTaiLieu, int idLoai, String linkDown, boolean isDelete) {
        this.maTaiLieu = maTaiLieu;
        this.tenTaiLieu = tenTaiLieu;
        this.idLoai = idLoai;
        this.linkDown = linkDown;
        this.isDelete = isDelete;
        this.kichThuoc = 0;
    }

    public String getMaTaiLieu() { return maTaiLieu; }
    public void setMaTaiLieu(String maTaiLieu) { this.maTaiLieu = maTaiLieu; }
    public String getTenTaiLieu() { return tenTaiLieu; }
    public void setTenTaiLieu(String tenTaiLieu) { this.tenTaiLieu = tenTaiLieu; }
    public int getIdLoai() { return idLoai; }
    public void setIdLoai(int idLoai) { this.idLoai = idLoai; }
    public String getLinkDown() { return linkDown; }
    public void setLinkDown(String linkDown) { this.linkDown = linkDown; }
    public long getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(long kichThuoc) { this.kichThuoc = kichThuoc; }
    public boolean isDelete() { return isDelete; }
    public void setDelete(boolean isDelete) { this.isDelete = isDelete; }
}