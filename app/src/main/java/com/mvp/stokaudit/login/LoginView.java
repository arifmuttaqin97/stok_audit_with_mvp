package com.mvp.stokaudit.login;

import com.google.gson.internal.LinkedTreeMap;

interface LoginView {
    void getDataLogin(LinkedTreeMap linkedTreeMap);
    void wrongType(String string);
    void wrongResponse(String string);
    void failureResponse(String string);
}
