package com.mvp.stokaudit.tambahBarang;

import com.google.gson.internal.LinkedTreeMap;

interface TambahBarangView {
    void addBarang();
    void wrongResponse(String string);
    void failureResponse(String string);
    void picture(LinkedTreeMap linkedTreeMap);
}
