package com.mvp.stokaudit.detailBarang;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.GeneralFunction;
import com.mvp.stokaudit.R;
import com.mvp.stokaudit.listBarang.ListBarangActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailBarangActivity extends AppCompatActivity implements DetailBarangView {

    private final Map<String, String> headerMap = new HashMap<>();
    private final Integer count = 15;
    private final String SAVE = "log_save";
    private ListView listSaved;
    private ProgressBar progressBar;
    private String site;
    private String nama_site;
    private ArrayList<DetailBarang> arrayDetailBarang;
    private HashMap<String, String> hashDetailBarang;
    private DetailBarang detailBarang;
    private DetailBarangAdapter detailBarangAdapter;
    private Integer startIndex = 0;
    private DetailBarangPresenter detailBarangPresenter;
    private String moreSearch;
    private String moreParams;
    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView testSaved = findViewById(R.id.textSaved);
        listSaved = findViewById(R.id.listSaved);
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.loading);

        detailBarangPresenter = new DetailBarangPresenter(this);

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

        site = getIntent().getStringExtra("Site");
        nama_site = getIntent().getStringExtra("Nama_Site");
        testSaved.setText(nama_site);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailBarangActivity.this, ListBarangActivity.class);
                i.putExtra("id_lokasi", site);
                i.putExtra("nama_site", nama_site);
                startActivity(i);
            }
        });
    }

    private void getDetailBarang(final String search, final String params) {
        arrayDetailBarang = new ArrayList<>();
        hashDetailBarang = new HashMap<>();

        hashDetailBarang.put("start", "0");
        hashDetailBarang.put("count", "15");
        hashDetailBarang.put("search", search);
        hashDetailBarang.put("id_lokasi", params);

        progressBar.setVisibility(View.VISIBLE);

        detailBarangPresenter.getDataDetailBarang(headerMap, hashDetailBarang);

        moreSearch = search;
        moreParams = params;
    }

    private void getMore(String search, String params) {
        arrayDetailBarang = new ArrayList<>();
        hashDetailBarang = new HashMap<>();

        hashDetailBarang.put("start", startIndex.toString());
        hashDetailBarang.put("count", count.toString());
        hashDetailBarang.put("search", search);
        hashDetailBarang.put("id_lokasi", params);

        progressBar.setVisibility(View.VISIBLE);

        detailBarangPresenter.getMoreDataBarang(headerMap, hashDetailBarang);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getDataDetailBarang(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            detailBarang = new DetailBarang(
                    (String) linkedTreeMap.get("id_barang"),
                    (String) linkedTreeMap.get("id_lokasi"),
                    (String) linkedTreeMap.get("nama_barang"),
                    (String) linkedTreeMap.get("total_serial")
            );
            arrayDetailBarang.add(detailBarang);
        }
        listSaved.setAdapter(null);
        detailBarangAdapter = new DetailBarangAdapter(DetailBarangActivity.this, arrayDetailBarang);
        listSaved.setAdapter(detailBarangAdapter);
        listSaved.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount - 1 && listSaved.getCount() > (count - 1)) {
                    startIndex += count;
                    getMore(moreSearch, moreParams);
                }
            }
        });
    }

    @Override
    public void wrongType(String string) {
        Toast.makeText(DetailBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        Log.d(SAVE, string);
    }

    @Override
    public void wrongResponse(String string) {
        Toast.makeText(DetailBarangActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(SAVE, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(SAVE, string);
    }

    @Override
    public void getMoreDataBarang(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            detailBarang = new DetailBarang(
                    (String) linkedTreeMap.get("id_barang"),
                    (String) linkedTreeMap.get("id_lokasi"),
                    (String) linkedTreeMap.get("nama_barang"),
                    (String) linkedTreeMap.get("total_serial")
            );
            arrayDetailBarang.add(detailBarang);
        }

        if (detailBarangAdapter != null) {
            detailBarangAdapter.addMore(arrayDetailBarang);
        } else {
            Log.d(SAVE, "Detail Barang Adapter null");
            Toast.makeText(DetailBarangActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
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
                getDetailBarang(s, site);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getDetailBarang(s, site);
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
        getDetailBarang("", site);
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
