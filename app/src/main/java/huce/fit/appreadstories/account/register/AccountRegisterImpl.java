package huce.fit.appreadstories.account.register;

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

public class AccountRegisterImpl extends BaseAccountImpl implements AccountRegisterPresenter {

    private static final String TAG = "AccountRegisterImpl";
    private AccountRegisterView mAccountRegisterView;
    private final Calendar mCalendar = Calendar.getInstance();
    private final DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        @SuppressLint("SimpleDateFormat") String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(mCalendar.getTime());
        mAccountRegisterView.changeDatePicker(dateStr);
    };

    AccountRegisterImpl(AccountRegisterView accountRegisterView) {
        super((Context) accountRegisterView);
        mAccountRegisterView = accountRegisterView;
    }

    @Override
    public void register(String username, String password, String email, String name, String birthday) {
        if (checkDate(birthday))
            if (isNetwork())
                Api.apiInterface().register(username, password, email, name, birthday).enqueue(new Callback<TaiKhoan>() {
                    @Override
                    public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                        if (response.isSuccessful() && response.body() != null)
                            switch (response.body().getAccountsuccess()) {
                                case 0:
                                    mAccountRegisterView.register(0);
                                    break;
                                case 1:
                                    setSharedPreferences(username);
                                    mAccountRegisterView.register(1);
                                    break;
                            }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                        Log.e(TAG, "register_err", t);
                    }
                });
            else
                mAccountRegisterView.register(2);
        else
            mAccountRegisterView.register(3);
    }

    @Override
    public void openDatePicker() {
        new DatePickerDialog((Context) mAccountRegisterView, d,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setSharedPreferences(String username) {
        MySharedPreferences mySharedPreferences = setSharedPreferences();
        mySharedPreferences.setUsername(username);
        mySharedPreferences.myApply();
    }
}
