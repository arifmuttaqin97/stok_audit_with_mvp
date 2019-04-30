package com.mvp.stokaudit.login;

import com.google.gson.internal.LinkedTreeMap;

interface LoginView {
    void userFound();

    void emptyField();

    void login(String user, String pass);

    void getDataLogin(LinkedTreeMap linkedTreeMap);

    void wrongType(String string);

    void wrongResponse(String string);

    void failureResponse(String string);
}
