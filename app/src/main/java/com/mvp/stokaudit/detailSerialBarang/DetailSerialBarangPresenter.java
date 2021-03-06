package com.mvp.stokaudit.detailSerialBarang;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.mvp.stokaudit.ApiResponse;
import com.mvp.stokaudit.retrofit.RetrofitBuilder;
import com.mvp.stokaudit.retrofit.RetrofitService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class DetailSerialBarangPresenter {

    private final DetailSerialBarangView detailSerialBarangView;

    DetailSerialBarangPresenter(DetailSerialBarangView detailSerialBarangView) {
        this.detailSerialBarangView = detailSerialBarangView;
    }

    void getDataDetailSerial(Map<String, String> headerMap, HashMap<String, String> hashMap) {
        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailSerial(headerMap, hashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                detailSerialBarangView.hideProgressbar();
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseSerial = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof List) {
                            List list = (List) response.body().getResponse();
                            detailSerialBarangView.getDataDetailSerial(list);
                        } else {
                            detailSerialBarangView.wrongType(responseSerial);
                        }
                    } else {
                        detailSerialBarangView.wrongResponse(response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                detailSerialBarangView.failureResponse(t.getLocalizedMessage());
            }
        });
    }

    void getMoreDetailSerial(Map<String, String> headerMap, HashMap<String, String> hashMap) {
        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailSerial(headerMap, hashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                detailSerialBarangView.hideProgressbar();
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseSerial = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof List) {
                            List list = (List) response.body().getResponse();
                            detailSerialBarangView.getMoreDetailSerial(list);
                        } else {
                            detailSerialBarangView.wrongType(responseSerial);
                        }
                    } else {
                        detailSerialBarangView.wrongResponse(response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                detailSerialBarangView.failureResponse(t.getLocalizedMessage());
            }
        });
    }
}
