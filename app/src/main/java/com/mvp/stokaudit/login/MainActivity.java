package com.mvp.stokaudit.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.customer.CustomerActivity;
import com.mvp.stokaudit.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements LoginView {

    private final String LOGIN = "log_login";
    private EditText username;
    private EditText password;
    private Button btnLogin;
    private String tmpUser;
    private String tmpPass;
    private SharedPreferences mLogin;
    private SharedPreferences.Editor editor;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.userLogin);
        password = findViewById(R.id.passLogin);
        btnLogin = findViewById(R.id.btnLogin);

        loginPresenter = new LoginPresenter(this);

        final HashMap<String, String> loginHash = new HashMap<>();

//        Inisialisasi Shared Preference
        mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

        if (mLogin.contains("username")) {
            Intent i = new Intent(MainActivity.this, CustomerActivity.class);
            startActivity(i);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpUser = username.getText().toString();
                tmpPass = password.getText().toString();

                if (tmpUser.equals("") || tmpPass.equals("")) {
                    Toast.makeText(MainActivity.this, "Silahkan masukkan username dan password", Toast.LENGTH_SHORT).show();
                } else {
                    loginHash.put("username", tmpUser);
                    loginHash.put("password", tmpPass);

                    loginPresenter.getDataLogin(loginHash);
                }
            }
        });
    }

    @Override
    public void getDataLogin(LinkedTreeMap linkedTreeMap) {
        editor = mLogin.edit();
        editor.putString("username", (String) linkedTreeMap.get("username"));
        editor.putString("nik", (String) linkedTreeMap.get("nik"));
        editor.putString("nama", (String) linkedTreeMap.get("nama"));
        editor.apply();

        Intent i = new Intent(MainActivity.this, CustomerActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void wrongType(String string) {
        Toast.makeText(MainActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        Log.d(LOGIN, string);
    }

    @Override
    public void wrongResponse(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(LOGIN, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(LOGIN, string);
    }
}
