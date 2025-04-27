package com.example.quanlytailieu.modle;

import java.net.URL;
import java.net.URLConnection;

public class TaiLieu {
    private String maTaiLieu;
    private String tenTaiLieu;
    private String maLoai;  // Đây sẽ là khóa phụ trỏ đến maLoai của LoaiTaiLieu
    private LoaiTaiLieu loaiTaiLieu;  // Quan hệ với LoaiTaiLieu
    private String linkDown;
    private long kichThuoc; // Tính theo byte

    public TaiLieu() {}

    public TaiLieu(String maTaiLieu, String tenTaiLieu, String maLoai, LoaiTaiLieu loaiTaiLieu, String linkDown) {
        this.maTaiLieu = maTaiLieu;
        this.tenTaiLieu = tenTaiLieu;
        this.maLoai = maLoai;
        this.loaiTaiLieu = loaiTaiLieu;
        this.linkDown = linkDown;
        this.kichThuoc = tinhKichThuoc(linkDown); // Tự động set kích thước
    }

    public String getMaTaiLieu() {
        return maTaiLieu;
    }

    public void setMaTaiLieu(String maTaiLieu) {
        this.maTaiLieu = maTaiLieu;
    }

    public String getTenTaiLieu() {
        return tenTaiLieu;
    }

    public void setTenTaiLieu(String tenTaiLieu) {
        this.tenTaiLieu = tenTaiLieu;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public LoaiTaiLieu getLoaiTaiLieu() {
        return loaiTaiLieu;
    }

    public void setLoaiTaiLieu(LoaiTaiLieu loaiTaiLieu) {
        this.loaiTaiLieu = loaiTaiLieu;
    }

    public String getLinkDown() {
        return linkDown;
    }

    public void setLinkDown(String linkDown) {
        this.linkDown = linkDown;
        this.kichThuoc = tinhKichThuoc(linkDown); // Cập nhật lại kích thước nếu thay đổi link
    }

    public long getKichThuoc() {
        return kichThuoc;
    }

    private long tinhKichThuoc(String linkDown) {
        try {
            URL url = new URL(linkDown);
            URLConnection connection = url.openConnection();
            return connection.getContentLengthLong(); // Đơn vị byte
        } catch (Exception e) {
            System.err.println("Không thể lấy kích thước từ link: " + e.getMessage());
            return 0;
        }
    }
}
