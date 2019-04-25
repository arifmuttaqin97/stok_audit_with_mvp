package com.mvp.stokaudit.detailSerialBarang;

import java.util.List;

interface DetailSerialBarangView {
    void hideProgressbar();
    void getDataDetailSerial(List list);
    void wrongType(String string);
    void wrongResponse(String string);
    void failureResponse(String string);

    void getMoreDetailSerial(List list);
}
