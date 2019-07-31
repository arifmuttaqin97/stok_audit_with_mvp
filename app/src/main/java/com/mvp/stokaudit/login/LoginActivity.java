package com.mvp.stokaudit.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.R;
import com.mvp.stokaudit.customer.CustomerActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private final String LOGIN = "log_login";
    private EditText username;
    private EditText password;
    private SharedPreferences mLogin;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.userLogin);
        password = findViewById(R.id.passLogin);
        Button btnLogin = findViewById(R.id.btnLogin);

        loginPresenter = new LoginPresenter(this);

//        Inisialisasi Shared Preference
        mLogin = getSharedPreferences("Login", Context.MODE_PRIVATE);

        loginPresenter.checkUsername(mLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginModel login = new LoginModel(username.getText().toString(), password.getText().toString());
                loginPresenter.login(login.username, login.password);
            }
        });
    }

    @Override
    public void userFound() {
        Intent i = new Intent(LoginActivity.this, CustomerActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void emptyField() {
        Toast.makeText(LoginActivity.this, "Silahkan masukkan username dan password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void login(String user, String pass) {
        HashMap<String, String> loginHash = new HashMap<>();

        loginHash.put("username", user);
        loginHash.put("password", pass);

        loginPresenter.getDataLogin(loginHash);
    }

    @Override
    public void getDataLogin(LinkedTreeMap linkedTreeMap) {
        SharedPreferences.Editor editor = mLogin.edit();
        editor.putString("username", (String) linkedTreeMap.get("username"));
        editor.putString("nik", (String) linkedTreeMap.get("nik"));
        editor.putString("nama", (String) linkedTreeMap.get("nama"));
        editor.apply();

        Intent i = new Intent(LoginActivity.this, CustomerActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void wrongType(String string) {
        Toast.makeText(LoginActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        Log.d(LOGIN, string);
    }

    @Override
    public void wrongResponse(String string) {
        Toast.makeText(LoginActivity.this, string, Toast.LENGTH_SHORT).show();
        Log.d(LOGIN, string);
    }

    @Override
    public void failureResponse(String string) {
        Log.d(LOGIN, string);
    }
}
