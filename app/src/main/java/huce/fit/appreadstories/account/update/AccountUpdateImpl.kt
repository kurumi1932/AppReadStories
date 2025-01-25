package huce.fit.appreadstories.account.update

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

class AccountUpdateImpl(private var accountUpdateView: AccountUpdateView) : BaseAccountImpl(accountUpdateView as Context), AccountUpdatePresenter {

    companion object{
        const val TAG = "AccountUpdateImpl"
    }

    val d = OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        @SuppressLint("SimpleDateFormat") val dateStr =
            SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        accountUpdateView.changeDatePicker(dateStr)
    }

    init {
        setAccountInfo()
    }

    private fun setAccountInfo() {
        val account: AccountSharedPreferences = getAccount()
        accountUpdateView.getInfoAccount(
            account.getName()!!,
            account.getEmail()!!,
            account.getBirthday()!!
        )
    }

    override fun updateAccount(accountId: Int, email: String, name: String, birthday: String) {
        if (!checkDate(birthday)) {
            accountUpdateView.update(3)
            return
        }
        if (!isConnecting(context)) {
            accountUpdateView.update(2)
            return
        }
        Api().apiInterface().updateAccount(accountId, email, name, birthday)
            .enqueue(object : Callback<Account?> {
                override fun onResponse(call: Call<Account?>, response: Response<Account?>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.success == 1) {
                            setAccount(email, name, birthday, age(birthday))
                            accountUpdateView.update(1)
                        } else {
                            accountUpdateView.update(0)
                        }
                    }
                    Log.e(TAG, "api updateAccount: success")
                }

                override fun onFailure(call: Call<Account?>, t: Throwable) {
                    Log.e(TAG, "api updateAccount: fail")
                }
            })
    }

    override fun changePassword(accountId: Int, oldPass: String, newPass: String) {
        if (!isConnecting(context)) {
            accountUpdateView.update(2)
            return
        }
        Api().apiInterface().checkPassword(accountId, oldPass).enqueue(object : Callback<Account?> {
            override fun onResponse(call: Call<Account?>, response: Response<Account?>) {
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.success == 1) {
                        if (oldPass == newPass) {
                            accountUpdateView.update(4)
                        } else {
                            Api().apiInterface().updatePassword(accountId, newPass)
                                .enqueue(object : Callback<Account?> {
                                    override fun onResponse(
                                        call: Call<Account?>,
                                        response: Response<Account?>
                                    ) {
                                        if (response.isSuccessful && response.body() != null) {
                                            if (response.body()!!.success == 1) {
                                                accountUpdateView.update(1)
                                            } else {
                                                accountUpdateView.update(5)
                                            }
                                        }
                                        Log.e(TAG, "api changePassword update: success")
                                    }

                                    override fun onFailure(call: Call<Account?>, t: Throwable) {
                                        Log.e(TAG, "api changePassword update: fail")
                                    }
                                })
                        }
                    } else {
                        accountUpdateView.update(6)
                    }
                }
                Log.e(TAG, "api changePassword check: success")
            }

            override fun onFailure(call: Call<Account?>, t: Throwable) {
                Log.e(TAG, "api changePassword check: fail")
            }
        })
    }

    override fun openDatePicker() {
        DatePickerDialog(
            (accountUpdateView as Context), d,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun setAccount(email: String, name: String, birthday: String, age: Int) {
        val account: AccountSharedPreferences = setAccount()
        account.setEmail(email)
        account.setName(name)
        account.setBirthday(birthday)
        account.setAge(age)
        account.myApply()
    }
}
