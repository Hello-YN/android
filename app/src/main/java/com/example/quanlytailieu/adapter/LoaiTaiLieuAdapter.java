package com.example.quanlytailieu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quanlytailieu.modle.LoaiTaiLieu;

import java.util.List;

public class LoaiTaiLieuAdapter extends ArrayAdapter<LoaiTaiLieu> {
    private Context context;
    private List<LoaiTaiLieu> daSachLoaiTaiLieu;

    // Constructor nhận vào danh sách các đối tượng LoaiTaiLieu
    public LoaiTaiLieuAdapter(Context context, List<LoaiTaiLieu> danhSachLoaiTaiLieu) {
        super(context, 0, danhSachLoaiTaiLieu);
        this.context = context;
        this.daSachLoaiTaiLieu = danhSachLoaiTaiLieu;
    }

    // Phương thức getView() để hiển thị mỗi item trong Spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        // Lấy đối tượng LoaiTaiLieu tại vị trí hiện tại
        LoaiTaiLieu loaiTaiLieu = daSachLoaiTaiLieu.get(position);

        // Tìm TextView trong layout và gán tên loại tài liệu vào đó
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(loaiTaiLieu.getTenLoai()); // Lấy tên loại tài liệu để hiển thị

        return convertView;
    }

    // Phương thức getDropDownView() để hiển thị khi dropdown mở
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        // Lấy đối tượng LoaiTaiLieu tại vị trí hiện tại
        LoaiTaiLieu loaiTaiLieu = daSachLoaiTaiLieu.get(position);

        // Tìm TextView trong layout và gán tên loại tài liệu vào đó
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(loaiTaiLieu.getTenLoai()); // Hiển thị tên loại tài liệu khi dropdown mở

        return convertView;
    }
}
