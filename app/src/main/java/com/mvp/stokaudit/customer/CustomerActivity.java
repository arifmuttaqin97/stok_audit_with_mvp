package com.mvp.stokaudit.customer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.GeneralFunction;
import com.mvp.stokaudit.R;
import com.mvp.stokaudit.login.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerActivity extends AppCompatActivity implements CustomerView {

    private final Map<String, String> headerMap = new HashMap<>();
    private final Integer count = 15;
    private final String CUSTOMER = "log_customer";
    private ListView listMain;
    private ProgressBar progressBar;
    private SharedPreferences mLogin;
    private ArrayList<Customer> arrayCustomer;
    private HashMap<String, String> hashCustomer;
    private Customer customer;
    private CustomerAdapter customerAdapter;
    private Integer startIndex = 0;
    private CustomerPresenter customerPresenter;
    private SearchView searchView;
    private String moreSearch;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        listMain = findViewById(R.id.listCustomer);
        progressBar = findViewById(R.id.loading);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        customerPresenter = new CustomerPresenter(this);

        mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

        GeneralFunction generalFunction = new GeneralFunction();
        generalFunction.getHeader(mLogin, headerMap);
    }

    private void getCustomer(final String search) {
        arrayCustomer = new ArrayList<>();
        hashCustomer = new HashMap<>();

        hashCustomer.put("start", "0");
        hashCustomer.put("count", "15");
        hashCustomer.put("search", search);

        progressBar.setVisibility(View.VISIBLE);

        customerPresenter.getDataCustomer(headerMap, hashCustomer);
        moreSearch = search;
    }

    private void getMore(String searchMore) {
        arrayCustomer = new ArrayList<>();
        hashCustomer = new HashMap<>();

        hashCustomer.put("start", startIndex.toString());
        hashCustomer.put("count", count.toString());
        hashCustomer.put("search", searchMore);

        progressBar.setVisibility(View.VISIBLE);

        customerPresenter.getMoreCustomer(headerMap, hashCustomer);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getDataCustomer(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;
            customer = new Customer(
                    (String) linkedTreeMap.get("customer_id"),
                    (String) linkedTreeMap.get("customer_name"),
                    (String) linkedTreeMap.get("alamat")
            );
            arrayCustomer.add(customer);
        }

        listMain.setAdapter(null);
        customerAdapter = new CustomerAdapter(CustomerActivity.this, arrayCustomer);
        listMain.setAdapter(customerAdapter);
        listMain.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount - 1 && listMain.getCount() > (count - 1)) {
                    startIndex += count;
                    getMore(moreSearch);
                }
            }
        });
    }

    @Override
    public void wrongType(String string) {
        Toast.makeText(CustomerActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        Log.d(CUSTOMER, string);
    }

    @Override
    public void wrongResponse(String string) {
        arrayCustomer.clear();

        listMain.setAdapter(null);
        customerAdapter = new CustomerAdapter(CustomerActivity.this, arrayCustomer);
        listMain.setAdapter(customerAdapter);

        Toast.makeText(CustomerActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(CUSTOMER, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(CUSTOMER, string);
    }

    @Override
    public void getMoreCustomer(List list) {
        for (Object object : list) {
            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;

            customer = new Customer(
                    (String) linkedTreeMap.get("customer_id"),
                    (String) linkedTreeMap.get("customer_name"),
                    (String) linkedTreeMap.get("alamat")
            );
            arrayCustomer.add(customer);
        }

        if (customerAdapter != null) {
            customerAdapter.addMore(arrayCustomer);
        } else {
            Log.d(CUSTOMER, "Customer adapter null");
            Toast.makeText(CustomerActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_with_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getCustomer(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getCustomer(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences.Editor editor = mLogin.edit();
                editor.remove("username");
                editor.apply();

                Toast.makeText(this, "Anda telah logout dari aplikasi ini", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(CustomerActivity.this, MainActivity.class);
                startActivity(i);
                finish();

                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        getCustomer("");
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
