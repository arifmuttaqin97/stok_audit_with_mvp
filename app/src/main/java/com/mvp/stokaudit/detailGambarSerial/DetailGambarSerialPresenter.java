package com.mvp.stokaudit.detailGambarSerial;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.ApiResponse;
import com.mvp.stokaudit.retrofit.RetrofitBuilder;
import com.mvp.stokaudit.retrofit.RetrofitService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import androidx.annotation.RequiresApi;

class DetailGambarSerialPresenter {
    private final DetailGambarSerialView detailGambarSerialView;

    DetailGambarSerialPresenter(DetailGambarSerialView detailGambarSerialView) {
        this.detailGambarSerialView = detailGambarSerialView;
    }

    void getDataSite(Map<String, String> headerMap, HashMap<String, String> hashMap) {
        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailGambar(headerMap, hashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseDetailGambar = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof LinkedTreeMap) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) response.body().getResponse();
                            detailGambarSerialView.getDataDetailGambar(linkedTreeMap);
                        } else {
                            detailGambarSerialView.wrongType(responseDetailGambar);
                        }
                    } else {
                        detailGambarSerialView.wrongResponse(response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                detailGambarSerialView.failureResponse(t.getLocalizedMessage());
            }
        });
    }
}
