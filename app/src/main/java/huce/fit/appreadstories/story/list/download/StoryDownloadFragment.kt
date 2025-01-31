package huce.fit.appreadstories.story.list.download

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.StoryDownloadAdapter
import huce.fit.appreadstories.dialog.story_download.StoryDownloadDialog
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.information.StoryInformationActivity

class StoryDownloadFragment: Fragment() , StoryDownloadView {
    
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var storyDownloadPresenter: StoryDownloadPresenter
    private lateinit var storyDownloadAdapter: StoryDownloadAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_story_download, container, false)
        init(view)
        processEvents()
        return view
    }

    private fun init(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        progressBar = view.findViewById(R.id.pbReLoad)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcViewStoryDownload)

        storyDownloadPresenter = StoryDownloadImpl(this, requireContext())
        storyDownloadAdapter = StoryDownloadAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = storyDownloadAdapter
        getData()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun processEvents() {
        swipeRefreshLayout.setOnRefreshListener {
            progressBar.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = true
            swipeRefreshLayout.isRefreshing = false
            getData()
        }

        storyDownloadAdapter.setClickListener { position: Int, _: View?, isLongClick: Boolean ->
            storyDownloadPresenter.setStoryId(position)
            if (isLongClick) {
                val storyDownloadDialog = StoryDownloadDialog(this)
                storyDownloadDialog.show()
            } else {
                val intent = Intent(activity, StoryInformationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getData() {
        progressBar.visibility = View.VISIBLE
        storyDownloadPresenter.getData()
    }

    override fun setData(storyList: List<Story>) {
        storyDownloadAdapter.setDataStory(storyList)
        progressBar.visibility = View.GONE
    }

    override fun openStoryDownload() {
        val intent = Intent(activity, StoryInformationActivity::class.java)
        startActivity(intent)
    }

    override fun deleteStoryDownload() {
        val intent = Intent(activity, DeleteStoryService::class.java)
        intent.putExtra("storyId", storyDownloadPresenter.getStoryId())
        requireActivity().startService(intent)
    }
}
