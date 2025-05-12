package com.example.quanlytailieu.view;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
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
    private EditText etMaTaiLieu, etTenTaiLieu, etLinkDown, etKichThuoc;
    private Spinner spinnerLoaiTaiLieu;
    private Button btnLuuTaiLieu, btnQuayLai;
    private DatabaseHelper dbHelper;
    private List<LoaiTaiLieu> loaiTaiLieuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tai_lieu);

        etMaTaiLieu = findViewById(R.id.etMaTaiLieu);
        etTenTaiLieu = findViewById(R.id.etTenTaiLieu);
        etLinkDown = findViewById(R.id.etLinkDown);
        etKichThuoc = findViewById(R.id.etKichThuoc);
        spinnerLoaiTaiLieu = findViewById(R.id.spinnerLoaiTaiLieu);
        btnLuuTaiLieu = findViewById(R.id.btnLuuTaiLieu);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        dbHelper = new DatabaseHelper(this);
        setupSpinner();

        btnLuuTaiLieu.setOnClickListener(v -> {
            String maTaiLieu = etMaTaiLieu.getText().toString().trim();
            String tenTaiLieu = etTenTaiLieu.getText().toString().trim();
            String linkDown = etLinkDown.getText().toString().trim();
            String kichThuocStr = etKichThuoc.getText().toString().trim();
            LoaiTaiLieu selectedLoai = (LoaiTaiLieu) spinnerLoaiTaiLieu.getSelectedItem();

            if (maTaiLieu.isEmpty() || tenTaiLieu.isEmpty() || selectedLoai == null) {
                Toast.makeText(this, "Vui lòng nhập mã, tên tài liệu và chọn loại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra maTaiLieu đã tồn tại
            if (dbHelper.getTaiLieuByMa(maTaiLieu) != null) {
                Toast.makeText(this, "Mã tài liệu đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            long kichThuoc = 0;
            if (!kichThuocStr.isEmpty()) {
                try {
                    kichThuoc = Long.parseLong(kichThuocStr);
                    if (kichThuoc < 0) {
                        Toast.makeText(this, "Kích thước không được âm", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Kích thước phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            TaiLieu taiLieu = new TaiLieu(maTaiLieu, tenTaiLieu, selectedLoai.getId(), linkDown, false);
            taiLieu.setKichThuoc(kichThuoc);

            long result = dbHelper.addTaiLieu(taiLieu);
            if (result != -1) {
                Toast.makeText(this, "Thêm tài liệu thành công", Toast.LENGTH_SHORT).show();
                clearFocusAndHideKeyboard();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại, thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        btnQuayLai.setOnClickListener(v -> {
            clearFocusAndHideKeyboard();
            finish();
        });
    }

    private void setupSpinner() {
        loaiTaiLieuList = dbHelper.getAllLoaiTaiLieu();
        ArrayAdapter<LoaiTaiLieu> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiTaiLieuList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiTaiLieu.setAdapter(adapter);
    }

    private void clearFocusAndHideKeyboard() {
        // Xóa focus khỏi EditText
        if (getCurrentFocus() != null) {
            getCurrentFocus().clearFocus();
        }

        // Đóng bàn phím ảo
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}