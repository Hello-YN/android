package com.example.quanlytailieu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.quanlytailieu.R;
import com.example.quanlytailieu.modle.LoaiTaiLieu;
import java.util.List;

public class LoaiTaiLieuAdapter extends ArrayAdapter<LoaiTaiLieu> {
    private final Context context;
    private final List<LoaiTaiLieu> loaiTaiLieuList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LoaiTaiLieu loaiTaiLieu);
    }

    // Constructor cho Spinner
    public LoaiTaiLieuAdapter(Context context, List<LoaiTaiLieu> loaiTaiLieuList) {
        this(context, loaiTaiLieuList, null);
    }

    // Constructor cho RecyclerView với listener
    public LoaiTaiLieuAdapter(Context context, List<LoaiTaiLieu> loaiTaiLieuList, OnItemClickListener listener) {
        super(context, 0, loaiTaiLieuList);
        this.context = context;
        this.loaiTaiLieuList = loaiTaiLieuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, android.R.layout.simple_spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, int layoutId) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            holder = new ViewHolder();
            holder.tvTenLoai = convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LoaiTaiLieu loaiTaiLieu = loaiTaiLieuList.get(position);
        holder.tvTenLoai.setText(loaiTaiLieu.getTenLoai());

        return convertView;
    }

    // Phương thức cho RecyclerView
    public View getRecyclerView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_loai_tai_lieu, parent, false);
            holder = new ViewHolder();
            holder.tvTenLoai = convertView.findViewById(R.id.tvTenLoai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LoaiTaiLieu loaiTaiLieu = loaiTaiLieuList.get(position);
        holder.tvTenLoai.setText(loaiTaiLieu.getTenLoai());

        if (listener != null) {
            convertView.setOnClickListener(v -> listener.onItemClick(loaiTaiLieu));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvTenLoai;
    }
}