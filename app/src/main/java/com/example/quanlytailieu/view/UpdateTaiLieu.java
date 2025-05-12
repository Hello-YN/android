package com.example.quanlytailieu.view;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.DatabaseHelper;
import com.example.quanlytailieu.modle.LoaiTaiLieu;
import com.example.quanlytailieu.modle.TaiLieu;

import java.util.List;

public class UpdateTaiLieu extends AppCompatActivity {
    private TextView tvMaTaiLieu;
    private EditText etTenTaiLieu, etLinkDown, etKichThuoc;
    private Spinner spinnerLoaiTaiLieu;
    private Button btnLuuTaiLieu, btnQuayLai;
    private DatabaseHelper dbHelper;
    private List<LoaiTaiLieu> loaiTaiLieuList;
    private String maTaiLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tai_lieu);

        // Ánh xạ view
        tvMaTaiLieu = findViewById(R.id.tvMaTaiLieu);
        etTenTaiLieu = findViewById(R.id.etTenTaiLieu);
        etLinkDown = findViewById(R.id.etLinkDown);
        etKichThuoc = findViewById(R.id.etKichThuoc);
        spinnerLoaiTaiLieu = findViewById(R.id.spinnerLoaiTaiLieu);
        btnLuuTaiLieu = findViewById(R.id.btnLuuTaiLieu);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        dbHelper = new DatabaseHelper(this);

        // Lấy maTaiLieu từ Intent
        maTaiLieu = getIntent().getStringExtra("maTaiLieu");
        if (maTaiLieu == null) {
            Toast.makeText(this, "Không tìm thấy tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Thiết lập Spinner
        setupSpinner();

        // Hiển thị thông tin tài liệu
        loadTaiLieu();

        // Xử lý nút Lưu
        btnLuuTaiLieu.setOnClickListener(v -> {
            String tenTaiLieu = etTenTaiLieu.getText().toString().trim();
            String linkDown = etLinkDown.getText().toString().trim();
            String kichThuocStr = etKichThuoc.getText().toString().trim();
            LoaiTaiLieu selectedLoai = (LoaiTaiLieu) spinnerLoaiTaiLieu.getSelectedItem();

            if (tenTaiLieu.isEmpty() || selectedLoai == null) {
                Toast.makeText(this, "Vui lòng nhập tên tài liệu và chọn loại", Toast.LENGTH_SHORT).show();
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

            boolean result = dbHelper.updateTaiLieu(taiLieu);
            if (result) {
                Toast.makeText(this, "Cập nhật tài liệu thành công", Toast.LENGTH_SHORT).show();
                clearFocusAndHideKeyboard();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý nút Quay lại
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

    private void loadTaiLieu() {
        TaiLieu taiLieu = dbHelper.getTaiLieuByMa(maTaiLieu);
        if (taiLieu == null) {
            Toast.makeText(this, "Không tìm thấy tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvMaTaiLieu.setText(taiLieu.getMaTaiLieu());
        etTenTaiLieu.setText(taiLieu.getTenTaiLieu());
        etLinkDown.setText(taiLieu.getLinkDown());
        etKichThuoc.setText(String.valueOf(taiLieu.getKichThuoc()));

        // Chọn loại tài liệu trong Spinner
        for (int i = 0; i < loaiTaiLieuList.size(); i++) {
            if (loaiTaiLieuList.get(i).getId() == taiLieu.getIdLoai()) {
                spinnerLoaiTaiLieu.setSelection(i);
                break;
            }
        }
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