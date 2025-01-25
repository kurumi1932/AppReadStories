package huce.fit.appreadstories.startapp;

import android.content.Context;
import android.util.Log;

import huce.fit.appreadstories.account.BaseAccountImpl;
import huce.fit.appreadstories.shared_preferences.MySharedPreferences;

public class StartAppImpl extends BaseAccountImpl implements StartAppPresenter {

    private static final String TAG="StartAppImpl";
    StartAppImpl(Context context) {
        super(context);
    }

    @Override
    public boolean checkLogged() {
        MySharedPreferences mMySharedPreferences = getSharedPreferences();
        int idAccount = mMySharedPreferences.getIdAccount();
        String birthday = mMySharedPreferences.getBirthday();
        Log.e(TAG, "birthday= "+birthday);

        if (idAccount != 0) {
            mMySharedPreferences = setSharedPreferences();
            mMySharedPreferences.setAge(age(birthday));
            mMySharedPreferences.myApply();
            return true;
        }
        return false;
    }
}
