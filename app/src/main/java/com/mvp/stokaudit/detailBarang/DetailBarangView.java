package com.mvp.stokaudit.detailBarang;

import java.util.List;

interface DetailBarangView {
    void hideProgressbar();

    void getDataDetailBarang(List list);

    void wrongType(String string);

    void wrongResponse(String string);

    void failureResponse(String string);

    void getMoreDataBarang(List list);
}
