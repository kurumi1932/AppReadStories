package huce.fit.appreadstories.story.information

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDatabase
import huce.fit.appreadstories.util.AppUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckStoryService : Service() {

    companion object {
        private const val TAG: String = "CheckStoryService"
    }

    private var appDao = AppDatabase.getInstance(this).appDao()
    private var accountId = 0
    private var storyId = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        AppUtil.setToast(this, "Kiểm tra cập nhật của truyện!")
        Log.e(TAG, "NHT CheckStoryService: start")
        getAccount()
        getStory()
        return START_STICKY
    }

    private fun getAccount() {
        val account = AccountSharedPreferences(this)
        account.getSharedPreferences("Account", MODE_PRIVATE)
        accountId = account.getAccountId()
    }

    private fun getStory() {
        val mStory = StorySharedPreferences(this)
        mStory.getSharedPreferences("Story", MODE_PRIVATE)
        storyId = mStory.getStoryId()

        if (appDao.getStory(storyId) != null) {
            updateStoryDownload()
            updateChapterReadServer()
        }
    }

    private fun updateStoryDownload() {
        Api().apiInterface().getStory(storyId).enqueue(object : Callback<Story> {
            override fun onResponse(call: Call<Story>, response: Response<Story>) {
                val story = response.body()
                if (response.isSuccessful && story != null) {
                    val storyDao = Story(story)
                    storyDao.newChapter = story.sumChapter - appDao.getSumChapter(storyId)
                    appDao.updateStory(storyDao)
                }

                Log.e(TAG, "api getStory: success")
            }

            override fun onFailure(call: Call<Story>, t: Throwable) {
                Log.e(TAG, "api getStory: false")
            }
        })
    }

    private fun updateChapterReadServer() {
        Api().apiInterface().getChapterListRead(storyId, accountId, 0)
            .enqueue(object : Callback<List<ChapterRead>> {
                override fun onResponse(
                    call: Call<List<ChapterRead>>, response: Response<List<ChapterRead>>
                ) {
                    val chapterReadListServer = response.body()
                    if (response.isSuccessful && chapterReadListServer != null) {
                        val chapterReadList = appDao.getChapterReadList(storyId)
                        chapterReadList.removeAll(chapterReadListServer)
                        for (chapterRead in chapterReadList) {
                            addChapterReadServer(chapterRead.chapterId)
                        }

                        val chapterReadingId = appDao.getStory(storyId).chapterReading
                        addChapterReadServer(chapterReadingId)
                    }
                    Log.e(TAG, "NHT updateChapterReadServer: success")
                    onDestroy()
                }

                override fun onFailure(call: Call<List<ChapterRead>>, t: Throwable) {
                    Log.e(TAG, "NHT updateChapterReadServer: fail")
                }
            })
    }

    private fun addChapterReadServer(chapterId: Int) {
        Api().apiInterface().getChapter(storyId, chapterId, 2, accountId)
            .enqueue(object : Callback<Chapter> {
                override fun onResponse(call: Call<Chapter>, response: Response<Chapter>) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.e(TAG, "NHT addChapterReadServer: success")
                    }
                }

                override fun onFailure(call: Call<Chapter>, t: Throwable) {
                    Log.e(TAG, "NHT addChapterReadServer: fail")
                }
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "NHT CheckStoryService: stop")
        AppUtil.setToast(this, "Cập nhật hoàn tất")
    }
}
