package huce.fit.appreadstories.shared_preferences

import android.content.Context
import android.content.SharedPreferences

open class BaseSharedPreferences(val context: Context) {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var edit: SharedPreferences.Editor

    fun getSharedPreferences(file: String, modePrivate: Int) {
        sharedPreferences = context.getSharedPreferences(file, modePrivate)
    }

    fun setSharedPreferences(file: String, modePrivate: Int) {
        sharedPreferences = context.getSharedPreferences(file, modePrivate)
        edit = sharedPreferences.edit()
    }

    fun myApply() {
        edit.apply()
    }

    fun myRemove() {
        edit.clear().apply()
    }
}
