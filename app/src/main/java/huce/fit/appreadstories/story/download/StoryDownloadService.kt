package huce.fit.appreadstories.story.download

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import huce.fit.appreadstories.R
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDao
import huce.fit.appreadstories.sqlite.AppDatabase
import kotlinx.coroutines.delay
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

    private lateinit var mAppDao: AppDao
    private lateinit var mStory: StorySharedPreferences
    private var mAccountId = 0
    private var mStoryId = 0
    private var mSumChapter = 0
    private var mStoryName: String? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Bắt đầu tải về", Toast.LENGTH_LONG).show()
        Log.e(TAG, "NHT StoryDownloadService: Start")
        mAppDao = AppDatabase.getInstance(this).appDao()
        getAccount()
        getStory()
        downloadStory()
        downloadChapterRead()
        runBlocking {
            downloadChapter()
            delay(1000)
        }
        return START_NOT_STICKY
    }

    private fun getAccount() {
        val account = AccountSharedPreferences(this)
        account.getSharedPreferences("Account", MODE_PRIVATE)
        mAccountId = account.getAccountId()
    }

    private fun getStory() {
        mStory = StorySharedPreferences(this)
        mStory.getSharedPreferences("Story", MODE_PRIVATE)
        mStoryId = mStory.getStoryId()
        mStoryName = mStory.getStoryName()
    }

    private fun downloadStory() {
        var story = mAppDao.getStory(mStoryId)
        if (story == null) {
            story = Story()
            story.storyId = mStoryId
            story.storyName = mStoryName
            story.author = mStory.getAuthor()
            story.status = mStory.getStatus()
            story.species = mStory.getSpecies()
            story.introduce = mStory.getIntroduce()
            story.image = mStory.getImage()
            story.ageLimit = mStory.getAgeLimit()
            story.sumChapter = mStory.getSumChapter()
            story.totalLikes = mStory.getTotalLikes()
            story.totalViews = mStory.getTotalViews()
            story.totalComments = mStory.getTotalComments()
            story.ratePoint = mStory.getRatePoint()
            story.timeUpdate = mStory.getTimeUpdate()
            story.isFollow = if (mStory.getIsFollow()) 1 else 0
            story.chapterReading = mStory.getChapterReading()
            story.newChapter = 0
            mAppDao.insertStory(story)
        }
    }

    private fun downloadChapterRead() {
        if (mAppDao.getSumChapterRead(mStoryId) > 0) {
            return
        }
        Api().apiInterface().getChapterListRead(mStoryId, mAccountId, 0)
            .enqueue(object : Callback<List<ChapterRead>> {
                override fun onResponse(
                    call: Call<List<ChapterRead>>,
                    response: Response<List<ChapterRead>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        for (chapterRead in response.body()!!) {
                            mAppDao.insertChapterRead(chapterRead)
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
        Api().apiInterface().getChapterList(mStoryId).enqueue(object : Callback<List<Chapter>> {
            override fun onResponse(
                call: Call<List<Chapter>>,
                response: Response<List<Chapter>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val chapterList = response.body()
                    var count = 0
                    for (i in 0 until chapterList!!.size) {
                        val chapter = chapterList[i]
                        if (checkChapter(chapter.chapterId)) {
                            chapter.chapterId = chapter.chapterId
                            chapter.storyId = chapter.storyId
                            chapter.chapterNumber = chapter.chapterNumber
                            chapter.chapterName = chapter.chapterName
                            chapter.content = chapter.content
                            chapter.poster = chapter.poster
                            chapter.postDay = chapter.postDay
                            mAppDao.insertChapter(chapter)
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
        return mAppDao.getChapter(mStoryId, chapterId) == null
    }

    private fun sendNotification(count: Int) {
        val icon = if (count < mSumChapter) {
            R.drawable.ic_download
        } else {
            R.drawable.ic_check
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setContentTitle(mStoryName)
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
        Toast.makeText(this, "Tải thành công", Toast.LENGTH_LONG).show()
    }
}
