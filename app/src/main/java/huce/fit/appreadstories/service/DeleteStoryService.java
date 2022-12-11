package huce.fit.appreadstories.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Chapter;
import huce.fit.appreadstories.sqlite.ChapterRead;
import huce.fit.appreadstories.sqlite.Story;

public class DeleteStoryService extends Service {
    private int idStory;
    private boolean checkDelete1, checkDelete2, checkDelete3;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("DeleteStoryService", "Delete Story Start");

        if (intent != null) {
            idStory = intent.getIntExtra("idStory", 0);
            Log.e("idStory", String.valueOf(idStory));

            deleteChapterRead();
        }
        return START_NOT_STICKY;
    }

    private void deleteChapterRead() {
        AppDatabase.getInstance(this).appDao().deleteChapterRead(idStory);
        List<ChapterRead> listChapterRead = AppDatabase.getInstance(this).appDao().getAllChapterRead(idStory);

        if (listChapterRead.size() == 0) {
            checkDelete1 = true;
            Log.e("DeleteStoryService", "1");
            stopDownloadService();
        }
    }

    private void deleteChapter() {
        AppDatabase.getInstance(this).appDao().deleteChapter(idStory);
        List<Chapter> listChapter = AppDatabase.getInstance(this).appDao().getAllChapter(idStory);

        if (listChapter.size() == 0) {
            checkDelete2 = true;
            Log.e("DeleteStoryService", "2");
            stopDownloadService();
        }
    }

    private void deleteStory() {
        AppDatabase.getInstance(this).appDao().deleteStory(idStory);
        Story story = AppDatabase.getInstance(this).appDao().getStory(idStory);

        if (story == null) {
            checkDelete3 = true;
            Log.e("DeleteStoryService", "3");
            stopDownloadService();
        }
    }


    private void stopDownloadService() {
        if (checkDelete1 && !checkDelete2 && !checkDelete3) {
            deleteChapter();
        }
        if (checkDelete1 && checkDelete2 && !checkDelete3) {
            deleteStory();
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

        Log.e("DeleteStoryService", "Delete Story Success");
        Toast.makeText(this, "Truyện đã được xóa!!", Toast.LENGTH_SHORT).show();
    }
}
