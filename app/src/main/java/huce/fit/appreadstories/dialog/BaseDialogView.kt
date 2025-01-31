package huce.fit.appreadstories.dialog

interface BaseDialogView {
    fun setWindow(width: Int, height: Int, gravity: Int, animation: Int)
    fun show()
    fun dismiss()
}
