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
        val chapter = mChapterList[position]
        holder.tvChapterNumber.text = String.format(
            Locale.getDefault(), "%s.", chapter.chapterNumber
        )
        holder.tvChapter.text = chapter.chapterName
        holder.itemView.setOnClickListener { view: View ->
            mClickListener!!.onItemClick(chapter.chapterId, view, false)
        }
    }

    override fun getItemCount(): Int {
        return mChapterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val searchStr = charSequence.toString()
                mChapterList = if (searchStr.isEmpty()) {
                    mChapterListOld
                } else {
                    val list: MutableList<Chapter> = mutableListOf()
                    for (chapter in mChapterListOld) {
                        if (chapter.chapterNumber.contains(searchStr)) {
                            list.add(chapter)
                        }
                    }
                    list
                }
                val filterResults = FilterResults()
                filterResults.values = mChapterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mChapterList = filterResults.values as MutableList<Chapter>
                notifyDataSetChanged()
            }
        }
    }

    private var mChapterList: MutableList<Chapter> = mutableListOf()
    private var mChapterListOld: MutableList<Chapter> = mutableListOf()
    private var mClickListener: ClickListener? = null

    fun clickListener(clickListener: ClickListener?) {
        mClickListener = clickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChapterList(chapterList: MutableList<Chapter>) {
        mChapterList = chapterList
        mChapterListOld = chapterList
        notifyDataSetChanged()
    }

    fun reverseData() {
        mChapterList.reverse()
    }
}
