package huce.fit.appreadstories.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MySharedPreferencesImpl implements MySharedPreferences {

    private static final String TAG = "MySharedPreferencesImpl";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    Context mContext;

    public MySharedPreferencesImpl(Context context) {
        Log.e(TAG, "context= "+context);
        mContext = context;
    }

    @Override
    public void getMySharedPreferences(String file, int modePrivate) {
        sharedPreferences = mContext.getSharedPreferences(file, modePrivate);
    }

    @Override
    public void setMySharedPreferences(String file, int modePrivate) {
        getMySharedPreferences(file, modePrivate);
        edit = sharedPreferences.edit();
    }

    @Override
    public void myApply() {
        edit.apply();
    }

    @Override
    public void myRemove() {
        edit.clear().apply();
    }

    @Override
    public int getIdAccount() {
        return sharedPreferences.getInt("idAccount", 0);
    }

    @Override
    public void setIdAccount(int idAccount) {
        edit.putInt("idAccount", idAccount);
    }

    @Override
    public String getUsername() {
        return sharedPreferences.getString("username", "");
    }

    @Override
    public void setUsername(String username) {
        edit.putString("username", username);
    }

    @Override
    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    @Override
    public void setEmail(String email) {
        edit.putString("email", email);
    }

    @Override
    public String getName() {
        return sharedPreferences.getString("name", "");
    }

    @Override
    public void setName(String name) {
        edit.putString("name", name);
    }

    @Override
    public String getBirthday() {
        return sharedPreferences.getString("birthday", "");
    }

    @Override
    public void setBirthday(String birthday) {
        edit.putString("birthday", birthday);
    }

    @Override
    public int getAge() {
        return sharedPreferences.getInt("age", 0);
    }

    @Override
    public void setAge(int age) {
        edit.putInt("age", age);
    }
}
