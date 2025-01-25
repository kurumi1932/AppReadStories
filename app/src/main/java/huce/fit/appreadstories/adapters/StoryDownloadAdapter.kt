package huce.fit.appreadstories.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import huce.fit.appreadstories.R
import huce.fit.appreadstories.model.Story
import java.util.Locale

class StoryDownloadAdapter: RecyclerView.Adapter<StoryDownloadAdapter.StoryDownloadHolder>() {

    class StoryDownloadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val tvStoryName: TextView = itemView.findViewById(R.id.tvStoryName)
         val tvAuthor: TextView= itemView.findViewById(R.id.tvAuthor)
         val tvAge: TextView= itemView.findViewById(R.id.tvAge)
         val tvStatus: TextView= itemView.findViewById(R.id.tvStatus)
         val tvChapter: TextView= itemView.findViewById(R.id.tvChapter)
         val tvNewChapter: TextView= itemView.findViewById(R.id.tvNewChapter)
         val ivStory: ImageView= itemView.findViewById(R.id.ivStory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryDownloadHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_story_item, parent, false)
        return StoryDownloadHolder(view)
    }

    override fun onBindViewHolder(holder: StoryDownloadHolder, position: Int) {
        val story = mStoryList[position]
        holder.itemView.setOnClickListener { view: View ->
            mClickListener!!.onItemClick(story.storyId, view, false)
        }
        holder.itemView.setOnLongClickListener { view: View ->
            mClickListener!!.onItemClick(story.storyId, view, true)
            false
        }
        holder.tvStoryName.text = story.storyName
        holder.tvAuthor.text = story.author
        holder.tvAge.text = story.ageLimit.toString()
        holder.tvStatus.text = story.status
        holder.tvChapter.text = story.sumChapter.toString()
        holder.tvNewChapter.text =
            String.format(Locale.getDefault(), "Có %d chương mới chưa tải", story.newChapter)
        holder.tvNewChapter.visibility = if (story.newChapter > 0) View.VISIBLE else View.GONE
        Picasso.get().load(story.image).into(holder.ivStory)
    }

    override fun getItemCount(): Int {
        return mStoryList.size
    }

    private var mStoryList: MutableList<Story> = mutableListOf()
    private var mClickListener: ClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataStory(storyList: MutableList<Story>) {
        mStoryList = storyList
        notifyDataSetChanged()
    }

    fun setClickListener(clickListener: ClickListener?) {
        mClickListener = clickListener
    }
}
