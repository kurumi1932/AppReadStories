package huce.fit.appreadstories.account.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import huce.fit.appreadstories.account.BaseAccountImpl
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Account
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class AccountRegisterImpl(var accountRegisterView: AccountRegisterView) : BaseAccountImpl(accountRegisterView as Context), AccountRegisterPresenter {

    companion object{
        const val TAG = "AccountRegisterImpl"
    }

    val d = OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        @SuppressLint("SimpleDateFormat") val dateStr =
            SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        accountRegisterView.changeDatePicker(dateStr)
    }

    override fun register(
        username: String,
        password: String,
        email: String,
        name: String,
        birthday: String
    ) {
        if (checkDate(birthday)) {
            if (isConnecting(context)) {
                Api().apiInterface().register(username, password, email, name, birthday)
                    .enqueue(object : Callback<Account> {
                        override fun onResponse(call: Call<Account>, response: Response<Account>) {
                            if (response.isSuccessful && response.body() != null) {
                                when (response.body()!!.success) {
                                    0 -> accountRegisterView.register(0)
                                    1 -> {
                                        setAccount(username)
                                        accountRegisterView.register(1)
                                    }
                                }
                            }
                            Log.e(TAG, "api register: success")
                        }

                        override fun onFailure(call: Call<Account>, t: Throwable) {
                            Log.e(TAG, "api register: fail")
                        }
                    })
            } else {
                accountRegisterView.register(2)
            }
        } else {
            accountRegisterView.register(3)
        }
    }

    override fun openDatePicker() {
        DatePickerDialog(
            context, d,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun setAccount(username: String) {
        val account: AccountSharedPreferences = setAccount()
        account.setUsername(username)
        account.myApply()
    }
}
