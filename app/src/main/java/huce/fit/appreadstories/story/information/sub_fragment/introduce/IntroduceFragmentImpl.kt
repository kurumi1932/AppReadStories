package huce.fit.appreadstories.story.information.sub_fragment.introduce

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IntroduceFragmentImpl(val introduceFragmentView: IntroduceFragmentView, val context: Context) :
    IntroduceFragmentPresenter {

    companion object {
        private const val TAG: String = "StoryInterfaceIntroduceFragmentImpl"
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

    override fun getIntroduce() {
        if (isConnecting(context)) {
            Api().apiInterface().getStory(storyId).enqueue(object : Callback<Story> {
                @SuppressLint("LongLogTag")
                override fun onResponse(call: Call<Story>, response: Response<Story>) {
                    val story = response.body()
                    if (response.isSuccessful && story != null) {
                        introduceFragmentView.setIntroduce(story.introduce)
                    }
                    Log.e(TAG, "api getIntroduce: success")
                }

                @SuppressLint("LongLogTag")
                override fun onFailure(call: Call<Story>, t: Throwable) {
                    Log.e(TAG, "api getIntroduce: fail")
                }
            })
        } else {
            val story = AppDatabase.getInstance(context).appDao().getStory(storyId)
            introduceFragmentView.setIntroduce(story.introduce)
        }
    }
}