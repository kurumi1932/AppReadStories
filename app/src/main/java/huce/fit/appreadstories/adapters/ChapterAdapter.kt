package huce.fit.appreadstories.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead
import java.util.Locale

class ChapterAdapter(private var context: Context) : RecyclerView.Adapter<ChapterAdapter.ChapterHolder>() , Filterable {

    companion object{
        private const val TAG = "ChapterAdapter"
    }

    private var chapterList: MutableList<Chapter> = mutableListOf()
    private var chapterListOld: MutableList<Chapter> = mutableListOf()
    private val chapterReadMap = HashMap<Int, ChapterRead?>()
    private lateinit var clickListener: ClickListener
    private var chapterReadingId = 0

    class ChapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChapterNumber: TextView = itemView.findViewById(R.id.tvChapterNumber)
        val tvChapterName: TextView = itemView.findViewById(R.id.tvChapterName)
        val tvPostDay: TextView= itemView.findViewById(R.id.tvPostDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_chapter_item, parent, false)
        return ChapterHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterHolder, position: Int) {
        val chapter = chapterList[position]
        val chapterId = chapter.chapterId
        holder.tvChapterNumber.text = String.format(
            Locale.getDefault(), "%s.", chapter.chapterNumber
        )
        holder.tvChapterName.text = chapter.chapterName
        holder.tvPostDay.text = chapter.postDay
        holder.itemView.setOnClickListener { view: View ->
            clickListener.onItemClick(chapterId, view, false)
        }
        if (chapterReadMap.containsKey(chapterId)) {
            if (chapterReadingId != chapterId) {
                holder.tvChapterNumber.setTextColor(context.getColor(R.color.dim_gray))
                holder.tvChapterName.setTextColor(context.getColor(R.color.dim_gray))
                holder.tvPostDay.setTextColor(context.getColor(R.color.dim_gray))
            } else {
                holder.tvChapterNumber.setTextColor(context.getColor(R.color.orange))
                holder.tvChapterName.setTextColor(context.getColor(R.color.orange))
                holder.tvPostDay.setTextColor(context.getColor(R.color.orange))
            }
        } else {
            holder.tvChapterNumber.setTextColor(context.getColor(R.color.black))
            holder.tvChapterName.setTextColor(context.getColor(R.color.black))
            holder.tvPostDay.setTextColor(context.getColor(R.color.black))
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

    fun clickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    fun reverseData() {
        chapterList.reverse()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChapterList(chapterList: MutableList<Chapter>) {
        this.chapterList = chapterList
        chapterListOld = chapterList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChapterReadList(chapterReadList: List<ChapterRead>, chapterReadingId: Int) {
        for (chapterRead in chapterReadList) {
            chapterReadMap[chapterRead.chapterId] = null
        }
        this.chapterReadingId = chapterReadingId
        notifyDataSetChanged()
    }
}
