package com.mvp.stokaudit.detailGambarSerial;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.GeneralFunction;
import com.mvp.stokaudit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import androidx.annotation.RequiresApi;

public class DetailGambarSerialActivity extends AppCompatActivity implements DetailGambarSerialView {

    private final Map<String, String> headerMap = new HashMap<>();
    private final HashMap<String, String> hashMap = new HashMap<>();
    private final String GAMBAR = "log_detailGambar";
    private TextView detailGambar;
    private RecyclerView rvGambar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gambar_serial);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        detailGambar = findViewById(R.id.testDetailGambar);
        rvGambar = findViewById(R.id.rvGambar);

        DetailGambarSerialPresenter detailGambarSerialPresenter = new DetailGambarSerialPresenter(this);

        SharedPreferences mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

//        if (mLogin.contains("username") && mLogin.contains("nik")) {
//            headerMap.put("User-Name", Objects.requireNonNull(mLogin.getString("username", "")));
//            headerMap.put("User-Id", Objects.requireNonNull(mLogin.getString("nik", "")));
//        } else {
//            headerMap.put("User-Name", "");
//            headerMap.put("User-Id", "");
//        }
//
//        headerMap.put("Client-Service", "gmedia-stok-audit");
//        headerMap.put("Auth-Key", "gmedia");
//        headerMap.put("Content-Type", "application/json");

        GeneralFunction generalFunction = new GeneralFunction();
        generalFunction.getHeader(mLogin, headerMap);

        String id_serial = getIntent().getStringExtra("id_serial");

        hashMap.put("id_serial", id_serial);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGambar.setLayoutManager(layoutManager);

        detailGambarSerialPresenter.getDataSite(headerMap, hashMap);
    }

    @Override
    public void getDataDetailGambar(LinkedTreeMap linkedTreeMap) {
        detailGambar.setText((String) linkedTreeMap.get("nama_barang"));
        ArrayList arrayList = (ArrayList) linkedTreeMap.get("data_image");
        assert arrayList != null;

        ArrayList<String> arrayList1 = new ArrayList<>();

        for (Object object : arrayList) {
            LinkedTreeMap linkedTreeMap1 = (LinkedTreeMap) object;
            String tmpGambar = (String) linkedTreeMap1.get("image");
            arrayList1.add(tmpGambar);
        }
        DetailGambarSerialAdapter detailGambarAdapter = new DetailGambarSerialAdapter(arrayList1);
        rvGambar.setAdapter(detailGambarAdapter);
    }

    @Override
    public void wrongType(String string) {
        Toast.makeText(DetailGambarSerialActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        Log.d(GAMBAR, string);
    }

    @Override
    public void wrongResponse(String string) {
        Toast.makeText(DetailGambarSerialActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(GAMBAR, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(GAMBAR, string);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
