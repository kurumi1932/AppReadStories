package huce.fit.appreadstories.dialog.setting

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.chapter.information.ChapterInformationView
import huce.fit.appreadstories.shared_preferences.SettingSharedPreferences
import java.util.Locale

class TextAndLineStretchDialogImpl(
    private val chapterInformationView: ChapterInformationView,
    private val textAndLineStretchDialogView: TextAndLineStretchDialogView
) : TextAndLineStretchDialogPresenter {
    private var textSize = 0
    private var lineStretch = 0
    private var backgroundColor = "#FFFFFF"
    private var textColor = "#000000"
    private var settingSharedPreferences = SettingSharedPreferences(chapterInformationView as Context)

    init {
        getData()
    }

    private fun getSetting() {
        settingSharedPreferences.getSharedPreferences("Setting", Context.MODE_PRIVATE)
    }

    private fun setSetting() {
        settingSharedPreferences.setSharedPreferences("Setting", Context.MODE_PRIVATE)
    }

    private fun getData() {
        getSetting()
        textSize = settingSharedPreferences.getTextSize()
        lineStretch = settingSharedPreferences.getLineStretch()
        textAndLineStretchDialogView.getData(
            textSize.toString(), String.format(Locale.getDefault(), "%d%%", lineStretch)
        )
    }

    override fun upTextSize() {
        if (textSize in 15..39) {
            ++textSize
            Log.e("NHT textSize up: ", textSize.toString())
            setTextSize()
        }
    }

    override fun downTextSize() {
        if (textSize in 16..40) {
            --textSize
            Log.e("NHT textSize down: ", textSize.toString())
            setTextSize()
        }
    }

    private fun setTextSize() {
        chapterInformationView.setTextSize(textSize)
        textAndLineStretchDialogView.setTextSize(textSize.toString())

        setSetting()
        settingSharedPreferences.setTextSize(textSize)
        settingSharedPreferences.myApply()
    }

    override fun upLineStretch() {
        if (lineStretch in 66..299) {
            lineStretch += 5
            setLineStretch(lineStretch)
        }
    }

    override fun downLineStretch() {
        if (lineStretch in 71..304) {
            lineStretch -= 5
            setLineStretch(lineStretch)
        }
    }

    private fun setLineStretch(lineStretch: Int) {
        val lineStretchStr = String.format(Locale.getDefault(), "%d%%", lineStretch)
        textAndLineStretchDialogView.setLineStretch(lineStretchStr)

        val lineStretchFloat = lineStretch.toString().toFloat() / 100
        chapterInformationView.setLineStretch(lineStretchFloat)
        setSetting()
        settingSharedPreferences.setLineStretch(lineStretch)
        settingSharedPreferences.myApply()
    }

    override fun setColor(bgColor: Int, tColor: Int) {
        if (bgColor != 0) {
            backgroundColor = "#" + Integer.toHexString(bgColor)
        }
        if (tColor != 0) {
            textColor = "#" + Integer.toHexString(tColor)
        }

        chapterInformationView.setTextColor(textColor)
        chapterInformationView.setBackgroundColor(backgroundColor)
        setSetting()
        settingSharedPreferences.setBackgroundColor(backgroundColor)
        settingSharedPreferences.setTextColor(textColor)
        settingSharedPreferences.myApply()
    }
}
