package com.mvp.stokaudit.detailBarang;

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

class DetailBarangPresenter {
    private final DetailBarangView detailBarangView;

    DetailBarangPresenter(DetailBarangView detailBarangView) {
        this.detailBarangView = detailBarangView;
    }

    void getDataDetailBarang(Map<String, String> headerMap, HashMap<String, String> hashMap) {
        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailBarang(headerMap, hashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                detailBarangView.hideProgressbar();
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseSave = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof List) {
                            List list = (List) response.body().getResponse();
                            detailBarangView.getDataDetailBarang(list);
                        } else {
                            detailBarangView.wrongType(responseSave);
                        }
                    } else {
                        detailBarangView.wrongResponse(response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                detailBarangView.failureResponse(t.getLocalizedMessage());
            }
        });
    }

    void getMoreDataBarang(Map<String, String> headerMap, HashMap<String, String> hashMap) {
        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.detailBarang(headerMap, hashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                detailBarangView.hideProgressbar();
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        String responseSave = response.body().getResponse().toString() + response.body().getMetadata().toString();
                        if (response.body().getResponse() instanceof List) {
                            List list = (List) response.body().getResponse();
                            detailBarangView.getMoreDataBarang(list);
                        } else {
                            detailBarangView.wrongType(responseSave);
                        }
                    } else {
                        detailBarangView.wrongResponse(response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                detailBarangView.failureResponse(t.getLocalizedMessage());
            }
        });
    }
}
