package huce.fit.appreadstories.account.update;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import huce.fit.appreadstories.account.BaseAccountImpl;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.TaiKhoan;
import huce.fit.appreadstories.shared_preferences.MySharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountUpdateImpl extends BaseAccountImpl implements AccountUpdatePresenter {

    private static final String TAG = "AccountUpdateImpl";
    private AccountUpdateView mAccountUpdateView;
    private final Calendar mCalendar = Calendar.getInstance();
    private final DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        @SuppressLint("SimpleDateFormat") String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(mCalendar.getTime());
        mAccountUpdateView.changeDatePicker(dateStr);
    };

    public AccountUpdateImpl(AccountUpdateView accountUpdateView) {
        super((Context) accountUpdateView);
        mAccountUpdateView = accountUpdateView;
        MySharedPreferences mySharedPreferences = getSharedPreferences();
        mAccountUpdateView.getInfoAccount(mySharedPreferences.getName(), mySharedPreferences.getEmail(), mySharedPreferences.getBirthday());
    }

    @Override
    public void updateAccount(int idAccount, String email, String name, String birthday) {
        if (checkDate(birthday))
            if (isNetwork())
                Api.apiInterface().updateAccount(idAccount, email, name, birthday).enqueue(new Callback<TaiKhoan>() {
                    @Override
                    public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                        if (response.isSuccessful() && response.body() != null)
                            if (response.body().getAccountsuccess() == 1) {
                                setSharedPreferences(email, name, birthday, age(birthday));
                                mAccountUpdateView.update(1);
                            } else {
                                mAccountUpdateView.update(0);
                            }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                        Log.e(TAG, "update_info_err", t);
                    }
                });
            else
                mAccountUpdateView.update(2);
        else
            mAccountUpdateView.update(3);
    }

    @Override
    public void changePassword(int idAccount, String oldPass, String newPass) {
        if (isNetwork())
            Api.apiInterface().checkPassword(idAccount, oldPass).enqueue(new Callback<TaiKhoan>() {
                @Override
                public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                    if (response.isSuccessful() && response.body() != null)
                        if (response.body().getAccountsuccess() == 1)
                            if (oldPass.equals(newPass))
                                mAccountUpdateView.update(4);
                            else
                                Api.apiInterface().updatePassword(idAccount, newPass).enqueue(new Callback<TaiKhoan>() {
                                    @Override
                                    public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                                        if (response.isSuccessful() && response.body() != null)
                                            if (response.body().getAccountsuccess() == 1)
                                                mAccountUpdateView.update(1);
                                            else
                                                mAccountUpdateView.update(5);
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                                        Log.e(TAG, "update_pass_err", t);
                                    }
                                });
                        else
                            mAccountUpdateView.update(6);
                }

                @Override
                public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                    Log.e(TAG, "check_pass_err", t);
                }
            });
        else
            mAccountUpdateView.update(2);
    }

    @Override
    public void openDatePicker() {
        new DatePickerDialog((Context) mAccountUpdateView, d,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setSharedPreferences(String email, String name, String birthday, int age) {
        MySharedPreferences mySharedPreferences = setSharedPreferences();
        mySharedPreferences.setEmail(email);
        mySharedPreferences.setName(name);
        mySharedPreferences.setBirthday(birthday);
        mySharedPreferences.setAge(age);
        mySharedPreferences.myApply();
    }
}
