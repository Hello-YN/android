package com.example.quanlytailieu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.TaiLieu;
import com.example.quanlytailieu.view.ChiTietTaiLieu;

import java.util.ArrayList;
import java.util.List;

public class TaiLieuAdapter extends RecyclerView.Adapter<TaiLieuAdapter.TaiLieuViewHolder> {
    private List<TaiLieu> danhSach;
    private Context context;
    private OnItemClickListener listener;

    // Constructor duy nhất
    public TaiLieuAdapter(Context context, List<TaiLieu> danhSach, OnItemClickListener listener) {
        this.context = context;
        this.danhSach = (danhSach != null) ? danhSach : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaiLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_tai_lieu cho từng item của RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_tai_lieu, parent, false);
        return new TaiLieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaiLieuViewHolder holder, int position) {
        TaiLieu tl = danhSach.get(position);

        // Gán giá trị vào các TextView
        holder.tvTen.setText(tl.getTenTaiLieu());
        holder.tvKichThuoc.setText(tl.getKichThuoc() / 1024 + " KB");

        // Sự kiện click vào item để mở trang chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietTaiLieu.class);
            intent.putExtra("maTaiLieu", tl.getMaTaiLieu());
            context.startActivity(intent);
        });

        // Sự kiện click vào nút sửa
        holder.btnSua.setOnClickListener(v -> listener.onEdit(tl));

        // Sự kiện click vào nút xóa
        holder.btnXoa.setOnClickListener(v -> listener.onDelete(tl));
    }

    @Override
    public int getItemCount() {
        return danhSach.size();
    }

    // Hàm cập nhật dữ liệu của adapter
    public void updateData(List<TaiLieu> newData) {
        this.danhSach.clear();
        this.danhSach.addAll(newData);
        notifyDataSetChanged();
    }

    // ViewHolder cho từng item trong RecyclerView
    public class TaiLieuViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvKichThuoc;
        ImageView imgIcon;
        ImageButton btnSua, btnXoa;

        public TaiLieuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvTen = itemView.findViewById(R.id.tvTenTaiLieu);
            tvKichThuoc = itemView.findViewById(R.id.tvKichThuoc);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }

    // Interface để xử lý sự kiện edit và delete
    public interface OnItemClickListener {
        void onEdit(TaiLieu taiLieu);
        void onDelete(TaiLieu taiLieu);
    }
}