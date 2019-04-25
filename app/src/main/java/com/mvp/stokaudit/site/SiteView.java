package com.mvp.stokaudit.site;

import java.util.List;

interface SiteView {
    void hideProgressbar();
    void getDataSite(List list);
    void wrongType(String string);
    void wrongResponse(String string);
    void failureResponse(String string);

    void getMoreSite(List list);
}
