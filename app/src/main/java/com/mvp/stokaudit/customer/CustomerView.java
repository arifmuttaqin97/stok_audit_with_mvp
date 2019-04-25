package com.mvp.stokaudit.customer;

import java.util.List;

interface CustomerView {
    void hideProgressbar();
    void getDataCustomer(List list);
    void wrongType(String string);
    void wrongResponse(String string);
    void failureResponse(String string);

    void getMoreCustomer(List list);
}
