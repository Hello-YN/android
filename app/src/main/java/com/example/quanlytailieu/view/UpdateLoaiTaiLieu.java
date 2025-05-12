package com.example.quanlytailieu.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.DatabaseHelper;
import com.example.quanlytailieu.modle.LoaiTaiLieu;

public class UpdateLoaiTaiLieu extends AppCompatActivity {
    private EditText etMaLoai, etTenLoai, etMoTa;
    private Button btnLuu, btnQuayLai;
    private DatabaseHelper dbHelper;
    private int idLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_loai_tai_lieu);

        etMaLoai = findViewById(R.id.etMaLoai);
        etTenLoai = findViewById(R.id.etTenLoai);
        etMoTa = findViewById(R.id.etMoTa);
        btnLuu = findViewById(R.id.btnLuu);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        dbHelper = new DatabaseHelper(this);

        idLoai = getIntent().getIntExtra("idLoai", -1);
        if (idLoai == -1) {
            Toast.makeText(this, "Không tìm thấy loại tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadLoaiTaiLieu();

        btnLuu.setOnClickListener(v -> {
            String maLoai = etMaLoai.getText().toString().trim();
            String tenLoai = etTenLoai.getText().toString().trim();
            String moTa = etMoTa.getText().toString().trim();

            if (maLoai.isEmpty() || tenLoai.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã loại và tên loại", Toast.LENGTH_SHORT).show();
                return;
            }

            LoaiTaiLieu loaiTaiLieu = new LoaiTaiLieu(idLoai, maLoai, tenLoai, moTa);
            boolean result = dbHelper.updateLoaiTaiLieu(loaiTaiLieu);
            if (result) {
                Toast.makeText(this, "Cập nhật loại tài liệu thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại, mã loại có thể đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });

        btnQuayLai.setOnClickListener(v -> finish());
    }

    private void loadLoaiTaiLieu() {
        LoaiTaiLieu loaiTaiLieu = dbHelper.getLoaiTaiLieuById(idLoai);
        if (loaiTaiLieu == null) {
            Toast.makeText(this, "Không tìm thấy loại tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etMaLoai.setText(loaiTaiLieu.getMaLoai());
        etTenLoai.setText(loaiTaiLieu.getTenLoai());
        etMoTa.setText(loaiTaiLieu.getMoTa());
    }
}