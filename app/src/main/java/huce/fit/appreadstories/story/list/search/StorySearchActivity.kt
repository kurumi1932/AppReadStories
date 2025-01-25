package huce.fit.appreadstories.story.list.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.StoryAdapter
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.information.StoryInformationActivity

class StorySearchActivity : AppCompatActivity(), StorySearchView {

    private lateinit var ivBack: ImageView
    private lateinit var svStory: SearchView
    private lateinit var mStoryAdapter: StoryAdapter
    private lateinit var mStorySearchPresenter: StorySearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_search)
        init()
        processEvents()
    }

    fun init() {
        mStorySearchPresenter = StorySearchImpl(this, this)
        ivBack = findViewById(R.id.ivBack)
        svStory = findViewById(R.id.svStory)
        mStoryAdapter = StoryAdapter()

        val rcViewStory: RecyclerView = findViewById(R.id.rcViewStory)
        rcViewStory.setLayoutManager(LinearLayoutManager(this))
        //Đổ dữ liệu lên adpter
        rcViewStory.setAdapter(mStoryAdapter)
        //đường kẻ giữa các icon
        val itemDecoration: ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rcViewStory.addItemDecoration(itemDecoration)
    }

    fun processEvents() {
        mStoryAdapter.setClickListener { position: Int, _: View?, _: Boolean ->
            mStorySearchPresenter.setStoryId(position)
            val intent = Intent(this, StoryInformationActivity::class.java)
            startActivity(intent)
        }
        ivBack.setOnClickListener { finish() }
        svStory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    mStorySearchPresenter.getStoryListSearch(newText)
                }
                return false
            }
        })
    }

    override fun setData(storyList: List<Story>) {
        mStoryAdapter.setDataStory(storyList.toMutableList())
    }
}
