package com.mvp.stokaudit.detailGambarSerial;

import com.google.gson.internal.LinkedTreeMap;

interface DetailGambarSerialView {
    void getDataDetailGambar(LinkedTreeMap linkedTreeMap);
    void wrongType(String string);
    void wrongResponse(String string);
    void failureResponse(String string);
}
