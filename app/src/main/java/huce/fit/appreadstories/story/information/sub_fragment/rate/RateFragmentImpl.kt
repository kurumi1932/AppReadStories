package huce.fit.appreadstories.story.information.sub_fragment.rate

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Rate
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RateFragmentImpl(val rateFragmentView: RateFragmentView, val context: Context) :
    RateFragmentPresenter {

    companion object {
        private const val TAG: String = "StoryInterfaceRateFragmentImpl"
    }

    private var storyId = 0

    init {
        getStory()
    }

    private fun getStory() {
        val story = StorySharedPreferences(context)
        story.getSharedPreferences("Story", Context.MODE_PRIVATE)
        storyId = story.getStoryId()
    }

    override fun getRateList() {
        if (isConnecting(context)) {
            Api().apiInterface().getRateList(storyId).enqueue(object : Callback<List<Rate>> {
                @SuppressLint("LongLogTag")
                override fun onResponse(call: Call<List<Rate>>, response: Response<List<Rate>>) {
                    val rate = response.body()
                    if (response.isSuccessful && rate != null) {
                        rateFragmentView.setData(rate)
                    }
                    Log.e(TAG, "api getRateList: success")
                }

                @SuppressLint("LongLogTag")
                override fun onFailure(call: Call<List<Rate>>, t: Throwable) {
                    Log.e(TAG, "api getRateList: fail")
                }
            })
        } else {
            rateFragmentView.noNetwork()
        }
    }
}