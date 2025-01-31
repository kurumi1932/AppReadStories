package huce.fit.appreadstories.dialog.chapter_list

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.chapter.information.ChapterInformationView
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChapterListDialogImpl(
    chapterInformationView: ChapterInformationView, val rateDialogView: ChapterListDialogView
) : ChapterListDialogPresenter {

    companion object {
        private const val TAG = "ChapterListDialogImpl"
    }

    private var context = chapterInformationView as Context
    private val chapterList: MutableList<Chapter> = ArrayList()
    private var storyId = 0

    init {
        getStory()
    }

    private fun getStory() {
        val storySharedPreferences = StorySharedPreferences(context)
        storySharedPreferences.getSharedPreferences("Story", Context.MODE_PRIVATE)
        storyId = storySharedPreferences.getStoryId()
        Log.e(TAG, "NHT storyId: $storyId")
        Api().apiInterface().getStory(storyId).enqueue(object : Callback<Story> {
            override fun onResponse(call: Call<Story>, response: Response<Story>) {
                val storyServer = response.body()
                if (response.isSuccessful && storyServer != null) {
                    rateDialogView.setData(storyServer)
                }
                Log.e(TAG, "api getStory: success")
            }

            override fun onFailure(call: Call<Story>, t: Throwable) {
                Log.e(TAG, "api getStory: false")
            }
        })
    }

    override fun getChapterList() {
        Api().apiInterface().getChapterList(storyId).enqueue(object : Callback<List<Chapter>> {
            override fun onResponse(call: Call<List<Chapter>>, response: Response<List<Chapter>>) {
                val chapterListServer = response.body()
                if (response.isSuccessful && chapterListServer != null) {
                    chapterList.clear()
                    chapterList.addAll(chapterListServer)
                    rateDialogView.setChapterList(chapterList)
                    rateDialogView.show()
                }
                Log.e(TAG, "api getChapterList: success")
            }

            override fun onFailure(call: Call<List<Chapter>>, t: Throwable) {
                Log.e(TAG, "api getChapterList: fail")
            }
        })
    }
}