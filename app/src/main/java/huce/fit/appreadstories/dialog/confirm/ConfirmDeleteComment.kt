package huce.fit.appreadstories.dialog.confirm

import android.content.Context
import huce.fit.appreadstories.comment.CommentView

class ConfirmDeleteComment internal constructor(private val commentView: CommentView) :
    BaseConfirmDialog(commentView as Context) {

    init {
        init()
        processEvents()
    }

    private fun init() {
        dialog.setCancelable(false)
        val title = "Xóa bình luận"
        val content = "\"Bạn có muốn xóa bình luận không?"
        setContent(title, content)
    }

    private fun processEvents() {
        tvYes.setOnClickListener {
            commentView.deleteComment()
            dismiss()
        }
        tvNo.setOnClickListener { dismiss() }
    }
}
