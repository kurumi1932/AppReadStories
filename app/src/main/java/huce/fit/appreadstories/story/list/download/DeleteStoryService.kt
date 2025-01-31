package huce.fit.appreadstories.story.list.download

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import huce.fit.appreadstories.sqlite.AppDatabase
import huce.fit.appreadstories.util.AppUtil
import kotlinx.coroutines.runBlocking

class DeleteStoryService: Service() {

    companion object{
        private const val TAG: String = "DeleteStoryService"
    }

    private var storyId = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "NHT DeleteStoryService: start")
        if (intent != null) {
            storyId = intent.getIntExtra("storyId", 0)
            deleteStory()
        } else {
            onDestroy()
        }
        return START_NOT_STICKY
    }

    private fun deleteStory() {
        val mAppDao = AppDatabase.getInstance(this).appDao()
        mAppDao.deleteChapterRead(storyId)
        runBlocking {
            mAppDao.deleteChapter(storyId)
            mAppDao.deleteStory(storyId)
            onDestroy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "NHT DeleteStoryService: stop")
        AppUtil.setToast(this, "Truyện đã được xóa!!")
    }
}
