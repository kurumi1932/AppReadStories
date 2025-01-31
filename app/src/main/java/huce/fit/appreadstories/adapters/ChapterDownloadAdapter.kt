package huce.fit.appreadstories.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.model.Chapter
import java.util.Locale

class ChapterDownloadAdapter : RecyclerView.Adapter<ChapterDownloadAdapter.ChapterBasicHolder>() {

    private var chapterList: List<Chapter> = mutableListOf()

    class ChapterBasicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val tvChapterNumber: TextView= itemView.findViewById(R.id.tvChapterNumber)
         val tvChapterName: TextView= itemView.findViewById(R.id.tvChapterName)
         val tvPostDay: TextView= itemView.findViewById(R.id.tvPostDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterBasicHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_chapter_item, parent, false)
        return ChapterBasicHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterBasicHolder, position: Int) {
        val chapter = chapterList[position]
        holder.tvChapterNumber.text =
            String.format(Locale.getDefault(), "%s.", chapter.chapterNumber)
        holder.tvChapterName.text = chapter.chapterName
        holder.tvPostDay.text = chapter.postDay
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChapterList(chapterList: List<Chapter>) {
        this.chapterList = chapterList
        notifyDataSetChanged()
    }
}
