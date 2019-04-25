package com.mvp.stokaudit.detailSerialBarang;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailSerialBarangActivity extends AppCompatActivity implements DetailSerialBarangView {

    private final Map<String, String> headerMap = new HashMap<>();
    private final Integer count = 15;
    private final String SERIAL = "log_serial";
    private TextView namaBarang;
    private ListView listSerial;
    private ProgressBar progressBar;
    private String id_lokasi;
    private String id_barang;
    private SharedPreferences mLogin;
    private ArrayList<DetailSerialBarang> arraySerial;
    private HashMap<String, String> hashSerial;
    private DetailSerialBarang serial;
    private DetailSerialBarangAdapter serialAdapter;
    private Integer startIndex = 0;
    private DetailSerialBarangPresenter detailSerialBarangPresenter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_serial_barang);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        namaBarang = findViewById(R.id.testNamaBarang);
        listSerial = findViewById(R.id.listSerial);
        progressBar = findViewById(R.id.loading);

        detailSerialBarangPresenter = new DetailSerialBarangPresenter(this);

        namaBarang.setText(getIntent().getStringExtra("nama_barang"));
        id_lokasi = getIntent().getStringExtra("id_lokasi");
        id_barang = getIntent().getStringExtra("id_barang");

        mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (mLogin.contains("username") && mLogin.contains("nik")) {
            headerMap.put("User-Name", Objects.requireNonNull(mLogin.getString("username", "")));
            headerMap.put("User-Id", Objects.requireNonNull(mLogin.getString("nik", "")));
        } else {
            headerMap.put("User-Name", "");
            headerMap.put("User-Id", "");
        }

        headerMap.put("Client-Service", "gmedia-stok-audit");
        headerMap.put("Auth-Key", "gmedia");
        headerMap.put("Content-Type", "application/json");
    }

    private void getSerial(){
        arraySerial = new ArrayList<>();
        hashSerial = new HashMap<>();

        hashSerial.put("start", "0");
        hashSerial.put("count", "15");
        hashSerial.put("id_lokasi", id_lokasi);
        hashSerial.put("id_barang", id_barang);

        progressBar.setVisibility(View.VISIBLE);

        detailSerialBarangPresenter.getDataDetailSerial(headerMap, hashSerial);
    }

    private void getMore(){
        arraySerial = new ArrayList<>();
        hashSerial = new HashMap<>();

        hashSerial.put("start", startIndex.toString());
        hashSerial.put("count", count.toString());
        hashSerial.put("id_lokasi", id_lokasi);
        hashSerial.put("id_barang", id_barang);

        progressBar.setVisibility(View.VISIBLE);

        detailSerialBarangPresenter.getMoreDetailSerial(headerMap, hashSerial);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getDataDetailSerial(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            serial = new DetailSerialBarang(
                    (String) linkedTreeMap.get("id_serial"),
                    (String) linkedTreeMap.get("id_barang"),
                    (String) linkedTreeMap.get("id_lokasi"),
                    (String) linkedTreeMap.get("nama_barang"),
                    (String) linkedTreeMap.get("serial")
            );
            arraySerial.add(serial);
        }
        listSerial.setAdapter(null);
        serialAdapter = new DetailSerialBarangAdapter(DetailSerialBarangActivity.this, arraySerial);
        listSerial.setAdapter(serialAdapter);
        listSerial.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount - 1 && listSerial.getCount() > (count - 1)) {
                    startIndex += count;
                    getMore();
                }
            }
        });
    }

    @Override
    public void wrongType(String string) {
        Toast.makeText(DetailSerialBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        Log.d(SERIAL, string);
    }

    @Override
    public void wrongResponse(String string) {
        Toast.makeText(DetailSerialBarangActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(SERIAL, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(SERIAL, string);
    }

    @Override
    public void getMoreDetailSerial(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            serial = new DetailSerialBarang(
                    (String) linkedTreeMap.get("id_serial"),
                    (String) linkedTreeMap.get("id_barang"),
                    (String) linkedTreeMap.get("id_lokasi"),
                    (String) linkedTreeMap.get("nama_barang"),
                    (String) linkedTreeMap.get("serial")
            );
            arraySerial.add(serial);
        }

        if (serialAdapter != null) {
            serialAdapter.addMore(arraySerial);
        } else {
            Log.d(SERIAL, "Serial Adapter null");
            Toast.makeText(DetailSerialBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        getSerial();
        super.onResume();
    }
}
