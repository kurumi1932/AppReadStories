package huce.fit.appreadstories.comment

interface CommentPresenter {

    fun getCommentList()
    fun enterSent(comment: String)
    fun deleteComment()
    fun checkCommentOfAccount(commentId: Int)
    fun getComment(): String
}