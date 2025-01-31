package huce.fit.appreadstories.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import huce.fit.appreadstories.story.information.sub_fragment.introduce.IntroduceFragment
import huce.fit.appreadstories.story.information.sub_fragment.rate.RateFragment

class ViewPagerStoryInterfaceAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            IntroduceFragment()
        } else {
            RateFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
