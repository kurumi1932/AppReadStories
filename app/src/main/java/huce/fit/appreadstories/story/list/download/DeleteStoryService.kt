package huce.fit.appreadstories.story.list.download;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import huce.fit.appreadstories.sqlite.AppDao;
import huce.fit.appreadstories.sqlite.AppDatabase;

public class DeleteStoryService extends Service {
    private static final String TAG = "DeleteStoryService";
    private AppDao mAppDao;
    private int mStoryId;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "NHT DeleteStoryService: start");
        if (intent != null) {
            mStoryId = intent.getIntExtra("storyId", 0);
            deleteStory();
        }
        return START_NOT_STICKY;
    }

    private void deleteStory() {
        mAppDao = AppDatabase.getInstance(this).appDao();
        mAppDao.deleteChapterRead(mStoryId);
        new Handler().postDelayed(() -> mAppDao.deleteChapter(mStoryId), 700);
        new Handler().postDelayed(() -> mAppDao.deleteStory(mStoryId), 1000);
        new Handler().postDelayed(this::onDestroy,1100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "NHT DeleteStoryService: stop");
        Toast.makeText(this, "Truyện đã được xóa!!", Toast.LENGTH_SHORT).show();
    }
}
