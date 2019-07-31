package com.mvp.stokaudit.tambahBarang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.google.gson.internal.LinkedTreeMap;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mvp.stokaudit.R;
import com.mvp.stokaudit.detailBarang.DetailBarangActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TambahBarangActivity extends AppCompatActivity implements TambahBarangView {

    private final Map<String, String> headerMap = new HashMap<>();
    private final ArrayList<String> arrayList = new ArrayList<>();
    private final HashMap<String, Object> hashTambahBarang = new HashMap<>();
    private final ArrayList<HashMap<String, Integer>> idResponse = new ArrayList<>();
    private final String DATA_BARANG = "log_dataBarang";
    private RecyclerView rvGambar;
    private TextView testBarcode;
    private String kodeBarang = "";
    private TambahBarangAdapter tambahBarangAdapter;
    private String barcodes = "";
    private String id_lokasi;
    private String nama_site;
    private TambahBarangPresenter tambahBarangPresenter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        TextView testDataBarang = findViewById(R.id.testDataBarang);
        RelativeLayout btnBarcode = findViewById(R.id.btnBarcode);
        RelativeLayout btnPicture = findViewById(R.id.btnPicture);
        Button btnSave = findViewById(R.id.btnSave);
        rvGambar = findViewById(R.id.rvGambar);
        testBarcode = findViewById(R.id.testBarcode);

        tambahBarangPresenter = new TambahBarangPresenter(this);

        SharedPreferences mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (mLogin.contains("username") && mLogin.contains("nik")) {
            headerMap.put("User-Name", Objects.requireNonNull(mLogin.getString("username", "")));
            headerMap.put("User-Id", Objects.requireNonNull(mLogin.getString("nik", "")));
        } else {
            headerMap.put("User-Name", "");
            headerMap.put("User-Id", "");
        }

        headerMap.put("Client-Service", "gmedia-stok-audit");
        headerMap.put("Auth-Key", "gmedia");

        String dataBarang = getIntent().getStringExtra("Master");
        testDataBarang.setText(dataBarang);

        kodeBarang = getIntent().getStringExtra("KodeMaster");

        id_lokasi = getIntent().getStringExtra("id_lokasi");
        nama_site = getIntent().getStringExtra("nama_site");

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGambar.setLayoutManager(layoutManager);

        tambahBarangAdapter = new TambahBarangAdapter(arrayList);
        rvGambar.setAdapter(tambahBarangAdapter);

        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new IntentIntegrator(AddActivity.this).initiateScan();
                IntentIntegrator integrator = new IntentIntegrator(TambahBarangActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pix.start(TambahBarangActivity.this, 1);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahBarangModel tambahBarangModel = new TambahBarangModel(kodeBarang, id_lokasi, barcodes, idResponse);

                hashTambahBarang.put("id_barang", tambahBarangModel.id_barang);
                hashTambahBarang.put("id_lokasi", tambahBarangModel.id_lokasi);
                hashTambahBarang.put("serial", tambahBarangModel.serial);
                hashTambahBarang.put("data_image", tambahBarangModel.data_image);

                tambahBarangPresenter.addBarang(headerMap, hashTambahBarang);
            }
        });
    }

    @Override
    public void addBarang() {
        Toast.makeText(TambahBarangActivity.this, "Barang sudah berhasil disimpan", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(TambahBarangActivity.this, DetailBarangActivity.class);
        i.putExtra("Site", id_lokasi);
        i.putExtra("Nama_Site", nama_site);
        startActivity(i);
        finish();
    }

    @Override
    public void wrongResponse(String string) {
        Toast.makeText(TambahBarangActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(DATA_BARANG, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(DATA_BARANG, string);
    }

    @Override
    public void picture(LinkedTreeMap linkedTreeMap) {
        HashMap<String, Integer> hashImage = new HashMap<>();

        Double angkaTemp = (Double) linkedTreeMap.get("id");

        assert angkaTemp != null;

        hashImage.put("id_image", angkaTemp.intValue());
        idResponse.add(hashImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                barcodes = result.getContents();
                testBarcode.setText(barcodes);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            assert data != null;
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            arrayList.add(returnValue.get(0));

            tambahBarangAdapter = new TambahBarangAdapter(arrayList);
            rvGambar.setAdapter(tambahBarangAdapter);

            tambahBarangPresenter.picture(this, data, kodeBarang, headerMap);
        }
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
