package huce.fit.appreadstories.dialog.setting

interface TextAndLineStretchDialogPresenter {
    fun upTextSize()
    fun downTextSize()
    fun upLineStretch()
    fun downLineStretch()
    fun setColor(backgroundColor: Int, textColor: Int)
}
