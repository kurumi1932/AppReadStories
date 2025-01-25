package huce.fit.appreadstories.story.download

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.squareup.picasso.Picasso
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.ChapterDownloadAdapter
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.Story

class StoryDownloadActivity : AppCompatActivity(), StoryDownloadView{

    private lateinit var mStoryDownloadPresenter: StoryDownloadPresenter
    private lateinit var llStoryDownload: LinearLayout
    private lateinit var ivBack: ImageView
    private lateinit var ivStory:ImageView
    private lateinit var tvStoryName: TextView
    private lateinit var tvAuthor:TextView
    private lateinit var tvStatus:TextView
    private lateinit var tvSumChapter:TextView
    private lateinit var tvSpecies:TextView
    private lateinit var btDownloadStory: Button
    private lateinit var pbReLoad: ProgressBar
    private lateinit var chapterDownloadAdapter: ChapterDownloadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_download)
        init()
        processEvents()
    }

    fun init() {
        llStoryDownload = findViewById(R.id.llStoryDownload)
        ivStory = findViewById(R.id.ivStory)
        tvStoryName = findViewById(R.id.tvStoryName)
        tvAuthor = findViewById(R.id.tvAuthor)
        tvStatus = findViewById(R.id.tvStatus)
        tvSumChapter = findViewById(R.id.tvSumChapter)
        tvSpecies = findViewById(R.id.tvSpecies)
        ivBack = findViewById(R.id.ivBack)
        btDownloadStory = findViewById(R.id.btDownloadStory)
        pbReLoad = findViewById(R.id.pbReLoad)
        val rcViewChapter: RecyclerView = findViewById(R.id.rcViewChapter)
        val itemDecoration: ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        start()
        mStoryDownloadPresenter = StoryDownloadImpl(this)
        chapterDownloadAdapter = ChapterDownloadAdapter()
        rcViewChapter.setLayoutManager(LinearLayoutManager(this))
        rcViewChapter.setAdapter(chapterDownloadAdapter)
        rcViewChapter.addItemDecoration(itemDecoration)
    }

    fun start() {
        llStoryDownload.visibility = View.INVISIBLE
        pbReLoad.visibility = View.VISIBLE
    }

    override fun setDataStory(story: Story) {
        tvStoryName.text = story.storyName
        tvAuthor.text = story.author
        tvStatus.text = story.status
        tvSumChapter.text = story.sumChapter.toString()
        tvSpecies.text = story.species
        Picasso.get().load(story.image).into(ivStory)
    }

    override fun setChapterList(chapterList: List<Chapter>) {
        chapterDownloadAdapter.setChapterList(chapterList)
        pbReLoad.visibility = View.GONE
        llStoryDownload.visibility = View.VISIBLE
    }

    fun processEvents() {
        ivBack.setOnClickListener { finish() }
        btDownloadStory.setOnClickListener {
            val intent = Intent(this@StoryDownloadActivity, StoryDownloadService::class.java)
            startService(intent)
        }
    }
}
