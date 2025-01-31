package huce.fit.appreadstories.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.model.Comment

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    private var commentList: MutableList<Comment> = mutableListOf()
    private var clickListener: ClickListener? = null

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvComment: TextView = itemView.findViewById(R.id.tvComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_comment_item, parent, false)
        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val comment = commentList[position]
        holder.tvName.text = comment.displayName
        holder.tvComment.text = comment.commentContent
        holder.itemView.setOnClickListener {    //rút ngắn 1 đoạn bình luận dài
            holder.tvComment.setMaxLines(if (holder.tvComment.lineCount > 4) 4 else 31)
        }
        holder.itemView.setOnLongClickListener { view: View ->
            clickListener!!.onItemClick(comment.commentId, view, true)
            false
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun clickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCommentList(commentList: MutableList<Comment>) {
        this.commentList = commentList
        notifyDataSetChanged()
    }
}
