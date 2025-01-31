package huce.fit.appreadstories.dialog.setting.color

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.chapter.information.ChapterInformationView
import huce.fit.appreadstories.shared_preferences.SettingSharedPreferences

class ColorDialogImpl(
    private val chapterInformationView: ChapterInformationView,
    private val colorDialogView: ColorDialogView
) : ColorDialogPresenter {
    private var settingSharedPreferences = SettingSharedPreferences(chapterInformationView as Context)
    private var textColor = "#000000"
    private var backgroundColor = "#FFFFFF"

    init {
        getSetting()
        textColor = settingSharedPreferences.getTextColor()
        backgroundColor = settingSharedPreferences.getBackgroundColor()
        setSetting()

        colorDialogView.setTextColor(textColor)
        colorDialogView.setBackgroundColor(backgroundColor)
    }

    private fun getSetting() {
        settingSharedPreferences.getSharedPreferences("Setting", Context.MODE_PRIVATE)
    }

    private fun setSetting() {
        settingSharedPreferences.setSharedPreferences("Setting", Context.MODE_PRIVATE)
    }

    override fun setTextColorPickerDialog() {
        colorDialogView.setColorPickerDialog(backgroundColor)
    }

    override fun setBackgroundColorPickerDialog() {
        colorDialogView.setColorPickerDialog(backgroundColor)
    }

    override fun viewTextColor(hexVal: String) {
        textColor = "#" + hexVal.substring(3)
        Log.e("NHT hexVal: ", hexVal)
        Log.e("NHT hexColor: ", textColor)
        colorDialogView.setTextColor(textColor)

        settingSharedPreferences.setTextColor(textColor)
        settingSharedPreferences.myApply()
    }

    override fun viewBackgroundColor(hexVal: String) {
        backgroundColor = "#" + hexVal.substring(3)
        Log.e("NHT hexVal: ", hexVal)
        Log.e("NHT hexColor: ", backgroundColor)
        colorDialogView.setBackgroundColor(backgroundColor)

        settingSharedPreferences.setBackgroundColor(backgroundColor)
        settingSharedPreferences.myApply()
    }

    override fun setTextColor() {
        chapterInformationView.setTextColor(textColor)
    }

    override fun setBackgroundColor() {
        chapterInformationView.setBackgroundColor(backgroundColor)
    }
}
