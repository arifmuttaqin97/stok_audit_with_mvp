package com.mvp.stokaudit.detailSerialBarang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mvp.stokaudit.R;
import com.mvp.stokaudit.detailGambarSerial.DetailGambarSerialActivity;

import java.util.List;

class DetailSerialBarangAdapter extends BaseAdapter {

    private final Context context;
    private final List<DetailSerialBarangModel> customer;

    DetailSerialBarangAdapter(Context context, List<DetailSerialBarangModel> customer) {
        this.context = context;
        this.customer = customer;
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

        final DetailSerialBarangModel detailSerial = customer.get(position);
        textView.setText(detailSerial.serial);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailGambarSerialActivity.class);
                i.putExtra("id_serial", detailSerial.id_serial);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    void addMore(List<DetailSerialBarangModel> moreData) {
        customer.addAll(moreData);
        notifyDataSetChanged();
    }
}
