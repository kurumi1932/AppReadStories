package huce.fit.appreadstories.adapters

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import huce.fit.appreadstories.story.information.sub_fragment.introduce.StoryInterfaceIntroduceFragment
import huce.fit.appreadstories.story.information.sub_fragment.rate.StoryInterfaceRateFragment

class ViewPagerStoryInterfaceAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            StoryInterfaceIntroduceFragment()
        } else {
            StoryInterfaceRateFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
