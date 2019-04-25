package com.mvp.stokaudit.listBarang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mvp.stokaudit.R;
import com.mvp.stokaudit.tambahBarang.TambahBarangActivity;

import java.util.List;

class ListBarangAdapter extends BaseAdapter {

    private final Context context;
    private final List<ListBarang> customer;
    private final String id_lokasi;
    private final String nama_site;

    ListBarangAdapter(Context context, List<ListBarang> customer, String id_lokasi, String nama_site) {
        this.context = context;
        this.customer = customer;
        this.id_lokasi = id_lokasi;
        this.nama_site = nama_site;
    }

    @Override
    public int getCount() {
        return customer.size();
    }

    @Override
    public Object getItem(int position) {
        return customer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.model, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textModel);

        final ListBarang listBarangResponseData = customer.get(position);
        textView.setText(listBarangResponseData.nama_barang);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TambahBarangActivity.class);
                i.putExtra("Master", listBarangResponseData.nama_barang);
                i.putExtra("KodeMaster", listBarangResponseData.id_header);
                i.putExtra("id_lokasi", id_lokasi);
                i.putExtra("nama_site", nama_site);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    void addMore(List<ListBarang> moreData) {
        customer.addAll(moreData);
        notifyDataSetChanged();
    }
}
