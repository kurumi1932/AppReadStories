package huce.fit.appreadstories.comment

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.dialog.confirm.ConfirmEditOrDeleteCommentDialog
import huce.fit.appreadstories.model.Comment
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.util.AppUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentImpl(private val commentView: CommentView) : CommentPresenter {

    companion object {
        private const val TAG = "CommentImpl"
    }

    private var context = commentView as Context
    private val commentList: MutableList<Comment> = ArrayList()

    private var storyId = 0
    private var accountId = 0
    private var commentId = 0
    private var accountName = ""
    private var comment = ""

    init {
        getStory()
        getAccount()
        getCommentList()
    }

    private fun getStory() {
        val story = StorySharedPreferences(context)
        story.getSharedPreferences("Story", Context.MODE_PRIVATE)
        storyId = story.getStoryId()
    }

    private fun getAccount() {
        val account = AccountSharedPreferences(context)
        account.getSharedPreferences("Account", Context.MODE_PRIVATE)
        accountId = account.getAccountId()
        accountName = account.getName()
    }

    override fun getCommentList() {
        Api().apiInterface().getCommentList(storyId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                val commentListServer = response.body()
                if (response.isSuccessful && commentListServer != null) {
                    commentList.clear()
                    commentList.addAll(commentListServer)
                    commentView.setData(commentList)
                }
                Log.e(TAG, "api getCommentList: success")
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.e(TAG, "api getCommentList: fail")
            }
        })
    }

    override fun enterSent(comment: String) {
        if (commentId == 0) {
            addComment(comment)
        } else {
            updateComment(comment)
            commentId = 0
        }
    }

    private fun addComment(commentContent: String) {
        Log.e(TAG, "NHT storyId: $storyId")
        Log.e(TAG, "NHT accountId: $accountId")
        Log.e(TAG, "NHT accountName: $accountName")
        Log.e(TAG, "NHT comment: $commentContent")
        Api().apiInterface().addCommnet(storyId, accountId, accountName, commentContent)
            .enqueue(object : Callback<Comment> {
                override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                    val commentServer = response.body()
                    if (response.isSuccessful && commentServer != null) {
                        if (commentServer.commentSuccess == 1) {
                            getCommentList()
                        }
                    }
                    Log.e(TAG, "api addComment: success")
                }

                override fun onFailure(call: Call<Comment>, t: Throwable) {
                    Log.e(TAG, "api addComment: fail")
                }
            })
    }

    private fun updateComment(commentContent: String) {
        Log.e(TAG, "NHT commentId: $commentId")
        Log.e(TAG, "NHT comment: $commentContent")
        Api().apiInterface().updateCommnet(commentId, commentContent).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                val commentServer = response.body()
                if (response.isSuccessful && commentServer != null) {
                    when (commentServer.commentSuccess) {
                        1 -> {
                            AppUtil.setToast(context, "Bình luận đã được sửa")
                            getCommentList()
                        }

                        2 -> AppUtil.setToast(context, "Lỗi sửa bình luận!")
                    }
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e("Err_CommentList", "updateComment", t)
            }
        })
    }

    override fun deleteComment() {
        Log.e(TAG, "NHT deleteComment")
        Log.e(TAG, "NHT commentId: $commentId")
        Api().apiInterface().deleteComment(commentId).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                val commentServer = response.body()
                if (response.isSuccessful && commentServer != null) {
                    when (commentServer.commentSuccess) {
                        1 -> {
                            AppUtil.setToast(context, "Bình luận đã được xóa")
                            getCommentList()
                        }

                        2 -> AppUtil.setToast(context, "Lỗi xóa bình luận!")
                    }
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e("Err_CommentList", "deleteComment", t)
            }
        })
    }

    override fun checkCommentOfAccount(cId: Int) {
        Log.e(TAG, "NHT checkCommentOfAccount")
        Log.e(TAG, "NHT commentId: $cId")
        Log.e(TAG, "NHT accountId: $accountId")
        Api().apiInterface().checkCommentOfAccount(cId, accountId)
            .enqueue(object : Callback<Comment> {
                override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                    val commentServer = response.body()
                    if (response.isSuccessful && commentServer != null) {
                        Log.e(TAG, "NHT Success: " + commentServer.commentSuccess)
                        if (commentServer.commentSuccess == 1) {
                            commentId = cId
                            comment = commentServer.commentContent
                            val confirmEditOrDeleteCommentDialog =
                                ConfirmEditOrDeleteCommentDialog(commentView)
                            confirmEditOrDeleteCommentDialog.show()
                        }
                    }
                }

                override fun onFailure(call: Call<Comment>, t: Throwable) {
                    Log.e("Err_CommentList", "checkCommentOfAccount", t)
                }
            })
    }

    override fun getComment() = comment
}
