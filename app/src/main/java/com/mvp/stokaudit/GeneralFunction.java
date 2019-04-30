package com.mvp.stokaudit;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Map;
import java.util.Objects;

public class GeneralFunction {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getHeader(SharedPreferences mLogin, Map<String, String> headerMap) {

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
}
