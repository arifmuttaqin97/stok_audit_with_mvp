package com.mvp.stokaudit.site;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteActivity extends AppCompatActivity implements SiteView {

    private final Map<String, String> headerMap = new HashMap<>();
    private final Integer count = 15;
    private final String SITE = "log_site";
    private ListView listSite;
    private ProgressBar progressBar;
    private String customer;
    private ArrayList<Site> arrayCustomerSite;
    private HashMap<String, String> hashCustomerSite;
    private Site site;
    private SiteAdapter siteAdapter;
    private Integer startIndex = 0;
    private SitePresenter sitePresenter;
    private SearchView searchView;
    private String moreSearch;
    private String moreParams;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView testSite = findViewById(R.id.textSite);
        listSite = findViewById(R.id.listSite);
        progressBar = findViewById(R.id.loading);

        sitePresenter = new SitePresenter(this);

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

        customer = getIntent().getStringExtra("Customer");
        String nama_customer = getIntent().getStringExtra("Nama_Customer");

        testSite.setText(nama_customer);
    }

    private void getSite(final String search, final String params) {
        arrayCustomerSite = new ArrayList<>();
        hashCustomerSite = new HashMap<>();

        hashCustomerSite.put("start", "0");
        hashCustomerSite.put("count", "10");
        hashCustomerSite.put("search", search);
        hashCustomerSite.put("customer_id", params);

        progressBar.setVisibility(View.VISIBLE);

        sitePresenter.getDataSite(headerMap, hashCustomerSite);

        moreSearch = search;
        moreParams = params;
    }

    private void getMore(String searchMore, String params) {
        arrayCustomerSite = new ArrayList<>();
        hashCustomerSite = new HashMap<>();

        hashCustomerSite.put("start", startIndex.toString());
        hashCustomerSite.put("count", count.toString());
        hashCustomerSite.put("search", searchMore);
        hashCustomerSite.put("customer_id", params);

        progressBar.setVisibility(View.VISIBLE);

        sitePresenter.getMoreSite(headerMap, hashCustomerSite);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getDataSite(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            site = new Site(
                    (String) linkedTreeMap.get("id_site"),
                    (String) linkedTreeMap.get("service_id"),
                    (String) linkedTreeMap.get("nama_site"),
                    (String) linkedTreeMap.get("alamat_site"),
                    (String) linkedTreeMap.get("kota_site"),
                    (String) linkedTreeMap.get("telp_site")
            );
            arrayCustomerSite.add(site);
        }
        listSite.setAdapter(null);
        siteAdapter = new SiteAdapter(SiteActivity.this, arrayCustomerSite);
        listSite.setAdapter(siteAdapter);
        listSite.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount - 1 && listSite.getCount() > (count - 1)) {
                    startIndex += count;
                    getMore(moreSearch, moreParams);
                }
            }
        });
    }

    @Override
    public void wrongType(String string) {
        Toast.makeText(SiteActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        Log.d(SITE, string);
    }

    @Override
    public void wrongResponse(String string) {
        Toast.makeText(SiteActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(SITE, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(SITE, string);
    }

    @Override
    public void getMoreSite(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            site = new Site(
                    (String) linkedTreeMap.get("id_site"),
                    (String) linkedTreeMap.get("service_id"),
                    (String) linkedTreeMap.get("nama_site"),
                    (String) linkedTreeMap.get("alamat_site"),
                    (String) linkedTreeMap.get("kota_site"),
                    (String) linkedTreeMap.get("telp_site")
            );
            arrayCustomerSite.add(site);
        }

        if (siteAdapter != null) {
            siteAdapter.addMore(arrayCustomerSite);
        } else {
            Log.d(SITE, "Site Adapter null");
            Toast.makeText(SiteActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
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
                getSite(s, customer);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getSite(s, customer);
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
        getSite("", customer);
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
