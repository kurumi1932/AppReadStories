package huce.fit.appreadstories.shared_preferences

import android.content.Context

class SettingSharedPreferences(context: Context) : BaseSharedPreferences(context) {

    fun getTextSize(): Int {
        return sharedPreferences.getInt("textSize", 18)
    }

    fun setTextSize(textSize: Int) {
        edit.putInt("textSize", textSize)
    }

    fun getLineStretch(): Int {
        return sharedPreferences.getInt("lineStretch", 75)
    }

    fun setLineStretch(lineStretch: Int) {
        edit.putInt("lineStretch", lineStretch)
    }

    fun getTextColor(): String {
        return sharedPreferences.getString("textColor", "#000000").toString()
    }

    fun setTextColor(textColor: String) {
        edit.putString("textColor", textColor)
    }

    fun getBackgroundColor(): String {
        return sharedPreferences.getString("backgroundColor", "#FFFFFF").toString()
    }

    fun setBackgroundColor(backgroundColor: String) {
        edit.putString("backgroundColor", backgroundColor)
    }
}
