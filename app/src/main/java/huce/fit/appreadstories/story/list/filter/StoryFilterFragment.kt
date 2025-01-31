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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.StoryAdapter
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.information.StoryInformationActivity
import huce.fit.appreadstories.util.AppUtil

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
    private lateinit var storyFilterPresenter: StoryFilterPresenter
    private lateinit var storyAdapter: StoryAdapter
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

        storyFilterPresenter = StoryFilterImpl(this, requireContext())
        storyAdapter = StoryAdapter()
        rcViewStory.setLayoutManager(LinearLayoutManager(activity))
        rcViewStory.setAdapter(storyAdapter)

        tvSpecies0.setBackgroundResource(R.drawable.border_filter_click)
        tvStatus0.setBackgroundResource(R.drawable.border_filter_click)
        storyFilterPresenter.setSpecies(tvSpecies0.getText().toString().trim { it <= ' ' })
        storyFilterPresenter.setStatus(tvStatus0.getText().toString().trim { it <= ' ' })
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
            storyFilterPresenter.getData()
        } else {
            hide()
        }
    }

    override fun setData(storyList: List<Story>) {
        storyAdapter.setDataStory(storyList)
        progressBar.visibility = View.GONE
    }

    private fun selectSpeciesFilter(view: View, speciesId: Int) {
        removeSelectSpecial()
        val tvSpecies: TextView = view.findViewById(speciesId)
        tvSpecies.setBackgroundResource(R.drawable.border_filter_click)
        storyFilterPresenter.setSpecies(tvSpecies.text.toString())
        getData()
    }

    private fun removeSelectSpecial(){
        tvSpecies0.setBackgroundColor(Color.WHITE)
        tvSpecies1.setBackgroundColor(Color.WHITE)
        tvSpecies2.setBackgroundColor(Color.WHITE)
        tvSpecies3.setBackgroundColor(Color.WHITE)
        tvSpecies4.setBackgroundColor(Color.WHITE)
        tvSpecies5.setBackgroundColor(Color.WHITE)
    }

    private fun selectStatusFilter(view: View, statusId: Int) {
        removeSelectStatus()
        val tvStatus: TextView = view.findViewById(statusId)
        tvStatus.setBackgroundResource(R.drawable.border_filter_click)
        storyFilterPresenter.setStatus(tvStatus.text.toString())
        getData()
    }

    private fun removeSelectStatus(){
        tvStatus0.setBackgroundColor(Color.WHITE)
        tvStatus1.setBackgroundColor(Color.WHITE)
        tvStatus2.setBackgroundColor(Color.WHITE)
    }

    private fun processEvents() {
        storyAdapter.setClickListener { position: Int, _: View, _: Boolean ->
            storyFilterPresenter.setStoryId(position)
            val intent = Intent(activity, StoryInformationActivity::class.java)
            startActivity(intent)
        }

        btCheckNetwork.setOnClickListener {
            if (isConnecting(requireContext())) {
                getData()
                linearLayout.visibility = View.VISIBLE
                btCheckNetwork.visibility = View.GONE
            } else {
                AppUtil.setToast(requireContext(), "Không có kết nối mạng!\n\tVui lòng thử lại.")
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