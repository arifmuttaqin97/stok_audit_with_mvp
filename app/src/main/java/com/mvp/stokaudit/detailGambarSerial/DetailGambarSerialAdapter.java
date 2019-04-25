package com.mvp.stokaudit.detailGambarSerial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mvp.stokaudit.R;

import java.util.List;

public class DetailGambarSerialAdapter extends RecyclerView.Adapter<DetailGambarSerialAdapter.ViewHolder> {

    private final List detailGambar;

    DetailGambarSerialAdapter(List detailGambar) {
        this.detailGambar = detailGambar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gambar, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(viewHolder.context).load(detailGambar.get(i)).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return detailGambar.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final Context context;

        ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.gambar);
            context = v.getContext();
        }
    }
}
