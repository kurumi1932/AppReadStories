package huce.fit.appreadstories.story.list.filter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.StoryAdapter
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.information.StoryInformationActivity

class StoryFilterFragment: Fragment(), StoryFilterView {

    private lateinit var linearLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var tvSpecies0: TextView
    private lateinit var tvSpecies1:TextView
    private lateinit var tvSpecies2:TextView
    private lateinit var tvSpecies3:TextView
    private lateinit var tvSpecies4:TextView
    private lateinit var tvSpecies5:TextView
    private lateinit var tvStatus0: TextView
    private lateinit var tvStatus1:TextView
    private lateinit var tvStatus2:TextView
    private lateinit var btCheckNetwork: Button
    private lateinit var mStoryFilterPresenter: StoryFilterPresenter
    private lateinit var mStoryAdapter: StoryAdapter
    private lateinit var rcViewStory: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_story_filter, container, false)
        init(view)
        processEvents()
        return view
    }

    private fun init(view: View) {
        linearLayout = view.findViewById(R.id.llFragmentStoryFilter)
        tvSpecies0 = view.findViewById(R.id.tvSpecies0)
        tvSpecies1 = view.findViewById(R.id.tvSpecies1)
        tvSpecies2 = view.findViewById(R.id.tvSpecies2)
        tvSpecies3 = view.findViewById(R.id.tvSpecies3)
        tvSpecies4 = view.findViewById(R.id.tvSpecies4)
        tvSpecies5 = view.findViewById(R.id.tvSpecies5)
        tvStatus0 = view.findViewById(R.id.tvStatus0)
        tvStatus1 = view.findViewById(R.id.tvStatus1)
        tvStatus2 = view.findViewById(R.id.tvStatus2)
        progressBar = view.findViewById(R.id.pbReLoad)
        btCheckNetwork = view.findViewById(R.id.btCheckNetwork)
        rcViewStory = view.findViewById(R.id.rcViewStory)

        mStoryFilterPresenter = StoryFilterImpl(this, requireActivity())
        mStoryAdapter = StoryAdapter()
        rcViewStory.setLayoutManager(LinearLayoutManager(activity))
        rcViewStory.setAdapter(mStoryAdapter)

        tvSpecies0.setBackgroundResource(R.drawable.border_filter_click)
        tvStatus0.setBackgroundResource(R.drawable.border_filter_click)
        mStoryFilterPresenter.setSpeciesId(R.id.tvSpecies0)
        mStoryFilterPresenter.setSpecies(tvSpecies0.getText().toString().trim { it <= ' ' })
        mStoryFilterPresenter.setStatusId(R.id.tvStatus0)
        mStoryFilterPresenter.setStatus(tvStatus0.getText().toString().trim { it <= ' ' })
        getData()
    }

    private fun hide() {
        linearLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
        btCheckNetwork.visibility = View.VISIBLE
    }

    private fun getData() {
        if (isConnecting(requireActivity())) {
            progressBar.visibility = View.VISIBLE
            mStoryFilterPresenter.getData()
        } else {
            hide()
        }
    }

    override fun setData(storyList: List<Story>) {
        mStoryAdapter.setDataStory(storyList)
        progressBar.visibility = View.GONE
    }

    private fun selectSpeciesFilter(view: View, speciesId: Int) {
        view.findViewById<View>(mStoryFilterPresenter.getSpeciesId()).setBackgroundColor(Color.WHITE)
        val tvSpecies: TextView = view.findViewById(speciesId)
        tvSpecies.setBackgroundResource(R.drawable.border_filter_click)
        mStoryFilterPresenter.setSpeciesId(speciesId)
        mStoryFilterPresenter.setSpecies(tvSpecies.text.toString())
        getData()
    }

    private fun selectStatusFilter(view: View, statusId: Int) {
        view.findViewById<View>(mStoryFilterPresenter.getStatusId()).setBackgroundColor(Color.WHITE)
        val tvStatus: TextView = view.findViewById(statusId)
        tvStatus.setBackgroundResource(R.drawable.border_filter_click)
        mStoryFilterPresenter.setStatusId(statusId)
        mStoryFilterPresenter.setStatus(tvStatus.text.toString())
        getData()
    }

    private fun processEvents() {
        mStoryAdapter.setClickListener { position: Int, _: View, _: Boolean ->
            mStoryFilterPresenter.setStoryId(position)
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

        tvSpecies0.setOnClickListener { v: View -> selectSpeciesFilter(v, R.id.tvSpecies0) }
        tvSpecies1.setOnClickListener { v: View -> selectSpeciesFilter(v, R.id.tvSpecies1) }
        tvSpecies2.setOnClickListener { v: View -> selectSpeciesFilter(v, R.id.tvSpecies2) }
        tvSpecies3.setOnClickListener { v: View -> selectSpeciesFilter(v, R.id.tvSpecies3) }
        tvSpecies4.setOnClickListener { v: View -> selectSpeciesFilter(v, R.id.tvSpecies4) }
        tvSpecies5.setOnClickListener { v: View -> selectSpeciesFilter(v, R.id.tvSpecies5) }

        tvStatus0.setOnClickListener { v: View -> selectStatusFilter(v, R.id.tvStatus0) }
        tvStatus1.setOnClickListener { v: View -> selectStatusFilter(v, R.id.tvStatus1) }
        tvStatus2.setOnClickListener { v: View -> selectStatusFilter(v, R.id.tvStatus2) }
    }
}