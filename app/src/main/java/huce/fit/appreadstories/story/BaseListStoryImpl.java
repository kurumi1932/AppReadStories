package huce.fit.appreadstories.story;

import android.content.Context;

import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.shared_preferences.MySharedPreferences;
import huce.fit.appreadstories.shared_preferences.MySharedPreferencesImpl;

public class BaseListStoryImpl implements BaseListStoryPresenter {

    private final static String TAG = "BaseListStoryImpl";
    private final CheckNetwork mCheckNetwork;
    private final Context mContext;

    public BaseListStoryImpl(Context context) {
        mContext = context;
        mCheckNetwork = new CheckNetwork(mContext);
    }

    public MySharedPreferences getSharedPreferences() {
        MySharedPreferences mySharedPreferences = new MySharedPreferencesImpl(mContext);
        mySharedPreferences.getMySharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        return mySharedPreferences;
    }

    @Override
    public boolean isNetwork() {
        return mCheckNetwork.isNetwork();
    }
}
