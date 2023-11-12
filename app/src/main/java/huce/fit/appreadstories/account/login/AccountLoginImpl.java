package huce.fit.appreadstories.account.login;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import huce.fit.appreadstories.account.BaseAccountImpl;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.TaiKhoan;
import huce.fit.appreadstories.shared_preferences.MySharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountLoginImpl extends BaseAccountImpl implements AccountPresenter{

    private static final String TAG = "AccountImpl";
    private final AccountLoginView mAccountLoginView;

    AccountLoginImpl(AccountLoginView accountLoginView) {
        super((Context) accountLoginView);
        mAccountLoginView = accountLoginView;
    }

    @Override
    public void login(String username, String password) {
        if (isNetwork()) {
            Api.apiInterface().login(username, password).enqueue(new Callback<TaiKhoan>() {
                @Override
                public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        TaiKhoan tk = response.body();
                        if (tk.getAccountsuccess() == 1) {
                            setSharedPreferences(tk.getMataikhoan(), tk.getTenhienthi(), tk.getEmail(), tk.getNgaysinh(), age(tk.getNgaysinh()));
                            mAccountLoginView.login(1);
                        } else {
                            mAccountLoginView.login(0);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                    Log.e(TAG, "login_err", t);
                }
            });
        } else {
            mAccountLoginView.login(2);
        }
    }

    public void setSharedPreferences(int idAccount, String name, String email, String birthDay, int age) {
        MySharedPreferences mySharedPreferences = setSharedPreferences();
        mySharedPreferences.setIdAccount(idAccount);
        mySharedPreferences.setName(name);
        mySharedPreferences.setEmail(email);
        mySharedPreferences.setBirthday(birthDay);
        mySharedPreferences.setAge(age);
        mySharedPreferences.myApply();
    }
}
