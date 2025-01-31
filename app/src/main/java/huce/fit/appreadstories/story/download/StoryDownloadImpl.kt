package huce.fit.appreadstories.story.download

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryDownloadImpl(val storyDownloadView: StoryDownloadView) : StoryDownloadPresenter {

    companion object {
        private const val TAG = "StoryDownloadImpl"
    }

    private var context = storyDownloadView as Context
    private var appDao = AppDatabase.getInstance(context).appDao()
    private val chapterList: MutableList<Chapter> = ArrayList()
    private var storyId = 0

    override fun getStory() {
        val story = StorySharedPreferences(context)
        story.getSharedPreferences("Story", Context.MODE_PRIVATE)
        storyId = story.getStoryId()
        Log.e(TAG, "NHT storyId: $storyId")
        Api().apiInterface().getStory(storyId).enqueue(object : Callback<Story> {
            override fun onResponse(call: Call<Story>, response: Response<Story>) {
                val storyServer = response.body()
                if (response.isSuccessful && storyServer != null) {
                    val storyDao = Story(storyServer)
                    storyDao.newChapter = 0
                    storyDownloadView.setDataStory(storyDao)
                }

                Log.e(TAG, "api getStory: success")
            }

            override fun onFailure(call: Call<Story>, t: Throwable) {
                Log.e(TAG, "api getStory: false")
            }
        })
    }

    override fun getChapterList() {
        val chapterId = HashMap<Int, Chapter?>()
        for (chapter in appDao.getChapterList(storyId)) {
            chapterId[chapter.chapterId] = null
        }
        Api().apiInterface().getChapterList(storyId).enqueue(object : Callback<List<Chapter>> {
            override fun onResponse(call: Call<List<Chapter>>, response: Response<List<Chapter>>) {
                if (response.isSuccessful && response.body() != null) {
                    chapterList.clear()
                    for (chapter in response.body()!!) {
                        if (!chapterId.containsKey(chapter.chapterId)) {
                            chapterList.add(chapter)
                        }
                    }
                    storyDownloadView.setChapterList(chapterList)
                }
                Log.e(TAG, "api getChapterList: success")
            }

            override fun onFailure(call: Call<List<Chapter>>, t: Throwable) {
                Log.e(TAG, "api getChapterList: false")
            }
        })
    }
}
