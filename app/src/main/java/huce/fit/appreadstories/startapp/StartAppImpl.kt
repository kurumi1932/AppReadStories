package huce.fit.appreadstories.startapp

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.account.BaseAccountImpl
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences

class StartAppImpl(context: Context) : BaseAccountImpl(context), StartAppPresenter {

    override fun checkLogged(): Boolean {
        var account = getAccount()
        val birthday = account.getBirthday().toString()
        if (account.getAccountId() != 0) {
            account = setAccount()
            account.setAge(age(birthday))
            account.myApply()
            return true
        }
        return false
    }
}
