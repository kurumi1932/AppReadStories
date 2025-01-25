package huce.fit.appreadstories.story.list.follow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.StoryAdapter
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.information.StoryInformationActivity

class StoryFollowFragment : Fragment() , StoryFollowView{
    
    private lateinit var linearLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var btCheckNetwork: Button
    private lateinit var storyFollowPresenter: StoryFollowPresenter
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_story_follow, container, false)
        init(view)
        processEvent()
        return view
    }

    private fun init(view: View) {
        linearLayout = view.findViewById(R.id.llFragmentStoryFollow)
        progressBar = view.findViewById(R.id.pbReLoad)
        btCheckNetwork = view.findViewById(R.id.btCheckNetwork)
        val rcViewStory = view.findViewById<RecyclerView>(R.id.rcViewStory)

        storyFollowPresenter = StoryFollowImpl(this, requireContext())
        storyAdapter = StoryAdapter()
        rcViewStory.layoutManager = LinearLayoutManager(activity)
        rcViewStory.adapter = storyAdapter
        getData()
    }

    private fun processEvent() {
        storyAdapter.setClickListener { position: Int, _: View, _: Boolean ->
            storyFollowPresenter.setStoryId(position)
            val intent = Intent(activity, StoryInformationActivity::class.java)
            startActivity(intent)
        }

        btCheckNetwork.setOnClickListener {
            if (isConnecting(requireContext())) {
                getData()
                linearLayout.visibility = View.VISIBLE
                btCheckNetwork.visibility = View.GONE
            } else {
                Toast.makeText(
                    activity, "Không có kết nối mạng!\n\tVui lòng thử lại.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        if (isConnecting(requireContext())) {
            progressBar.visibility = View.VISIBLE
            storyFollowPresenter.getData()
        } else {
            linearLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
            btCheckNetwork.visibility = View.VISIBLE
        }
    }

    override fun setDataFollow(storyList: List<Story>) {
        if (storyList.isNotEmpty()) {
            storyAdapter.setDataStory(storyList)
        }
        progressBar.visibility = View.GONE
    }
}