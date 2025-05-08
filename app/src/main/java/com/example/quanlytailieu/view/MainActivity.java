package com.example.quanlytailieu.view;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private Button btnThemLoaiTaiLieu;
    private TextView appName;
    private Spinner spinnerLoaiTaiLieu;
    private TaiLieuAdapter taiLieuAdapter;
    private LoaiTaiLieuAdapter loaiTaiLieuAdapter;
    private List<TaiLieu> danhSachTaiLieu;
    private List<LoaiTaiLieu> danhSachLoaiTaiLieu;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerTaiLieu);
        btnThemTaiLieu = findViewById(R.id.btnThemTaiLieu);
        btnThemLoaiTaiLieu = findViewById(R.id.btnThemLoaiTaiLieu);
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
                Toast.makeText(MainActivity.this, "Chức năng sửa chưa được triển khai", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(TaiLieu taiLieu) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE TaiLieu SET isDelete = 1 WHERE maTaiLieu = ?", new String[]{taiLieu.getMaTaiLieu()});
                db.close();
                Toast.makeText(MainActivity.this, "Xóa: " + taiLieu.getTenTaiLieu(), Toast.LENGTH_SHORT).show();
                loadTaiLieu();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taiLieuAdapter);
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
                loadTaiLieu();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadTaiLieu();
            }
        });

        loadTaiLieu();
    }

    private void loadTaiLieu() {
        danhSachTaiLieu.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        LoaiTaiLieu selectedLoai = (LoaiTaiLieu) spinnerLoaiTaiLieu.getSelectedItem();
        String query;
        String[] args = null;

        if (selectedLoai != null && !selectedLoai.getMaLoai().equals("ALL")) {
            query = "SELECT * FROM TaiLieu WHERE isDelete = 0 AND idLoai = ?";
            args = new String[]{String.valueOf(selectedLoai.getId())};
        } else {
            query = "SELECT * FROM TaiLieu WHERE isDelete = 0";
        }

        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            do {
                String maTaiLieu = cursor.getString(cursor.getColumnIndexOrThrow("maTaiLieu"));
                String tenTaiLieu = cursor.getString(cursor.getColumnIndexOrThrow("tenTaiLieu"));
                int idLoai = cursor.getInt(cursor.getColumnIndexOrThrow("idLoai"));
                String linkDown = cursor.getString(cursor.getColumnIndexOrThrow("linkDown"));
                long kichThuoc = cursor.getLong(cursor.getColumnIndexOrThrow("kichThuoc"));
                boolean isDelete = cursor.getInt(cursor.getColumnIndexOrThrow("isDelete")) == 1;

                TaiLieu taiLieu = new TaiLieu(maTaiLieu, tenTaiLieu, idLoai, linkDown, isDelete);
                taiLieu.setKichThuoc(kichThuoc);
                danhSachTaiLieu.add(taiLieu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        taiLieuAdapter.notifyDataSetChanged();
    }

    private void setupSuKien() {
        btnThemLoaiTaiLieu.setOnClickListener(v -> startActivity(new Intent(this, add_loai_tai_lieu.class)));
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