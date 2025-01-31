package huce.fit.appreadstories.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.model.Chapter
import java.util.Locale

class ChapterDialogAdapter : RecyclerView.Adapter<ChapterDialogAdapter.ChapterDownloadHolder>(),
    Filterable {

    private var chapterList: MutableList<Chapter> = mutableListOf()
    private var chapterListOld: MutableList<Chapter> = mutableListOf()
    private var clickListener: ClickListener? = null

    class ChapterDownloadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChapterNumber: TextView = itemView.findViewById(R.id.tvChapterNumber)
        val tvChapter: TextView = itemView.findViewById(R.id.tvChapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterDownloadHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.nav_chapter_list_item, parent, false)
        return ChapterDownloadHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterDownloadHolder, position: Int) {
        val chapter = chapterList[position]
        holder.tvChapterNumber.text = String.format(
            Locale.getDefault(), "%s.", chapter.chapterNumber
        )
        holder.tvChapter.text = chapter.chapterName
        holder.itemView.setOnClickListener { view: View ->
            clickListener!!.onItemClick(chapter.chapterId, view, false)
        }
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val searchStr = charSequence.toString()
                chapterList = if (searchStr.isEmpty()) {
                    chapterListOld
                } else {
                    val list: MutableList<Chapter> = mutableListOf()
                    for (chapter in chapterListOld) {
                        if (chapter.chapterNumber.contains(searchStr)) {
                            list.add(chapter)
                        }
                    }
                    list
                }
                val filterResults = FilterResults()
                filterResults.values = chapterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                chapterList = filterResults.values as MutableList<Chapter>
                notifyDataSetChanged()
            }
        }
    }

    fun clickListener(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChapterList(chapterList: List<Chapter>) {
        this.chapterList.clear()
        chapterListOld.clear()
        this.chapterList.addAll(chapterList)
        chapterListOld.addAll(chapterList)
        notifyDataSetChanged()
    }

    fun reverseData() {
        chapterList.reverse()
    }
}
