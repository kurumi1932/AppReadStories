package huce.fit.appreadstories.dialog.setting

interface TextAndLineStretchDialogView {
    fun getData(fontSize: String, lineStretch: String)
    fun setTextSize(textSize: String)
    fun setLineStretch(lineStretch: String)
}
