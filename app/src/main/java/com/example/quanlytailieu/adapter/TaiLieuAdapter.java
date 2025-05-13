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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.TaiLieu;
import com.example.quanlytailieu.view.ChiTietTaiLieu;

import java.util.List;

public class TaiLieuAdapter extends RecyclerView.Adapter<TaiLieuAdapter.TaiLieuViewHolder> {
    private Context context;
    private List<TaiLieu> taiLieuList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(TaiLieu taiLieu);
        void onDelete(TaiLieu taiLieu);
    }

    public TaiLieuAdapter(Context context, List<TaiLieu> taiLieuList, OnItemClickListener listener) {
        this.context = context;
        this.taiLieuList = taiLieuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaiLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tai_lieu, parent, false);
        return new TaiLieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaiLieuViewHolder holder, int position) {
        TaiLieu taiLieu = taiLieuList.get(position);
        holder.tvTenTaiLieu.setText(taiLieu.getTenTaiLieu());
        holder.tvKichThuoc.setText(formatSize(taiLieu.getKichThuoc()));

        holder.btnXoa.setOnClickListener(v -> {
            // Hiển thị dialog xác nhận xóa
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa tài liệu \"" + taiLieu.getTenTaiLieu() + "\"?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (listener != null) {
                            listener.onDelete(taiLieu);
                        }
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietTaiLieu.class);
            intent.putExtra("maTaiLieu", taiLieu.getMaTaiLieu());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taiLieuList != null ? taiLieuList.size() : 0;
    }

    static class TaiLieuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvTenTaiLieu, tvKichThuoc;
        ImageButton btnXoa;

        public TaiLieuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvTenTaiLieu = itemView.findViewById(R.id.tvTenTaiLieu);
            tvKichThuoc = itemView.findViewById(R.id.tvKichThuoc);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }

    private String formatSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}