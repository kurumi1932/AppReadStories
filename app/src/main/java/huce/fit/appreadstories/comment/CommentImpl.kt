package huce.fit.appreadstories.comment

import android.content.Context
import android.util.Log
import android.widget.Toast
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.dialog.confirm.ConfirmEditOrDeleteCommentDialog
import huce.fit.appreadstories.model.Comment
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentImpl(private val commentView: CommentView) : CommentPresenter {

    companion object {
        private const val TAG = "CommentImpl"
    }

    private var mContext: Context = commentView as Context
    private val mCommentList: MutableList<Comment> = ArrayList()

    private var mStoryId = 0
    private var mAccountId: Int = 0
    private var mCommentId: Int = 0
    private var mAccountName: String = ""
    private var mComment: String = ""

    init {
        getStory()
        getAccount()
        getCommentList()
    }

    private fun getStory() {
        val story = StorySharedPreferences(mContext)
        story.getSharedPreferences("Story", Context.MODE_PRIVATE)
        mStoryId = story.getStoryId()
    }

    private fun getAccount() {
        val account = AccountSharedPreferences(mContext)
        account.getSharedPreferences("Account", Context.MODE_PRIVATE)
        mAccountId = account.getAccountId()
        mAccountName = account.getName()!!
    }

    override fun getCommentList() {
        Api().apiInterface().getCommentList(mStoryId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(
                call: Call<List<Comment>>,
                response: Response<List<Comment>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    mCommentList.clear()
                    mCommentList.addAll(response.body()!!)
                    commentView.setData(mCommentList)
                }
                Log.e(TAG, "api getCommentList: success")
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.e(TAG, "api getCommentList: fail")
            }
        })
    }

    override fun enterSent(comment: String) {
        if (mCommentId == 0) {
            addComment(comment)
        } else {
            updateComment(comment)
            mCommentId = 0
        }
    }

    private fun addComment(comment: String) {
        Log.e(TAG, "NHT mStoryId: $mStoryId")
        Log.e(TAG, "NHT mAccountId: $mAccountId")
        Log.e(TAG, "NHT mAccountName: $mAccountName")
        Log.e(TAG, "NHT comment: $comment")
        Api().apiInterface().addCommnet(mStoryId, mAccountId, mAccountName, comment)
            .enqueue(object : Callback<Comment> {
                override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.success == 1) {
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

    private fun updateComment(comment: String) {
        Log.e(TAG, "NHT mCommentId: $mCommentId")
        Log.e(TAG, "NHT comment: $comment")
        Api().apiInterface().updateCommnet(mCommentId, comment).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful && response.body() != null) {
                    when (response.body()!!.success) {
                        1 -> {
                            Toast.makeText(mContext, "Bình luận đã được sửa", Toast.LENGTH_SHORT)
                                .show()
                            getCommentList()
                        }

                        2 -> Toast.makeText(mContext, "Lỗi sửa bình luận!", Toast.LENGTH_SHORT)
                            .show()
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
        Log.e(TAG, "NHT commentId: $mCommentId")
        Api().apiInterface().deleteComment(mCommentId).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful && response.body() != null) {
                    when (response.body()!!.success) {
                        1 -> {
                            Toast.makeText(mContext, "Bình luận đã được xóa", Toast.LENGTH_SHORT)
                                .show()
                            getCommentList()
                        }

                        2 -> Toast.makeText(mContext, "Lỗi xóa bình luận!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.e("Err_CommentList", "deleteComment", t)
            }
        })
    }

    override fun checkCommentOfAccount(commentId: Int) {
        Log.e(TAG, "NHT checkCommentOfAccount")
        Log.e(TAG, "NHT commentId: $commentId")
        Log.e(TAG, "NHT mAccountId: $mAccountId")
        Api().apiInterface().checkCommentOfAccount(commentId, mAccountId)
            .enqueue(object : Callback<Comment> {
                override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                    if (response.isSuccessful && response.body() != null) {
                        val comment = response.body()
                        Log.e(TAG, "NHT Success: " + comment!!.success)
                        if (comment.success == 1) {
                            mCommentId = commentId
                            mComment = comment.comment
                            val confirmEditOrDeleteCommentDialog =
                                ConfirmEditOrDeleteCommentDialog(commentView, mContext)
                            confirmEditOrDeleteCommentDialog.show()
                        }
                    }
                }

                override fun onFailure(call: Call<Comment>, t: Throwable) {
                    Log.e("Err_CommentList", "checkCommentOfAccount", t)
                }
            })
    }

    override fun getComment(): String {
        return mComment
    }
}
