package huce.fit.appreadstories.dialog.confirm

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.comment.CommentView
import huce.fit.appreadstories.dialog.BaseDialog

class ConfirmEditOrDeleteCommentDialog(private val commentView: CommentView) :
    BaseDialog(commentView as Context) {
        
    private lateinit var tvUpdate: TextView
    private lateinit var tvDelete: TextView

    init {
        setDialog(R.layout.dialog_comment_update_delete)
        setWindow(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0
        )
        init()
        processEvents()
    }

    private fun init() {
        dialog.setCancelable(false)

        tvUpdate = dialog.findViewById(R.id.tvUpdate)
        tvDelete = dialog.findViewById(R.id.tvDelete)
    }

    private fun processEvents() {
        tvUpdate.setOnClickListener {
            commentView.setDataUpdate()
            dismiss()
        }
        tvDelete.setOnClickListener {
            val confirmDeleteComment = ConfirmDeleteComment(commentView)
            confirmDeleteComment.show()
            dismiss()
        }
    }
}
