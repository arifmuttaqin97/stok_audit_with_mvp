package com.mvp.stokaudit.tambahBarang;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mvp.stokaudit.R;

import java.util.ArrayList;

public class TambahBarangAdapter extends RecyclerView.Adapter<TambahBarangAdapter.ViewHolder> {
    private final ArrayList<String> arrayGambar;

    TambahBarangAdapter(ArrayList<String> arrayGambar) {
        this.arrayGambar = arrayGambar;
    }

    @NonNull
    @Override
    public TambahBarangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gambar, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TambahBarangAdapter.ViewHolder viewHolder, int i) {
        viewHolder.gambar.setImageURI(Uri.parse(arrayGambar.get(i)));
    }

    @Override
    public int getItemCount() {
        return arrayGambar.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView gambar;

        ViewHolder(View v) {
            super(v);
            gambar = v.findViewById(R.id.gambar);
        }
    }
}
