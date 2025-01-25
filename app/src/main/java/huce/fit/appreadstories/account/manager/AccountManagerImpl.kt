package huce.fit.appreadstories.account.manager;

import android.content.Context;
import android.util.Log;

import huce.fit.appreadstories.account.BaseAccountImpl;

public class AccountManagerImpl extends BaseAccountImpl implements AccountManagerPresenter {

    private static final String TAG = "AccountManagerImpl";
    AccountManagerImpl(AccountManagerView accountManagerView, Context context) {
        super(context);
        Log.e(TAG, "AccountManagerImpl");
        accountManagerView.setName(getSharedPreferences().getName());
    }

    @Override
    public void logout() {
        setSharedPreferences().myRemove();
    }
}
