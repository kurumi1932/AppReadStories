package huce.fit.appreadstories.story.list.online

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.StoryAdapter
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.pagination_croll.PaginationScrollListener
import huce.fit.appreadstories.story.information.StoryInformationActivity
import huce.fit.appreadstories.story.list.search.StorySearchActivity
import huce.fit.appreadstories.util.AppUtil

class StoryFragment : Fragment(), StoryView{

    private lateinit var linearLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var ivSearch: ImageView
    private lateinit var btCheckNetwork: Button
    private lateinit var storyPresenter: StoryPresenter
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_story, container, false)
        init(view)
        processEvent()
        return view
    }

    private fun init(view: View) {
        linearLayout = view.findViewById(R.id.llFragmentStory)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        progressBar = view.findViewById(R.id.pbReLoad)
        ivSearch = view.findViewById(R.id.ivSearch)
        recyclerView = view.findViewById(R.id.rcViewStory)
        btCheckNetwork = view.findViewById(R.id.btCheckNetwork)

        storyPresenter = StoryImpl(this, requireContext())
        storyAdapter = StoryAdapter()
        //các Item có cùng chiều cao và độ rộng có thể tối ưu hiệu năng để khi cuộn danh sách được mượt mà hơn
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerView.setAdapter(storyAdapter)

        getData(1)
    }

    private fun processEvent() {
        storyAdapter.setClickListener { position: Int, _: View?, _: Boolean ->
            storyPresenter.setStoryId(position)
            val intent = Intent(activity, StoryInformationActivity::class.java)
            startActivity(intent)
        }

        btCheckNetwork.setOnClickListener {
            if (isConnecting(requireContext())) {
                getData(1)
            } else {
                AppUtil.setToast(requireContext(), "Không có kết nối mạng!\n\tVui lòng thử lại.")
            }
        }

        recyclerView.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                progressBar.visibility = View.VISIBLE
                storyPresenter.loadNextPage()
            }

            override fun isLoading(): Boolean {
                return storyPresenter.isLoading()
            }

            override fun isLastPage(): Boolean {
                return storyPresenter.isLastPage()
            }
        })

        ivSearch.setOnClickListener {
            val intent = Intent(activity, StorySearchActivity::class.java)
            startActivity(intent)
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            storyPresenter.swipeData()
        }
    }

    override fun getData(page: Int) {
        if (isConnecting(requireContext())) {
            storyPresenter.getStory(page)
            swipeRefreshLayout.visibility = View.VISIBLE
            linearLayout.visibility = View.VISIBLE
            btCheckNetwork.visibility = View.GONE
        } else {
            swipeRefreshLayout.visibility = View.GONE
            linearLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
            btCheckNetwork.visibility = View.VISIBLE
        }
    }

    override fun setData(storyList: List<Story>) {
        storyAdapter.setDataStory(storyList)
        progressBar.visibility = View.GONE
    }
}