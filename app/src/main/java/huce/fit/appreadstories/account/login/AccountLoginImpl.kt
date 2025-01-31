package huce.fit.appreadstories.account.login

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.account.BaseAccountImpl
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Account
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountLoginImpl(val accountLoginView: AccountLoginView) :
    BaseAccountImpl(accountLoginView as Context), AccountPresenter {

    companion object {
        const val TAG = "AccountImpl"
    }

    override fun login(username: String, password: String) {
        if (isConnecting(context)) {
            Log.e(TAG, "api login: username: $username password: $password")
            Api().apiInterface().login(username, password).enqueue(object : Callback<Account> {
                override fun onResponse(call: Call<Account>, response: Response<Account>) {
                    val account = response.body()
                    if (response.isSuccessful && account != null) {
                        if (account.accountSuccess == 1) {
                            setAccountSharedPreferences(
                                account.accountId, account.displayName, account.email,
                                account.birthday, age(account.birthday)
                            )
                            accountLoginView.login(1)
                        } else {
                            accountLoginView.login(0)
                        }
                    }
                    Log.e(TAG, "api login: success")
                }

                override fun onFailure(call: Call<Account>, t: Throwable) {
                    Log.e(TAG, "api login: fail \n$t")
                }
            })
        } else {
            accountLoginView.login(2)
        }
    }

    private fun setAccountSharedPreferences(
        accountId: Int, name: String, email: String, birthday: String, age: Int
    ) {
        val account = setAccount()
        account.setAccountId(accountId)
        account.setName(name)
        account.setEmail(email)
        account.setBirthday(birthday)
        account.setAge(age)
        account.myApply()
    }
}
