package com.example.quanlytailieu.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.DatabaseHelper;
import com.example.quanlytailieu.modle.LoaiTaiLieu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class DanhSachLoaiTaiLieu extends AppCompatActivity {
    private LinearLayout containerLoaiTaiLieu;
    private FloatingActionButton btnThemLoaiTaiLieu;
    private List<LoaiTaiLieu> danhSachLoaiTaiLieu;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_loai_tai_lieu);

        containerLoaiTaiLieu = findViewById(R.id.containerLoaiTaiLieu);
        btnThemLoaiTaiLieu = findViewById(R.id.btnThemLoaiTaiLieu);

        dbHelper = new DatabaseHelper(this);
        danhSachLoaiTaiLieu = new ArrayList<>();

        loadLoaiTaiLieu();

        btnThemLoaiTaiLieu.setOnClickListener(v -> startActivity(new Intent(this, add_loai_tai_lieu.class)));
    }

    private void loadLoaiTaiLieu() {
        // Xóa các view cũ
        containerLoaiTaiLieu.removeAllViews();

        // Lấy danh sách loại tài liệu
        danhSachLoaiTaiLieu.clear();
        danhSachLoaiTaiLieu.addAll(dbHelper.getAllLoaiTaiLieu());

        // Tạo view động cho mỗi loại tài liệu
        for (LoaiTaiLieu loaiTaiLieu : danhSachLoaiTaiLieu) {
            // Tạo LinearLayout ngang cho mỗi mục
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(8, 8, 8, 8);

            // Tạo TextView cho tenLoai
            TextView tvTenLoai = new TextView(this);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f); // Chiếm phần còn lại của không gian
            tvTenLoai.setLayoutParams(tvParams);
            tvTenLoai.setText(loaiTaiLieu.getTenLoai());
            tvTenLoai.setTextSize(16);
            tvTenLoai.setTextColor(getResources().getColor(android.R.color.black));
            tvTenLoai.setPadding(8, 8, 8, 8);

            // Thêm sự kiện onClick để mở ChiTietLoaiTaiLieu
            tvTenLoai.setOnClickListener(v -> {
                Intent intent = new Intent(DanhSachLoaiTaiLieu.this, ChiTietLoaiTaiLieu.class);
                intent.putExtra("idLoai", loaiTaiLieu.getId());
                startActivity(intent);
            });

            // Tạo Button xóa
            Button btnXoa = new Button(this);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            btnXoa.setLayoutParams(btnParams);
            btnXoa.setText("Xóa");
            btnXoa.setTextSize(14);
            btnXoa.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_light));
            btnXoa.setTextColor(getResources().getColor(android.R.color.white));
            btnXoa.setPadding(8, 4, 8, 4);

            // Xử lý sự kiện xóa với dialog xác nhận
            btnXoa.setOnClickListener(v -> {
                new AlertDialog.Builder(DanhSachLoaiTaiLieu.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa loại tài liệu \"" + loaiTaiLieu.getTenLoai() + "\"?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            boolean result = dbHelper.deleteLoaiTaiLieu(loaiTaiLieu.getId());
                            if (result) {
                                Toast.makeText(this, "Xóa loại tài liệu thành công", Toast.LENGTH_SHORT).show();
                                loadLoaiTaiLieu(); // Làm mới danh sách
                            } else {
                                Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            });

            // Thêm TextView và Button vào itemLayout
            itemLayout.addView(tvTenLoai);
            itemLayout.addView(btnXoa);

            // Thêm itemLayout vào container
            containerLoaiTaiLieu.addView(itemLayout);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLoaiTaiLieu();
    }
}