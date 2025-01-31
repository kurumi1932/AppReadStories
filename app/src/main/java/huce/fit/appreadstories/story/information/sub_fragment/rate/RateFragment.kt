package huce.fit.appreadstories.story.information.sub_fragment.rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.RateAdapter
import huce.fit.appreadstories.model.Rate
import huce.fit.appreadstories.util.AppUtil

class RateFragment: Fragment() , RateFragmentView {

    private var rateAdapter = RateAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_story_interface_rate, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcViewRate)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = rateAdapter
        val itemDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)

        val rateFragmentPresenter = RateFragmentImpl(this, requireContext())
        rateFragmentPresenter.getRateList()
    }

    override fun setData(rateList: List<Rate>) {
        rateAdapter.setDataRate(rateList)
    }

    override fun noNetwork() {
        AppUtil.setToast(requireContext(), "\tChưa kết nối mạng!\nKhông xem được đánh giá!")
    }

}