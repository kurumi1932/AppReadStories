package huce.fit.appreadstories.account;

import huce.fit.appreadstories.shared_preferences.MySharedPreferences;

public interface BaseAccountPresenter {

    boolean isNetwork();
    MySharedPreferences getSharedPreferences();
    MySharedPreferences setSharedPreferences();

    int age(String birthday);
}
