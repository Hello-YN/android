package com.example.quanlytailieu.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.DatabaseHelper;
import com.example.quanlytailieu.modle.LoaiTaiLieu;

public class add_loai_tai_lieu extends AppCompatActivity {
    private EditText etMaLoai, etTenLoai, etMoTa;
    private Button btnLuuLoaiTaiLieu;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loai_tai_lieu);

        etMaLoai = findViewById(R.id.etMaLoai);
        etTenLoai = findViewById(R.id.etTenLoai);
        etMoTa = findViewById(R.id.etMoTa);
        btnLuuLoaiTaiLieu = findViewById(R.id.btnLuuLoaiTaiLieu);

        dbHelper = new DatabaseHelper(this);

        btnLuuLoaiTaiLieu.setOnClickListener(v -> {
            String maLoai = etMaLoai.getText().toString().trim();
            String tenLoai = etTenLoai.getText().toString().trim();
            String moTa = etMoTa.getText().toString().trim();

            if (maLoai.isEmpty() || tenLoai.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã và tên loại tài liệu", Toast.LENGTH_SHORT).show();
                return;
            }

            LoaiTaiLieu loaiTaiLieu = new LoaiTaiLieu(0, maLoai, tenLoai, moTa);
            long result = dbHelper.addLoaiTaiLieu(loaiTaiLieu);
            if (result != -1) {
                Toast.makeText(this, "Thêm loại tài liệu thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại, mã loại có thể đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}