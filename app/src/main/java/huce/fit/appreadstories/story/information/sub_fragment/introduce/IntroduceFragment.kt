package huce.fit.appreadstories.story.information.sub_fragment.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import huce.fit.appreadstories.R

class IntroduceFragment : Fragment() , IntroduceFragmentView {

    private lateinit var tvIntroduce: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_story_interface_introduce, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        tvIntroduce = view.findViewById(R.id.tvIntroduce)

        val introduceFragmentPresenter = IntroduceFragmentImpl(this, requireContext())
        introduceFragmentPresenter.getIntroduce()
    }

    override fun setIntroduce(introduce: String) {
        tvIntroduce.text = introduce
    }
}
