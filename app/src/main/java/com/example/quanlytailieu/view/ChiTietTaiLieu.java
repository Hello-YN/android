package com.example.quanlytailieu.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.DatabaseHelper;
import com.example.quanlytailieu.modle.LoaiTaiLieu;
import com.example.quanlytailieu.modle.TaiLieu;

public class ChiTietTaiLieu extends AppCompatActivity {
    private TextView tvMaTaiLieu, tvTenTaiLieu, tvLoaiTaiLieu, tvLinkDown, tvKichThuoc;
    private ImageButton btnSua;
    private Button btnQuayLai;
    private DatabaseHelper dbHelper;
    private String maTaiLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_tai_lieu);

        // Ánh xạ view
        tvMaTaiLieu = findViewById(R.id.tvMaTaiLieu);
        tvTenTaiLieu = findViewById(R.id.tvTenTaiLieu);
        tvLoaiTaiLieu = findViewById(R.id.tvLoaiTaiLieu);
        tvLinkDown = findViewById(R.id.tvLinkDown);
        tvKichThuoc = findViewById(R.id.tvKichThuoc);
        btnSua = findViewById(R.id.btnSua);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        dbHelper = new DatabaseHelper(this);

        // Lấy maTaiLieu từ Intent
        maTaiLieu = getIntent().getStringExtra("maTaiLieu");
        if (maTaiLieu == null) {
            Toast.makeText(this, "Không tìm thấy tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin tài liệu
        loadTaiLieu();

        // Kiểm tra btnSua trước khi gán sự kiện
        if (btnSua == null) {
            Log.e("ChiTietTaiLieu", "ImageButton btnSua is null. Check activity_chi_tiet_tai_lieu.xml for ID btnSua");
            Toast.makeText(this, "Lỗi giao diện: Không tìm thấy nút chỉnh sửa", Toast.LENGTH_SHORT).show();
        } else {
            // Xử lý nút chỉnh sửa
            btnSua.setOnClickListener(v -> {
                Intent intent = new Intent(this, UpdateTaiLieu.class);
                intent.putExtra("maTaiLieu", maTaiLieu);
                startActivity(intent);
            });
        }

        // Kiểm tra btnQuayLai trước khi gán sự kiện
        if (btnQuayLai == null) {
            Log.e("ChiTietTaiLieu", "Button btnQuayLai is null. Check activity_chi_tiet_tai_lieu.xml for ID btnQuayLai");
        } else {
            // Xử lý nút quay lại
            btnQuayLai.setOnClickListener(v -> finish());
        }
    }

    private void loadTaiLieu() {
        TaiLieu taiLieu = dbHelper.getTaiLieuByMa(maTaiLieu);
        if (taiLieu == null) {
            Toast.makeText(this, "Không tìm thấy tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        LoaiTaiLieu loaiTaiLieu = dbHelper.getLoaiTaiLieuById(taiLieu.getIdLoai());
        String tenLoai = loaiTaiLieu != null ? loaiTaiLieu.getTenLoai() : "Không xác định";

        tvMaTaiLieu.setText(taiLieu.getMaTaiLieu());
        tvTenTaiLieu.setText(taiLieu.getTenTaiLieu());
        tvLoaiTaiLieu.setText(tenLoai);
        tvLinkDown.setText(taiLieu.getLinkDown());
        tvKichThuoc.setText(String.format("%d bytes", taiLieu.getKichThuoc()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại thông tin tài liệu để cập nhật nếu có thay đổi từ UpdateTaiLieu
        loadTaiLieu();
    }
}