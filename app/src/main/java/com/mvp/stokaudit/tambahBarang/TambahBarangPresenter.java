package com.mvp.stokaudit.tambahBarang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.fxn.pix.Pix;
import com.google.gson.internal.LinkedTreeMap;
import com.mvp.stokaudit.ApiResponse;
import com.mvp.stokaudit.retrofit.RetrofitBuilder;
import com.mvp.stokaudit.retrofit.RetrofitService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class TambahBarangPresenter {
    private final TambahBarangView tambahBarangView;

    public TambahBarangPresenter(TambahBarangView tambahBarangView) {
        this.tambahBarangView = tambahBarangView;
    }

    public void addBarang(Map<String, String> headerMap, HashMap<String, Object> hashMap){
        RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
        Call<ApiResponse> call = retrofitService.tambahBarang(headerMap, hashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null) {
                    if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                        tambahBarangView.addBarang();
                    } else {
                        tambahBarangView.wrongResponse(response.body().getMetadata().get("message"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                tambahBarangView.failureResponse(t.getLocalizedMessage());
            }
        });
    }

    public void picture(Context context, @Nullable Intent data, String kodeBarang, Map<String, String> headerMap){
        try {
            assert data != null;
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(new File(data.getStringArrayListExtra(Pix.IMAGE_RESULTS).get(0))));
            Bitmap resizeBitmaps = resizeBitmap(bitmap, 1024);

            Uri tempUri = getImageUri(context.getApplicationContext(), resizeBitmaps);
            File finalFile = new File(getRealPathFromURI(context, tempUri));

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), finalFile);
            RequestBody id_barang = RequestBody.create(MediaType.parse("text/plain"), kodeBarang);

            MultipartBody.Part body = MultipartBody.Part.createFormData("pic", finalFile.getName(), reqFile);

            RetrofitService retrofitService = RetrofitBuilder.getApi().create(RetrofitService.class);
            Call<ApiResponse> call = retrofitService.uploadFoto(headerMap, body, id_barang);
            call.enqueue(new Callback<ApiResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    if (response.body() != null) {
                        if (Objects.equals(response.body().getMetadata().get("status"), "200")) {
                            if (response.body().getResponse() instanceof LinkedTreeMap) {
                                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) response.body().getResponse();
                                tambahBarangView.picture(linkedTreeMap);
                            } else {
                                tambahBarangView.wrongResponse(response.body().getMetadata().get("message"));
                            }
                        } else {
                            tambahBarangView.wrongResponse(response.body().getMetadata().get("message"));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    tambahBarangView.failureResponse(t.getLocalizedMessage());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    maxSize = ukuran pixel maksimal
    private static Bitmap resizeBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        int idx = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        }
        assert cursor != null;
        return cursor.getString(idx);
    }
}
