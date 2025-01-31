package huce.fit.appreadstories.dialog.confirm

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.dialog.BaseDialog

open class BaseConfirmDialog(context: Context) : BaseDialog(context), BaseConfirmDialogView {
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    lateinit var tvYes: TextView
    lateinit var tvNo: TextView

    init {
        setDialog(R.layout.dialog_confirm)
        setWindow(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0
        )
        init()
    }

    private fun init() {
        tvTitle = dialog.findViewById(R.id.tvTitle)
        tvContent = dialog.findViewById(R.id.tvContent)
        tvYes = dialog.findViewById(R.id.tvYes)
        tvNo = dialog.findViewById(R.id.tvNo)
    }

    override fun setContent(title: String, content: String) {
        tvTitle.text = title
        tvContent.text = content
    }
}
