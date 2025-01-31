package huce.fit.appreadstories.chapter.list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.ChapterAdapter
import huce.fit.appreadstories.chapter.information.ChapterInformationActivity
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead

class ChapterListActivity : AppCompatActivity(), ChapterListView {
    
    private lateinit var chapterAdapter: ChapterAdapter
    private lateinit var rcViewChapter: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var ivReverse:ImageView
    private lateinit var svChapter: SearchView
    private lateinit var pbReload: ProgressBar
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var chapterListPresenter: ChapterListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_list)
        init()
        getData()
        processEvents()
    }

    fun init() {
        pbReload = findViewById(R.id.pbReLoad)
        rcViewChapter = findViewById(R.id.rcViewChapter)
        ivBack = findViewById(R.id.ivBack)
        svChapter = findViewById(R.id.svChapter)
        ivReverse = findViewById(R.id.ivReverse)
        svChapter.setMaxWidth(Int.MAX_VALUE)

        chapterListPresenter = ChapterListImpl(this)
        rcViewChapter.setLayoutManager(LinearLayoutManager(this))
        chapterAdapter = ChapterAdapter(this)
        linearLayoutManager = LinearLayoutManager(this)
        rcViewChapter.setLayoutManager(linearLayoutManager)
        rcViewChapter.setAdapter(chapterAdapter)
    }

    fun getData() {
        pbReload.visibility = View.VISIBLE
        chapterListPresenter.getData()
    }

    override fun setData(chapterListRead: List<ChapterRead>, chapterReadingId: Int) {
        chapterAdapter.setChapterReadList(chapterListRead, chapterReadingId)
        pbReload.visibility = View.GONE
    }

    override fun setData(chapterList: List<Chapter>) {
        chapterAdapter.setChapterList(chapterList.toMutableList())
        pbReload.visibility = View.GONE
    }

    fun processEvents() {
        ivBack.setOnClickListener { finish() }
        svChapter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                chapterAdapter.filter.filter(newText)
                return false
            }
        })
        chapterAdapter.clickListener { position: Int, _: View?, _: Boolean ->
            val intent = Intent(this, ChapterInformationActivity::class.java)
            intent.putExtra("chapterId", position)
            startActivity(intent)
            finish()
        }
        ivReverse.setOnClickListener { reverse() }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun reverse() {
        //Đầu trang
        chapterAdapter.reverseData()
        rcViewChapter.setAdapter(chapterAdapter)
    }
}
