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
import java.util.Collections
import java.util.Locale

class ChapterAdapter(private var mContext: Context) : RecyclerView.Adapter<ChapterAdapter.ChapterHolder>() , Filterable {

    companion object{
        private const val TAG = "ChapterAdapter"
    }

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
        val chapter = mChapterList[position]
        val chapterId = chapter.chapterId
        holder.tvChapterNumber.text = String.format(
            Locale.getDefault(), "%s.", chapter.chapterNumber
        )
        holder.tvChapterName.text = chapter.chapterName
        holder.tvPostDay.text = chapter.postDay
        holder.itemView.setOnClickListener { view: View ->
            mClickListener.onItemClick(chapterId, view, false)
        }
        if (mChapterReadMap.containsKey(chapterId)) {
            if (mChapterReadingId != chapterId) {
                holder.tvChapterNumber.setTextColor(mContext.getColor(R.color.dim_gray))
                holder.tvChapterName.setTextColor(mContext.getColor(R.color.dim_gray))
                holder.tvPostDay.setTextColor(mContext.getColor(R.color.dim_gray))
            } else {
                holder.tvChapterNumber.setTextColor(mContext.getColor(R.color.orange))
                holder.tvChapterName.setTextColor(mContext.getColor(R.color.orange))
                holder.tvPostDay.setTextColor(mContext.getColor(R.color.orange))
            }
        } else {
            holder.tvChapterNumber.setTextColor(mContext.getColor(R.color.black))
            holder.tvChapterName.setTextColor(mContext.getColor(R.color.black))
            holder.tvPostDay.setTextColor(mContext.getColor(R.color.black))
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


    var mChapterList: MutableList<Chapter> = mutableListOf()
    var mChapterListOld: MutableList<Chapter> = mutableListOf()
    private val mChapterReadMap = HashMap<Int, ChapterRead?>()
    private lateinit var mClickListener: ClickListener
    private var mChapterReadingId = 0

    fun clickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    fun reverseData() {
        mChapterList.reverse()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChapterList(chapterList: MutableList<Chapter>) {
        mChapterList = chapterList
        mChapterListOld = chapterList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChapterReadList(chapterReadList: List<ChapterRead>, chapterReadingId: Int) {
        for (chapterRead in chapterReadList) {
            mChapterReadMap[chapterRead.chapterId] = null
        }
        mChapterReadingId = chapterReadingId
        notifyDataSetChanged()
    }
}
