package huce.fit.appreadstories.account

import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences

interface BaseAccountPresenter {

    fun getAccount(): AccountSharedPreferences
    fun setAccount(): AccountSharedPreferences
    fun age(birthday: String): Int
}
