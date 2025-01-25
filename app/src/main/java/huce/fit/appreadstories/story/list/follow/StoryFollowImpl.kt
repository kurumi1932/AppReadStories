package huce.fit.appreadstories.story.list.follow

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.list.BaseStoryListImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryFollowImpl(
    val storyFollowView: StoryFollowView, context: Context
) : BaseStoryListImpl(context), StoryFollowPresenter {

    companion object {
        private const val TAG = "StoryFollowImpl"
    }

    private var mAccountId = 0

    init {
        mAccountId = getAccount().getAccountId()
    }

    override fun getData() {
        Api().apiInterface().getListStoriesFollow(mAccountId)
            .enqueue(object : Callback<List<Story>> {
                override fun onResponse(call: Call<List<Story>>, response: Response<List<Story>>) {
                    if (response.isSuccessful) {
                        val stories = response.body()
                        if (stories != null) {
                            storyFollowView.setDataFollow(stories)
                        }
                    }
                    Log.e(TAG, "api getData: success")
                }

                override fun onFailure(call: Call<List<Story>>, t: Throwable) {
                    Log.e(TAG, "api getData: fail")
                }
            })
    }
}
