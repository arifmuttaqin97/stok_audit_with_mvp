package com.mvp.stokaudit.login;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.EditText;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.ApiResponse;
import com.mvp.stokaudit.retrofit.RetrofitBuilder;
import com.mvp.stokaudit.retrofit.RetrofitService;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LoginPresenter {
    private final LoginView loginView;

    LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    void checkUsername(SharedPreferences sharedPreferences) {
        if (sharedPreferences.contains("username")) {
            loginView.userFound();
        }
    }

    void login(String username, String password) {

        if (username.equals("") || password.equals("")) {
            loginView.emptyField();
        } else {
            loginView.login(username, password);
        }
    }

    void getDataLogin(HashMap<String, String> hashMap) {
        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.login(hashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseLogin = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof LinkedTreeMap) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) response.body().getResponse();
                            loginView.getDataLogin(linkedTreeMap);
                        } else {
                            loginView.wrongType(responseLogin);
                        }
                    } else {
                        loginView.wrongResponse(response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                loginView.failureResponse(t.getLocalizedMessage());
            }
        });
    }
}
