package com.example.quanlytailieu.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlytailieu.R;
import com.example.quanlytailieu.adapter.LoaiTaiLieuAdapter;
import com.example.quanlytailieu.adapter.TaiLieuAdapter;
import com.example.quanlytailieu.modle.DatabaseHelper;
import com.example.quanlytailieu.modle.LoaiTaiLieu;
import com.example.quanlytailieu.modle.TaiLieu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton btnThemTaiLieu;
    private Button btnThemLoaiTaiLieu, btnLoc2MB;
    private TextView appName;
    private Spinner spinnerLoaiTaiLieu;
    private TaiLieuAdapter taiLieuAdapter;
    private LoaiTaiLieuAdapter loaiTaiLieuAdapter;
    private List<TaiLieu> danhSachTaiLieu;
    private List<LoaiTaiLieu> danhSachLoaiTaiLieu;
    private DatabaseHelper dbHelper;
    private boolean isFilteredBySize = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerTaiLieu);
        btnThemTaiLieu = findViewById(R.id.btnThemTaiLieu);
        btnThemLoaiTaiLieu = findViewById(R.id.btnThemLoaiTaiLieu);
        btnLoc2MB = findViewById(R.id.btnLoc2MB);
        appName = findViewById(R.id.appName);
        spinnerLoaiTaiLieu = findViewById(R.id.spinnerLoaiTaiLieu);

        dbHelper = new DatabaseHelper(this);
        danhSachTaiLieu = new ArrayList<>();
        danhSachLoaiTaiLieu = new ArrayList<>();

        setupRecyclerView();
        setupSpinner();
        setupSuKien();
        doiFontChu();
    }

    private void setupRecyclerView() {
        taiLieuAdapter = new TaiLieuAdapter(this, danhSachTaiLieu, new TaiLieuAdapter.OnItemClickListener() {
            @Override
            public void onEdit(TaiLieu taiLieu) {
                // Không còn nút sửa, bỏ logic này
            }

            @Override
            public void onDelete(TaiLieu taiLieu) {
                boolean result = dbHelper.deleteTaiLieu(taiLieu.getMaTaiLieu());
                if (result) {
                    Toast.makeText(MainActivity.this, "Xóa: " + taiLieu.getTenTaiLieu(), Toast.LENGTH_SHORT).show();
                    loadTaiLieu();
                } else {
                    Toast.makeText(MainActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taiLieuAdapter);
        loadTaiLieu();
    }

    private void setupSpinner() {
        danhSachLoaiTaiLieu.clear();
        danhSachLoaiTaiLieu.addAll(dbHelper.getAllLoaiTaiLieu());
        danhSachLoaiTaiLieu.add(0, new LoaiTaiLieu(0, "ALL", "Tất cả", ""));

        loaiTaiLieuAdapter = new LoaiTaiLieuAdapter(this, danhSachLoaiTaiLieu);
        spinnerLoaiTaiLieu.setAdapter(loaiTaiLieuAdapter);

        spinnerLoaiTaiLieu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isFilteredBySize = false; // Reset bộ lọc kích thước khi chọn loại
                loadTaiLieu();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isFilteredBySize = false;
                loadTaiLieu();
            }
        });
    }

    private void loadTaiLieu() {
        danhSachTaiLieu.clear();
        if (isFilteredBySize) {
            danhSachTaiLieu.addAll(dbHelper.getTaiLieuBySize(2000000));
        } else {
            LoaiTaiLieu selectedLoai = (LoaiTaiLieu) spinnerLoaiTaiLieu.getSelectedItem();
            if (selectedLoai != null && !selectedLoai.getMaLoai().equals("ALL")) {
                danhSachTaiLieu.addAll(dbHelper.getTaiLieuByLoai(selectedLoai.getId()));
            } else {
                danhSachTaiLieu.addAll(dbHelper.getAllTaiLieu());
            }
        }
        taiLieuAdapter.notifyDataSetChanged();
    }

    private void setupSuKien() {
        btnThemLoaiTaiLieu.setOnClickListener(v -> startActivity(new Intent(this, DanhSachLoaiTaiLieu.class)));

        btnLoc2MB.setOnClickListener(v -> {
            isFilteredBySize = true;
            loadTaiLieu();
        });

        btnThemTaiLieu.setOnClickListener(v -> startActivity(new Intent(this, add_tai_lieu.class)));
    }

    private void doiFontChu() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VGARAMN.TTF");
        appName.setTypeface(font);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupSpinner();
        loadTaiLieu();
    }
}