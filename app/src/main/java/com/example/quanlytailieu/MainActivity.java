package com.example.quanlytailieu;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlytailieu.adapter.LoaiTaiLieuAdapter;
import com.example.quanlytailieu.adapter.TaiLieuAdapter;
import com.example.quanlytailieu.modle.LoaiTaiLieu;
import com.example.quanlytailieu.modle.TaiLieu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton btnThem;
    private TaiLieuAdapter taiLieuAdapter;
    private List<TaiLieu> danhSachTaiLieu;
    private TextView appName;
    private Spinner spinnerLoaiTaiLieu; // Spinner cho loại tài liệu
    private List<LoaiTaiLieu> danhSachLoaiTaiLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        recyclerView = findViewById(R.id.recyclerTaiLieu);
        btnThem = findViewById(R.id.btnThemTaiLieu);
        appName = findViewById(R.id.appName);
        spinnerLoaiTaiLieu = findViewById(R.id.spinnerLoaiTaiLieu);

        // Khởi tạo dữ liệu mẫu
        danhSachLoaiTaiLieu = new ArrayList<>();
        danhSachLoaiTaiLieu.add(new LoaiTaiLieu("LT1", "Sách", ""));
        danhSachLoaiTaiLieu.add(new LoaiTaiLieu("LT2", "Slide", ""));
        danhSachLoaiTaiLieu.add(new LoaiTaiLieu("LT3", "Bài giảng", ""));

        LoaiTaiLieuAdapter loaiTaiLieuAdapter = new LoaiTaiLieuAdapter(this, danhSachLoaiTaiLieu);
        spinnerLoaiTaiLieu.setAdapter(loaiTaiLieuAdapter);

        // Xử lý sự kiện khi người dùng chọn loại tài liệu
        spinnerLoaiTaiLieu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                LoaiTaiLieu loaiTaiLieu = (LoaiTaiLieu) parentView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Chọn loại tài liệu: " + loaiTaiLieu.getTenLoai(), Toast.LENGTH_SHORT).show();
                // Cập nhật danh sách tài liệu theo loại (chức năng lọc)
                filterTaiLieuByLoai(loaiTaiLieu);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không làm gì nếu không chọn loại tài liệu
            }
        });

        // Danh sách tài liệu mẫu
        danhSachTaiLieu = new ArrayList<>();
        LoaiTaiLieu loaiTaiLieu = new LoaiTaiLieu("L01", "Sách", "Sách học");
        TaiLieu taiLieu = new TaiLieu("T01", "Java Programming", loaiTaiLieu.getMaLoai(), loaiTaiLieu, "http://link.to/download");


        taiLieuAdapter = new TaiLieuAdapter(this, danhSachTaiLieu, new TaiLieuAdapter.OnItemClickListener() {
            @Override
            public void onEdit(TaiLieu taiLieu) {
                Toast.makeText(MainActivity.this, "Sửa: " + taiLieu.getTenTaiLieu(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(TaiLieu taiLieu) {
                danhSachTaiLieu.remove(taiLieu);
                taiLieuAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taiLieuAdapter);

        //font chữ
        Typeface font = Typeface.createFromAsset(getAssets(), "VGARAMN.TTF");
        appName.setTypeface(font);

        btnThem.setOnClickListener(v -> {
            Toast.makeText(this, "Thêm tài liệu mới", Toast.LENGTH_SHORT).show();
        });
    }

    // Hàm lọc tài liệu theo loại
    private void filterTaiLieuByLoai(LoaiTaiLieu loaiTaiLieu) {
        List<TaiLieu> filteredList = new ArrayList<>();
        for (TaiLieu taiLieu : danhSachTaiLieu) {
            if (taiLieu.getLoaiTaiLieu().getMaLoai().equals(loaiTaiLieu.getMaLoai())) {
                filteredList.add(taiLieu);
            }
        }
        taiLieuAdapter.updateData(filteredList);
    }
}
