package com.mvp.stokaudit.listBarang;

import java.util.List;

interface ListBarangView {
    void hideProgressbar();
    void getDataMaster(List list);
    void wrongType(String string);
    void wrongResponse(String string);
    void failureResponse(String string);

    void getMoreMaster(List list);
}
