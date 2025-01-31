package huce.fit.appreadstories.dialog.chapter_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.ChapterDialogAdapter
import huce.fit.appreadstories.chapter.information.ChapterInformationView
import huce.fit.appreadstories.dialog.BaseDialog
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.Story

class ChapterListDialog(private val chapterInformationView: ChapterInformationView) :
    BaseDialog(chapterInformationView as Context), ChapterListDialogView {

    private lateinit var ivStory: ImageView
    private lateinit var ivReverse: ImageView
    private lateinit var ivClose: ImageView
    private lateinit var tvStoryName: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var svChapter: SearchView
    private lateinit var rcViewChapter: RecyclerView
    private var linearLayoutManager = LinearLayoutManager(context)
    private var chapterDialogAdapter = ChapterDialogAdapter()

    init{
        setDialog(R.layout.dialog_chapter_list)
        setWindow(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, 
            Gravity.START, 0
        )
        init()
        processEvents()
    }

    private fun init() {
        ivStory = dialog.findViewById(R.id.ivStory)
        ivReverse = dialog.findViewById(R.id.ivReverse)
        ivClose = dialog.findViewById(R.id.ivClose)
        tvStoryName = dialog.findViewById(R.id.tvStoryName)
        tvAuthor = dialog.findViewById(R.id.tvAuthor)
        svChapter = dialog.findViewById(R.id.svChapter)
        rcViewChapter = dialog.findViewById(R.id.rcChapterListOnChapterRead)

        rcViewChapter.layoutManager = linearLayoutManager
        rcViewChapter.adapter = chapterDialogAdapter
        val chapterListDialogPresenter = ChapterListDialogImpl(
            chapterInformationView, this
        )
        chapterListDialogPresenter.getChapterList()
    }

    override fun setChapterList(chapterList: List<Chapter>) {
        chapterDialogAdapter.setChapterList(chapterList)
    }

    override fun setData(story: Story) {
        tvStoryName.text = story.storyName
        tvAuthor.text = story.author
        Picasso.get().load(story.image).into(ivStory)
    }

    private fun processEvents() {
        chapterDialogAdapter.clickListener { position: Int, _: View?, _: Boolean ->
            chapterInformationView.moveChapter(position)
        }

        ivReverse.setOnClickListener {
            chapterDialogAdapter.reverseData()
            rcViewChapter.adapter = chapterDialogAdapter
        }

        svChapter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                chapterDialogAdapter.filter.filter(newText)
                return false
            }
        })

        ivClose.setOnClickListener { dialog.dismiss() }
    }
}
