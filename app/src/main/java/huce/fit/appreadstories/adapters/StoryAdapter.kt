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

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryHolder>() {

    private var storyList: List<Story> = mutableListOf()
    private lateinit var clickListener: ClickListener
    
    class StoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStoryName: TextView = itemView.findViewById(R.id.tvStoryName)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val tvAge: TextView = itemView.findViewById(R.id.tvAge)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvChapter: TextView = itemView.findViewById(R.id.tvChapter)
        val ivStory: ImageView = itemView.findViewById(R.id.ivStory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_story_item, parent, false)
        return StoryHolder(view)
    }

    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        val story = storyList[position]
        holder.tvStoryName.text = story.storyName
        holder.tvAuthor.text = story.author
        holder.tvAge.text = story.ageLimit.toString()
        holder.tvStatus.text = story.status
        holder.tvChapter.text = story.sumChapter.toString()
        Picasso.get().load(story.image).into(holder.ivStory)
        holder.itemView.setOnClickListener { view: View ->
            clickListener.onItemClick(story.storyId, view, false)
        }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataStory(listStory: List<Story>) {
        storyList = listStory
        notifyDataSetChanged()
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }
}
