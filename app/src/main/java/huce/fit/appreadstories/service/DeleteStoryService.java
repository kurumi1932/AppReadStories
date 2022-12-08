package huce.fit.appreadstories.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import huce.fit.appreadstories.sqlite.AppDatabase;

public class DeleteStoryService extends Service {
    private int idStory;
    private boolean checkDelete1, checkDelete2, checkDelete3;
    private final Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            idStory = intent.getIntExtra("idStory", 0);
            Log.e("idStory", String.valueOf(idStory));
            Log.e("DeleteStoryService", "Delete Started");
            deleteChapterRead();
        }
        return START_NOT_STICKY;
    }

    private void deleteChapterRead() {
        AppDatabase.getInstance(this).appDao().deleteChapterRead(idStory);
        checkDelete1 = true;
        Log.e("DeleteStoryService", "1");
        stopDownloadService();
    }

    private void deleteChapter() {
        AppDatabase.getInstance(this).appDao().deleteChapter(idStory);
        checkDelete2 = true;
        Log.e("DeleteStoryService", "2");
        stopDownloadService();
    }

    private void deleteStory() {
        AppDatabase.getInstance(this).appDao().deleteStory(idStory);
        checkDelete3 = true;
        Log.e("DeleteStoryService", "3");
        stopDownloadService();
    }


    private void stopDownloadService() {
        if (checkDelete1 && !checkDelete2 && !checkDelete3) {
            handler.postDelayed(this::deleteChapter, 1500);
        }
        if (checkDelete1 && checkDelete2 && !checkDelete3) {
            handler.postDelayed(this::deleteStory, 2000);
        }
        if (checkDelete1 && checkDelete2 && checkDelete3) {
            onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkDelete1 = false;
        checkDelete2 = false;
        checkDelete3 = false;
        Log.e("DeleteStoryService", "Delete Success");
        Toast.makeText(this, "Truyện đã được xóa!!", Toast.LENGTH_SHORT).show();
    }
}
