package huce.fit.appreadstories.dialog.setting.color

interface ColorDialogPresenter {
    fun setTextColorPickerDialog()
    fun setBackgroundColorPickerDialog()
    fun viewTextColor(hexVal: String)
    fun viewBackgroundColor(hexVal: String)
    fun setTextColor()
    fun setBackgroundColor()
}
