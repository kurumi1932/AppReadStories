package huce.fit.appreadstories.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window

open class BaseDialog(val context: Context) : BaseDialogView {

    val dialog = Dialog(context)

    fun setDialog(layoutId: Int) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutId)
        // true click bên ngoài dialog có thể tắt dialog
//        dialog.setCancelable(false);
    }

    final override fun setWindow(width: Int, height: Int, gravity: Int, animation: Int) {
        val window = dialog.window ?: return

        //chiều dài, chiều cao dialog
        window.setLayout(width, height)
        //background
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // vị trí dialog
        val windowAttributes = window.attributes
        windowAttributes.gravity = gravity
        if (animation != 0) {
            windowAttributes.windowAnimations = animation
        }
        window.attributes = windowAttributes
    }

    override fun show() = dialog.show()

    override fun dismiss() = dialog.dismiss()
}
