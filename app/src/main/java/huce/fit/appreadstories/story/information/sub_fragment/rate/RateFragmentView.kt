package huce.fit.appreadstories.story.information.sub_fragment.rate

import huce.fit.appreadstories.model.Rate

interface RateFragmentView {
    fun setData(rateList: List<Rate>)
    fun noNetwork()
}
