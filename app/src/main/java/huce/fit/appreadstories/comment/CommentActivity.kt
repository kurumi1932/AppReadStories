package huce.fit.appreadstories.comment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.CommentAdapter
import huce.fit.appreadstories.model.Comment
import huce.fit.appreadstories.util.AppUtil
import java.util.Locale

class CommentActivity : AppCompatActivity(), CommentView{

    private lateinit var ivBack: ImageView
    private lateinit var ivSent:ImageView
    private lateinit var etComment: EditText
    private lateinit var tvCommentLength: TextView
    private lateinit var commentPresenter: CommentPresenter
    private lateinit var commentAdapter: CommentAdapter

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

        commentPresenter = CommentImpl(this)
        commentAdapter = CommentAdapter()
        rcViewComment.setLayoutManager(LinearLayoutManager(this))
        rcViewComment.setAdapter(commentAdapter)
        rcViewComment.addItemDecoration(itemDecoration)
    }

    override fun setData(commentList: MutableList<Comment>) {
        commentAdapter.setCommentList(commentList)
    }

    fun processEvents() {
        ivBack.setOnClickListener { finish() }
        commentAdapter.clickListener { position: Int, _: View?, isLongClick: Boolean ->
            if (isLongClick) {
                commentPresenter.checkCommentOfAccount(position)
            }
        }
        ivSent.setOnClickListener {
            val comment = etComment.getText().toString()
            if (comment.isEmpty()) {
                AppUtil.setToast(this, "Vui lòng nhập bình luận!")
            } else {
                commentPresenter.enterSent(comment)
                etComment.setText("")
            }
        }
        etComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (etComment.lineCount > 31) {
                    AppUtil.setToast(this@CommentActivity, "Vượt quá sô dòng giới hạn đánh giá!")
                    etComment.getText()
                        .delete(etComment.getText().length - 1, etComment.getText().length)
                }
                tvCommentLength.text =
                    String.format(Locale.getDefault(), "%d/2400", s.toString().length)
            }
        })
    }

    override fun deleteComment() = commentPresenter.deleteComment()


    override fun setDataUpdate() = etComment.setText(commentPresenter.getComment())
}
