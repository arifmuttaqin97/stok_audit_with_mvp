package com.mvp.stokaudit.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mvp.stokaudit.R;
import com.mvp.stokaudit.site.SiteActivity;

import java.util.List;

class CustomerAdapter extends BaseAdapter {

    private final Context context;
    private final List<Customer> customer;

    CustomerAdapter(Context context, List<Customer> customer) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.model_with_image, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textModel);

        final Customer customerResponseData = customer.get(position);
        textView.setText(customerResponseData.customer_name);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SiteActivity.class);
                i.putExtra("Customer", customerResponseData.customer_id);
                i.putExtra("Nama_Customer", customerResponseData.customer_name);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    void addMore(List<Customer> moreData) {
        customer.addAll(moreData);
        notifyDataSetChanged();
    }
}
