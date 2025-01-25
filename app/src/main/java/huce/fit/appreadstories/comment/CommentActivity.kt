package huce.fit.appreadstories.comment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.CommentAdapter
import huce.fit.appreadstories.model.Comment
import java.util.Locale

class CommentActivity : AppCompatActivity(), CommentView{

    private lateinit var ivBack: ImageView
    private lateinit var ivSent:ImageView
    private lateinit var etComment: EditText
    private lateinit var tvCommentLength: TextView
    private lateinit var mCommentPresenter: CommentPresenter
    private lateinit var mCommentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)
        init()
        processEvents()
    }

    fun init() {
        ivBack = findViewById(R.id.ivBack)
        ivSent = findViewById(R.id.ivSent)
        etComment = findViewById(R.id.etComment)
        tvCommentLength = findViewById(R.id.tvCommentLength)
        val rcViewComment: RecyclerView = findViewById(R.id.rcViewComment)
        val itemDecoration: ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        mCommentPresenter = CommentImpl(this)
        mCommentAdapter = CommentAdapter()
        rcViewComment.setLayoutManager(LinearLayoutManager(this))
        rcViewComment.setAdapter(mCommentAdapter)
        rcViewComment.addItemDecoration(itemDecoration)
    }

    override fun setData(commentList: MutableList<Comment>) {
        mCommentAdapter.setCommentList(commentList)
    }

    fun processEvents() {
        ivBack.setOnClickListener { finish() }
        mCommentAdapter.clickListener { position: Int, _: View?, isLongClick: Boolean ->
            if (isLongClick) {
                mCommentPresenter.checkCommentOfAccount(position)
            }
        }
        ivSent.setOnClickListener {
            val comment = etComment.getText().toString()
            if (comment.isEmpty()) {
                Toast.makeText(this@CommentActivity, "Vui lòng nhập bình luận!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                mCommentPresenter.enterSent(comment)
                etComment.setText("")
            }
        }
        etComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (etComment.lineCount > 31) {
                    Toast.makeText(
                        this@CommentActivity,
                        "Vượt quá sô dòng giới hạn đánh giá!",
                        Toast.LENGTH_SHORT
                    ).show()
                    etComment.getText()
                        .delete(etComment.getText().length - 1, etComment.getText().length)
                }
                tvCommentLength.text =
                    String.format(Locale.getDefault(), "%d/2400", s.toString().length)
            }
        })
    }

    override fun deleteComment() {
        mCommentPresenter.deleteComment()
    }

    override fun setDataUpdate() {
        etComment.setText(mCommentPresenter.getComment())
    }
}
