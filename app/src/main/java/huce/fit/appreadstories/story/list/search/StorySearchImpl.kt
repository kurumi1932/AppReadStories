package huce.fit.appreadstories.story.list.search

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.story.list.BaseStoryListImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StorySearchImpl(
    val storySearchView: StorySearchView, context: Context
): BaseStoryListImpl(context), StorySearchPresenter {

    companion object {
        private const val TAG = "StorySearchImpl"
    }

    private var mAge = 0

    init {
        getAge()
    }

    private fun getAge() {
        val account: AccountSharedPreferences = getAccount()
        mAge = account.getAge()
    }

    override fun getStoryListSearch(name: String) {
        Api().apiInterface().searchStory(name, mAge).enqueue(object : Callback<List<Story>> {
            override fun onResponse(call: Call<List<Story>>, response: Response<List<Story>>) {
                if (response.isSuccessful){
                    val story = response.body()
                    if (story != null) {
                        storySearchView.setData(story)
                    }
                }
                Log.e(TAG, "api getStoryListSearch: success")
            }

            override fun onFailure(call: Call<List<Story>>, t: Throwable) {
                Log.e(TAG, "api getStoryListSearch: fail")
            }
        })
    }
}
