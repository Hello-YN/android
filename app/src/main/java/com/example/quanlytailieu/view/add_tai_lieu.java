package com.example.quanlytailieu.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.DatabaseHelper;
import com.example.quanlytailieu.modle.LoaiTaiLieu;
import com.example.quanlytailieu.modle.TaiLieu;

import java.util.List;

public class add_tai_lieu extends AppCompatActivity {
    private EditText etTenTaiLieu, etLinkDown;
    private Spinner spinnerLoaiTaiLieu;
    private Button btnLuuTaiLieu, btnQuayLai;
    private DatabaseHelper dbHelper;
    private List<LoaiTaiLieu> loaiTaiLieuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tai_lieu);

        etTenTaiLieu = findViewById(R.id.etTenTaiLieu);
        etLinkDown = findViewById(R.id.etLinkDown);
        spinnerLoaiTaiLieu = findViewById(R.id.spinnerLoaiTaiLieu);
        btnLuuTaiLieu = findViewById(R.id.btnLuuTaiLieu);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        dbHelper = new DatabaseHelper(this);
        setupSpinner();

        btnLuuTaiLieu.setOnClickListener(v -> {
            String tenTaiLieu = etTenTaiLieu.getText().toString().trim();
            String linkDown = etLinkDown.getText().toString().trim();
            LoaiTaiLieu selectedLoai = (LoaiTaiLieu) spinnerLoaiTaiLieu.getSelectedItem();

            if (tenTaiLieu.isEmpty() || selectedLoai == null) {
                Toast.makeText(this, "Vui lòng nhập tên tài liệu và chọn loại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo mã tài liệu tự động (TL + số thứ tự)
            String maTaiLieu = "TL" + String.format("%03d", dbHelper.getTaiLieuCount() + 1);

            TaiLieu taiLieu = new TaiLieu(maTaiLieu, tenTaiLieu, selectedLoai.getId(), linkDown, false);
            taiLieu.setKichThuoc(0); // Giá trị mặc định vì không có trường nhập kích thước

            long result = dbHelper.addTaiLieu(taiLieu);
            if (result != -1) {
                Toast.makeText(this, "Thêm tài liệu thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại, thử lại với mã khác", Toast.LENGTH_SHORT).show();
            }
        });

        btnQuayLai.setOnClickListener(v -> finish());
    }

    private void setupSpinner() {
        loaiTaiLieuList = dbHelper.getAllLoaiTaiLieu();
        Log.d("add_tai_lieu", "Số lượng loại tài liệu: " + loaiTaiLieuList.size());
        for (LoaiTaiLieu loai : loaiTaiLieuList) {
            Log.d("add_tai_lieu", "LoaiTaiLieu: id=" + loai.getId() + ", tenLoai=" + loai.getTenLoai());
        }

        ArrayAdapter<LoaiTaiLieu> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiTaiLieuList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiTaiLieu.setAdapter(adapter);
    }
}