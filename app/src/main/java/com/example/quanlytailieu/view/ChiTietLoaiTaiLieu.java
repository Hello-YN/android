package com.example.quanlytailieu.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.DatabaseHelper;
import com.example.quanlytailieu.modle.LoaiTaiLieu;

public class ChiTietLoaiTaiLieu extends AppCompatActivity {
    private TextView tvMaLoai, tvTenLoai, tvMoTa;
    private ImageButton btnSua, btnXoa;
    private Button btnQuayLai;
    private DatabaseHelper dbHelper;
    private int idLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_loai_tai_lieu);

        tvMaLoai = findViewById(R.id.tvMaLoai);
        tvTenLoai = findViewById(R.id.tvTenLoai);
        tvMoTa = findViewById(R.id.tvMoTa);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        dbHelper = new DatabaseHelper(this);

        idLoai = getIntent().getIntExtra("idLoai", -1);
        if (idLoai == -1) {
            Toast.makeText(this, "Không tìm thấy loại tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadLoaiTaiLieu();

        btnSua.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateLoaiTaiLieu.class);
            intent.putExtra("idLoai", idLoai);
            startActivity(intent);
        });

        btnXoa.setOnClickListener(v -> {
            boolean result = dbHelper.deleteLoaiTaiLieu(idLoai);
            if (result) {
                Toast.makeText(this, "Xóa loại tài liệu thành công", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại DanhSachLoaiTaiLieu
            } else {
                Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
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

        tvMaLoai.setText(loaiTaiLieu.getMaLoai());
        tvTenLoai.setText(loaiTaiLieu.getTenLoai());
        tvMoTa.setText(loaiTaiLieu.getMoTa());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLoaiTaiLieu();
    }
}