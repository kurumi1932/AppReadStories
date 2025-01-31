package huce.fit.appreadstories.comment

import huce.fit.appreadstories.model.Comment

interface CommentView {
    fun setData(commentList: MutableList<Comment>)
    fun deleteComment()
    fun setDataUpdate()
}