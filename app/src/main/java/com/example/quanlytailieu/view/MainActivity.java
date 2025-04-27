package com.example.quanlytailieu.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
import com.example.quanlytailieu.modle.LoaiTaiLieu;
import com.example.quanlytailieu.modle.TaiLieu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton btnThemTaiLieu;
    private TaiLieuAdapter taiLieuAdapter;
    private List<TaiLieu> danhSachTaiLieu;
    private TextView appName;
    private Spinner spinnerLoaiTaiLieu; // Spinner cho loại tài liệu
    private List<LoaiTaiLieu> danhSachLoaiTaiLieu;
    private Button btnThemLoaiTaiLieu;

    SQLiteDatabase mysqldatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        recyclerView = findViewById(R.id.recyclerTaiLieu);
        btnThemTaiLieu = findViewById(R.id.btnThemTaiLieu);
        appName = findViewById(R.id.appName);
        spinnerLoaiTaiLieu = findViewById(R.id.spinnerLoaiTaiLieu);
        btnThemLoaiTaiLieu = findViewById(R.id.btnThemLoaiTaiLieu);

//        khời tạo database
        mysqldatabase=openOrCreateDatabase("qltailieu.db",MODE_PRIVATE,null);
// tạo table
        try {
            String sql1="CREATE TABLE LoaiTaiLieu (" +
                    "    maLoai TEXT PRIMARY KEY," +
                    "    tenLoai TEXT NOT NULL," +
                    "    moTa TEXT" +
                    ");";
            String sql2="CREATE TABLE TaiLieu (" +
                    "    maTaiLieu TEXT PRIMARY KEY," +
                    "    tenTaiLieu TEXT NOT NULL," +
                    "    maLoai TEXT," +
                    "    linkDown TEXT," +
                    "    kichThuoc INTEGER," +
                    "    FOREIGN KEY (maLoai) REFERENCES LoaiTaiLieu(maLoai)" +
                    ");";
            String sql3="INSERT INTO LoaiTaiLieu (maLoai, tenLoai, moTa) VALUES" +
                    "('LT001', 'Sách giáo khoa', 'Các loại sách phục vụ học tập chính khóa')," +
                    "('LT002', 'Tài liệu tham khảo', 'Sách dùng để tham khảo thêm kiến thức')," +
                    "('LT003', 'Luận văn', 'Các luận văn tốt nghiệp đại học, cao học')," +
                    "('LT004', 'Tạp chí', 'Các tạp chí khoa học và chuyên ngành');";
            String sql4="INSERT INTO TaiLieu (maTaiLieu, tenTaiLieu, maLoai, linkDown, kichThuoc) VALUES" +
                    "('TL001', 'Toán lớp 12', 'LT001', 'https://example.com/toan12.pdf', 2048000)," +
                    "('TL002', 'Vật lý nâng cao', 'LT002', 'https://example.com/vatly_nangcao.pdf', 3072000)," +
                    "('TL003', 'Luận văn CNTT', 'LT003', 'https://example.com/luanvan_cntt.pdf', 5120000)," +
                    "('TL004', 'Tạp chí Khoa học số 1', 'LT004', 'https://example.com/tapchi_khoahoc1.pdf', 1024000);";
//            mysqldatabase.execSQL(sql1);
//            mysqldatabase.execSQL(sql2);
//            mysqldatabase.execSQL(sql3);
//            mysqldatabase.execSQL(sql4);
        }catch (Exception e){
            Log.e("Error","table tồn tại");
        }
        // Khởi tạo dữ liệu mẫu
        danhSachLoaiTaiLieu = new ArrayList<>();
        // Danh sách tài liệu mẫu
        danhSachTaiLieu = new ArrayList<>();
        TaiLieu taiLieu1 = new TaiLieu("T01", "Java Programming", "LT1", "http://link.to/download");
        TaiLieu taiLieu2 = new TaiLieu("T01", "Java Programming", "LT1", "http://link.to/download");
        TaiLieu taiLieu3 = new TaiLieu("T01", "Java Programming", "LT1", "http://link.to/download");
        TaiLieu taiLieu4 = new TaiLieu("T01", "Java Programming", "LT1", "http://link.to/download");
        danhSachTaiLieu.add(taiLieu1);
        danhSachTaiLieu.add(taiLieu2);
        danhSachTaiLieu.add(taiLieu3);
        danhSachTaiLieu.add(taiLieu4);
        danhSachLoaiTaiLieu.add(new LoaiTaiLieu("LT1", "Sách", ""));
        danhSachLoaiTaiLieu.add(new LoaiTaiLieu("LT2", "Slide", ""));
        danhSachLoaiTaiLieu.add(new LoaiTaiLieu("LT3", "Bài giảng", ""));
        danhSachLoaiTaiLieu.add(new LoaiTaiLieu("ALL", "Tất cả", "")); // Thêm mục "Tất cả" vào danh sách

        LoaiTaiLieuAdapter loaiTaiLieuAdapter = new LoaiTaiLieuAdapter(this, danhSachLoaiTaiLieu);
        spinnerLoaiTaiLieu.setAdapter(loaiTaiLieuAdapter);

        // Xử lý sự kiện khi người dùng chọn loại tài liệu
        spinnerLoaiTaiLieu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                LoaiTaiLieu loaiTaiLieu = (LoaiTaiLieu) parentView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Chọn loại tài liệu: " + loaiTaiLieu.getTenLoai(), Toast.LENGTH_SHORT).show();

                // Nếu chọn "Tất cả", hiển thị tất cả tài liệu
                if (loaiTaiLieu.getMaLoai().equals("ALL")) {
                    showAllTaiLieu();
                } else {
                    // Cập nhật danh sách tài liệu theo loại (chức năng lọc)
                    filterTaiLieuByLoai(loaiTaiLieu);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Khi không chọn gì, hiển thị tất cả tài liệu
                showAllTaiLieu();
            }
        });



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

        // Font chữ
        Typeface font = Typeface.createFromAsset(getAssets(), "VGARAMN.TTF");
        appName.setTypeface(font);

        btnThemLoaiTaiLieu.setOnClickListener(view -> {
            // Hiển thị Toast thông báo
            Toast.makeText(this, "Thêm loại tài liệu mới", Toast.LENGTH_SHORT).show();
            // Tạo Intent để chuyển đến AddLoaiTaiLieuActivity
            Intent intent = new Intent(this, add_loai_tai_lieu.class);
            startActivity(intent);  // Bắt đầu Activity mới
        });

        btnThemTaiLieu.setOnClickListener(v -> {
            // Hiển thị Toast thông báo
            Toast.makeText(this, "Thêm tài liệu mới", Toast.LENGTH_SHORT).show();
            // Tạo Intent để chuyển đến AddTaiLieuActivity
            Intent intent = new Intent(this, add_tai_lieu.class);
            startActivity(intent);  // Bắt đầu Activity mới
        });
    }

    // Hàm lọc tài liệu theo loại
    private void filterTaiLieuByLoai(LoaiTaiLieu loaiTaiLieu) {
        List<TaiLieu> filteredList = new ArrayList<>();
        for (TaiLieu taiLieu : danhSachTaiLieu) {
            if (taiLieu.getMaLoai().equals(loaiTaiLieu.getMaLoai())) {
                filteredList.add(taiLieu);
            }
        }
        taiLieuAdapter.updateData(filteredList);
    }

    // Hàm hiển thị tất cả tài liệu
    private void showAllTaiLieu() {
        taiLieuAdapter.updateData(danhSachTaiLieu);
    }
}
