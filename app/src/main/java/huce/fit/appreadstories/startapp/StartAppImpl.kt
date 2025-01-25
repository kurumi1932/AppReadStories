package huce.fit.appreadstories.startapp

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.account.BaseAccountImpl
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences

class StartAppImpl(context: Context) : BaseAccountImpl(context), StartAppPresenter {

    companion object {
        private const val TAG = "StartAppImpl"
    }

    override fun checkLogged(): Boolean {
        var account: AccountSharedPreferences = getAccount()
        val birthday = account.getBirthday().toString()
        Log.e(TAG, "NHT birthday: $birthday")
        if (account.getAccountId() != 0) {
            account = setAccount()
            account.setAge(age(birthday))
            account.myApply()
            return true
        }
        return false
    }
}
