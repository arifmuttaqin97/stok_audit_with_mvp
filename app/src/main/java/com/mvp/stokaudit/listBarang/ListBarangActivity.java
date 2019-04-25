package com.mvp.stokaudit.listBarang;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListBarangActivity extends AppCompatActivity implements ListBarangView {

    private final Map<String, String> headerMap = new HashMap<>();
    private final Integer count = 15;
    private final String MASTER = "log_master";
    private ListView listMaster;
    private ProgressBar progressBar;
    private SharedPreferences mLogin;
    private String id_lokasi = "";
    private String nama_site = "";
    private ArrayList<ListBarang> arrayListBarang;
    private HashMap<String, String> hashListBarang;
    private ListBarang listBarang;
    private ListBarangAdapter listBarangAdapter;
    private Integer startIndex = 0;
    private ListBarangPresenter listBarangPresenter;
    private String moreSearch;
    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listMaster = findViewById(R.id.listMaster);
        progressBar = findViewById(R.id.loading);

        listBarangPresenter = new ListBarangPresenter(this);

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

        id_lokasi = getIntent().getStringExtra("id_lokasi");
        nama_site = getIntent().getStringExtra("nama_site");
    }

    private void getMaster(final String search){
        arrayListBarang = new ArrayList<>();
        hashListBarang = new HashMap<>();

        hashListBarang.put("start", "0");
        hashListBarang.put("count", "15");
        hashListBarang.put("search", search);

        progressBar.setVisibility(View.VISIBLE);

        listBarangPresenter.getDataMaster(headerMap, hashListBarang);

        moreSearch = search;
    }

    private void getMore(String search){
        arrayListBarang = new ArrayList<>();
        hashListBarang = new HashMap<>();

        hashListBarang.put("start", startIndex.toString());
        hashListBarang.put("count", count.toString());
        hashListBarang.put("search", search);

        progressBar.setVisibility(View.VISIBLE);

        listBarangPresenter.getMoreMaster(headerMap, hashListBarang);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getDataMaster(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            listBarang = new ListBarang(
                    (String) linkedTreeMap.get("id_header"),
                    (String) linkedTreeMap.get("nama_barang"),
                    (String) linkedTreeMap.get("merk"),
                    (String) linkedTreeMap.get("type"),
                    (String) linkedTreeMap.get("kode"),
                    (String) linkedTreeMap.get("id_ukuran"),
                    (String) linkedTreeMap.get("qty"),
                    (String) linkedTreeMap.get("qty_small"),
                    (String) linkedTreeMap.get("min_stock"),
                    (String) linkedTreeMap.get("max_stock"),
                    (String) linkedTreeMap.get("harga_jual"),
                    (String) linkedTreeMap.get("id_aktif"),
                    (String) linkedTreeMap.get("gambar"),
                    (String) linkedTreeMap.get("keterangan"),
                    (String) linkedTreeMap.get("user_input"),
                    (String) linkedTreeMap.get("tanggal_input"),
                    (String) linkedTreeMap.get("user_update"),
                    (String) linkedTreeMap.get("tanggal_update"),
                    (String) linkedTreeMap.get("flag"),
                    (String) linkedTreeMap.get("id_kategori"),
                    (String) linkedTreeMap.get("id_tipe"),
                    (String) linkedTreeMap.get("id_satuan_kecil")
            );
            arrayListBarang.add(listBarang);
        }
        listMaster.setAdapter(null);
        listBarangAdapter = new ListBarangAdapter(ListBarangActivity.this, arrayListBarang, id_lokasi, nama_site);
        listMaster.setAdapter(listBarangAdapter);
        listMaster.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount - 1 && listMaster.getCount() > (count - 1)) {
                    startIndex += count;
                    getMore(moreSearch);
                }
            }
        });
    }

    @Override
    public void wrongType(String string) {
        Toast.makeText(ListBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        Log.d(MASTER, string);
    }

    @Override
    public void wrongResponse(String string) {
        Toast.makeText(ListBarangActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(MASTER, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(MASTER, string);
    }

    @Override
    public void getMoreMaster(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            listBarang = new ListBarang(
                    (String) linkedTreeMap.get("id_header"),
                    (String) linkedTreeMap.get("nama_barang"),
                    (String) linkedTreeMap.get("merk"),
                    (String) linkedTreeMap.get("type"),
                    (String) linkedTreeMap.get("kode"),
                    (String) linkedTreeMap.get("id_ukuran"),
                    (String) linkedTreeMap.get("qty"),
                    (String) linkedTreeMap.get("qty_small"),
                    (String) linkedTreeMap.get("min_stock"),
                    (String) linkedTreeMap.get("max_stock"),
                    (String) linkedTreeMap.get("harga_jual"),
                    (String) linkedTreeMap.get("id_aktif"),
                    (String) linkedTreeMap.get("gambar"),
                    (String) linkedTreeMap.get("keterangan"),
                    (String) linkedTreeMap.get("user_input"),
                    (String) linkedTreeMap.get("tanggal_input"),
                    (String) linkedTreeMap.get("user_update"),
                    (String) linkedTreeMap.get("tanggal_update"),
                    (String) linkedTreeMap.get("flag"),
                    (String) linkedTreeMap.get("id_kategori"),
                    (String) linkedTreeMap.get("id_tipe"),
                    (String) linkedTreeMap.get("id_satuan_kecil")
            );
            arrayListBarang.add(listBarang);
        }
        if (listBarangAdapter != null) {
            listBarangAdapter.addMore(arrayListBarang);
        } else {
            Toast.makeText(ListBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            Log.d(MASTER, "List Barang Adapter null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getMaster(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getMaster(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        getMaster("");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
