package com.example.quanlytailieu.modle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quanlytailieu.modle.LoaiTaiLieu;
import com.example.quanlytailieu.modle.TaiLieu;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "qltailieu.db";
    private static final int DATABASE_VERSION = 3; // Tăng lên 3 để đảm bảo xóa database cũ

    // Bảng LoaiTaiLieu
    private static final String TABLE_LOAI_TAI_LIEU = "LoaiTaiLieu";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MA_LOAI = "maLoai";
    private static final String COLUMN_TEN_LOAI = "tenLoai";
    private static final String COLUMN_MO_TA = "moTa";
    private static final String COLUMN_IS_DELETE = "isDelete";

    // Bảng TaiLieu
    private static final String TABLE_TAI_LIEU = "TaiLieu";
    private static final String COLUMN_MA_TAI_LIEU = "maTaiLieu";
    private static final String COLUMN_TEN_TAI_LIEU = "tenTaiLieu";
    private static final String COLUMN_ID_LOAI = "idLoai";
    private static final String COLUMN_LINK_DOWN = "linkDown";
    private static final String COLUMN_KICH_THUOC = "kichThuoc";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng LoaiTaiLieu
        String createLoaiTaiLieuTable = "CREATE TABLE " + TABLE_LOAI_TAI_LIEU + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MA_LOAI + " TEXT NOT NULL UNIQUE, " +
                COLUMN_TEN_LOAI + " TEXT NOT NULL, " +
                COLUMN_MO_TA + " TEXT, " +
                COLUMN_IS_DELETE + " INTEGER DEFAULT 0)";
        db.execSQL(createLoaiTaiLieuTable);

        // Tạo bảng TaiLieu
        String createTaiLieuTable = "CREATE TABLE " + TABLE_TAI_LIEU + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MA_TAI_LIEU + " TEXT NOT NULL UNIQUE, " +
                COLUMN_TEN_TAI_LIEU + " TEXT NOT NULL, " +
                COLUMN_ID_LOAI + " INTEGER, " +
                COLUMN_LINK_DOWN + " TEXT, " +
                COLUMN_KICH_THUOC + " INTEGER, " +
                COLUMN_IS_DELETE + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY (" + COLUMN_ID_LOAI + ") REFERENCES " + TABLE_LOAI_TAI_LIEU + "(" + COLUMN_ID + "))";
        db.execSQL(createTaiLieuTable);

        // Chèn dữ liệu mẫu
        String sqlLoai = "INSERT INTO LoaiTaiLieu (maLoai, tenLoai, moTa, isDelete) VALUES" +
                "('LT001', 'Sách giáo khoa', 'Sách học tập chính khóa', 0)," +
                "('LT002', 'Tài liệu tham khảo', 'Sách tham khảo kiến thức', 0)," +
                "('LT003', 'Luận văn', 'Luận văn tốt nghiệp', 0)," +
                "('LT004', 'Tạp chí', 'Tạp chí khoa học', 0);";
        db.execSQL(sqlLoai);

        String sqlTaiLieu = "INSERT INTO TaiLieu (maTaiLieu, tenTaiLieu, idLoai, linkDown, kichThuoc, isDelete) VALUES" +
                "('TL001', 'Toán lớp 12', 1, 'https://example.com/toan12.pdf', 2048000, 0)," +
                "('TL002', 'Vật lý nâng cao', 2, 'https://example.com/vatly_nangcao.pdf', 3072000, 0)," +
                "('TL003', 'Luận văn CNTT', 3, 'https://example.com/luanvan_cntt.pdf', 5120000, 0)," +
                "('TL004', 'Tạp chí Khoa học số 1', 4, 'https://example.com/tapchi_khoahoc1.pdf', 1024000, 0);";
        db.execSQL(sqlTaiLieu);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_LIEU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAI_TAI_LIEU);
        onCreate(db);
    }

    // Thêm LoaiTaiLieu
    public long addLoaiTaiLieu(LoaiTaiLieu loaiTaiLieu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_LOAI, loaiTaiLieu.getMaLoai());
        values.put(COLUMN_TEN_LOAI, loaiTaiLieu.getTenLoai());
        values.put(COLUMN_MO_TA, loaiTaiLieu.getMoTa());
        values.put(COLUMN_IS_DELETE, loaiTaiLieu.isDelete() ? 1 : 0);

        long result = db.insert(TABLE_LOAI_TAI_LIEU, null, values);
        db.close();
        return result;
    }

    // Lấy tất cả LoaiTaiLieu
    public List<LoaiTaiLieu> getAllLoaiTaiLieu() {
        List<LoaiTaiLieu> loaiTaiLieuList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_LOAI_TAI_LIEU + " WHERE " + COLUMN_IS_DELETE + " = 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String maLoai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MA_LOAI));
                String tenLoai = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_LOAI));
                String moTa = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MO_TA));
                boolean isDelete = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_DELETE)) == 1;

                LoaiTaiLieu loaiTaiLieu = new LoaiTaiLieu(id, maLoai, tenLoai, moTa);
                loaiTaiLieu.setDelete(isDelete);
                loaiTaiLieuList.add(loaiTaiLieu);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return loaiTaiLieuList;
    }

    // Thêm TaiLieu
    public long addTaiLieu(TaiLieu taiLieu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA_TAI_LIEU, taiLieu.getMaTaiLieu());
        values.put(COLUMN_TEN_TAI_LIEU, taiLieu.getTenTaiLieu());
        values.put(COLUMN_ID_LOAI, taiLieu.getIdLoai());
        values.put(COLUMN_LINK_DOWN, taiLieu.getLinkDown());
        values.put(COLUMN_KICH_THUOC, taiLieu.getKichThuoc());
        values.put(COLUMN_IS_DELETE, taiLieu.isDelete() ? 1 : 0);

        long result = db.insert(TABLE_TAI_LIEU, null, values); // Sửa lỗi: dùng TABLE_TAI_LIEU thay vì TABLE_LOAI_TAI_LIEU
        db.close();
        return result;
    }

    // Đếm số lượng tài liệu
    public int getTaiLieuCount() {
        String query = "SELECT * FROM " + TABLE_TAI_LIEU + " WHERE " + COLUMN_IS_DELETE + " = 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}