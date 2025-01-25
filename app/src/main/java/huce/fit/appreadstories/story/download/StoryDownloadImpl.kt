package huce.fit.appreadstories.story.download

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDao
import huce.fit.appreadstories.sqlite.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryDownloadImpl(val storyDownloadView: StoryDownloadView) : StoryDownloadPresenter {

    companion object {
        private const val TAG = "StoryDownloadImpl"
    }

    private var mContext: Context = storyDownloadView as Context
    private var mAppDao: AppDao = AppDatabase.getInstance(mContext).appDao()
    private val mChapterList: MutableList<Chapter> = ArrayList()
    private val mStory = Story()
    private var mStoryId = 0

    init {
        getStory()
        getChapterList()
    }

    fun getStory() {
        val story = StorySharedPreferences(mContext)
        story.getSharedPreferences("Story", Context.MODE_PRIVATE)
        mStoryId = story.getStoryId()
        mStory.storyId = mStoryId
        Log.e(TAG, "NHT mStoryId: $mStoryId")
        mStory.storyName = story.getStoryName()
        mStory.author = story.getAuthor()
        mStory.status = story.getStatus()
        mStory.species = story.getSpecies()
        mStory.introduce = story.getIntroduce()
        mStory.image = story.getImage()
        mStory.ageLimit = story.getAgeLimit()
        mStory.sumChapter = story.getSumChapter()
        mStory.totalLikes = story.getTotalLikes()
        mStory.totalViews = story.getTotalViews()
        mStory.totalComments = story.getTotalComments()
        mStory.ratePoint = story.getRatePoint()
        mStory.timeUpdate = story.getTimeUpdate()
        mStory.isFollow = if (story.getIsFollow()) 1 else 0
        mStory.chapterReading = story.getChapterReading()
        mStory.newChapter = 0
        storyDownloadView.setDataStory(mStory)
    }

    fun getChapterList() {
        val chapterId = HashMap<Int, Chapter?>()
        for (chapter in mAppDao.getChapterList(mStoryId)) {
            chapterId[chapter.chapterId] = null
        }
        Api().apiInterface().getChapterList(mStoryId).enqueue(object : Callback<List<Chapter>> {
            override fun onResponse(call: Call<List<Chapter>>, response: Response<List<Chapter>>) {
                if (response.isSuccessful && response.body() != null) {
                    mChapterList.clear()
                    for (chapter in response.body()!!) {
                        if (!chapterId.containsKey(chapter.chapterId)) {
                            mChapterList.add(chapter)
                        }
                    }
                    storyDownloadView.setChapterList(mChapterList)
                }
                Log.e(TAG, "api getChapterList: success")
            }

            override fun onFailure(call: Call<List<Chapter>>, t: Throwable) {
                Log.e(TAG, "api getChapterList: false")
            }
        })
    }
}
