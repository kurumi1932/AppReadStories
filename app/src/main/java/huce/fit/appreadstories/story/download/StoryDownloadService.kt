package huce.fit.appreadstories.story.download

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import huce.fit.appreadstories.R
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDatabase
import huce.fit.appreadstories.util.AppUtil
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class StoryDownloadService : Service() {

    companion object{
        private const val TAG = "DownloadStoryService"
        private const val CHANNEL_ID = "download_story_service"
    }

    private var appDao = AppDatabase.getInstance(this).appDao()
    private var story = StorySharedPreferences(this)
    private var accountId = 0
    private var storyId = 0
    private var sumChapter = 0
    private var storyName = ""

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        AppUtil.setToast(this, "Bắt đầu tải về")
        Log.e(TAG, "NHT StoryDownloadService: Start")
        getAccount()
        getStory()
        runBlocking {
            downloadStory()
            downloadChapter()
            downloadChapterRead()
        }
        return START_NOT_STICKY
    }

    private fun getAccount() {
        val account = AccountSharedPreferences(this)
        account.getSharedPreferences("Account", MODE_PRIVATE)
        accountId = account.getAccountId()
    }

    private fun getStory() {
        story.getSharedPreferences("Story", MODE_PRIVATE)
        storyId = story.getStoryId()
    }

    private fun downloadStory() {
        if (appDao.getStory(storyId) == null) {
            Api().apiInterface().getStory(storyId).enqueue(object : Callback<Story> {
                override fun onResponse(call: Call<Story>, response: Response<Story>) {
                    val story = response.body()
                    if (response.isSuccessful && story != null) {
                        val storyDao = Story(story)
                        storyDao.newChapter = 0
                        storyName = story.storyName
                        appDao.insertStory(storyDao)
                    }
                    Log.e(TAG, "api getStory: success")
                }

                override fun onFailure(call: Call<Story>, t: Throwable) {
                    Log.e(TAG, "api getStory: false")
                }
            })
        }
    }

    private fun downloadChapterRead() {
        if (appDao.getSumChapterRead(storyId) > 0) {
            return
        }
        Api().apiInterface().getChapterListRead(storyId, accountId, 0)
            .enqueue(object : Callback<List<ChapterRead>> {
                override fun onResponse(
                    call: Call<List<ChapterRead>>, response: Response<List<ChapterRead>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        for (chapterRead in response.body()!!) {
                            appDao.insertChapterRead(chapterRead)
                        }
                    }
                    Log.e(TAG, "NHT downloadChapterRead: success")
                }

                override fun onFailure(call: Call<List<ChapterRead>>, t: Throwable) {
                    Log.e(TAG, "NHT downloadChapterRead: fail")
                }
            })
    }

    private fun downloadChapter() {
        Api().apiInterface().getChapterList(storyId).enqueue(object : Callback<List<Chapter>> {
            override fun onResponse(call: Call<List<Chapter>>, response: Response<List<Chapter>>) {
                val chapterListServer = response.body()
                if (response.isSuccessful && chapterListServer!= null) {
                    var count = 0
                    for (chapterServer in chapterListServer) {
                        if (checkChapter(chapterServer.chapterId)) {
                            appDao.insertChapter(chapterServer)
                            sendNotification(++count)
                        }
                    }
                }
                onDestroy()
                Log.e(TAG, "NHT downloadChapter: success")
            }

            override fun onFailure(call: Call<List<Chapter>>, t: Throwable) {
                Log.e(TAG, "NHT downloadChapter: fail")
            }
        })
    }

    private fun checkChapter(chapterId: Int): Boolean {
        return appDao.getChapter(storyId, chapterId) == null
    }

    private fun sendNotification(count: Int) {
        val icon = if (count < sumChapter) {
            R.drawable.ic_download
        } else {
            R.drawable.ic_check
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setContentTitle(storyName)
            .setContentText(String.format(Locale.getDefault(), "Chương tải về: %d", count))
            .setSmallIcon(icon)
            .setOngoing(true) // thông báo không thể loại bỏ khi vẫn còn hoạt động
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_DEFAULT)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        //tắt notification
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
        Log.e(TAG, "NHT StoryDownloadService: Stop")
        AppUtil.setToast(this, "Tải thành công")
    }
}
