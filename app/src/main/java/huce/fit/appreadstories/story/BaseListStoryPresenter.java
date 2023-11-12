package huce.fit.appreadstories.story;

import huce.fit.appreadstories.shared_preferences.MySharedPreferences;

public interface BaseListStoryPresenter {

    boolean isNetwork();
    MySharedPreferences getSharedPreferences();
}
