package huce.fit.appreadstories.story.information;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.List;

import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.Chapter;
import huce.fit.appreadstories.model.ChapterRead;
import huce.fit.appreadstories.model.Story;
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences;
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences;
import huce.fit.appreadstories.sqlite.AppDao;
import huce.fit.appreadstories.sqlite.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckStoryService extends Service {
    private static final String TAG = "CheckStoryService";
    private AppDao mAppDao;
    private Story mStoryDao;
    private StorySharedPreferences mStory;
    private int mAccountId, mStoryId;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Kiểm tra cập nhật của truyện!", Toast.LENGTH_LONG).show();
        Log.e(TAG, "NHT CheckStoryService: start");
        getAccount();
        getStory();
        return START_STICKY;
    }

    private void getAccount() {
        AccountSharedPreferences account = new AccountSharedPreferences(this);
        account.getSharedPreferences("Account", Context.MODE_PRIVATE);
        mAccountId = account.getAccountId();
    }

    private void getStory() {
        mStory = new StorySharedPreferences(this);
        mStory.getSharedPreferences("Story", Context.MODE_PRIVATE);
        mStoryId = mStory.getStoryId();

        mAppDao = AppDatabase.getInstance(this).appDao();
        mStoryDao = mAppDao.getStory(mStoryId);
        if (mStoryDao != null) {
            updateStoryServer();
            updateStoryDownload();
            updateChapterReadServer();
        } else {
            onDestroy();
        }
    }

    private void updateStoryServer() {
        int chapterReadingId = mStoryDao.getChapterReading();
        if (chapterReadingId > 0) {
            new Api().apiInterface().getChapter(mStoryId, chapterReadingId, 2, mAccountId).enqueue(new Callback<Chapter>() {
                @Override
                public void onResponse(@NonNull Call<Chapter> call, @NonNull Response<Chapter> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e(TAG, "NHT updateStoryServer: success");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Chapter> call, @NonNull Throwable t) {
                    Log.e(TAG, "NHT updateStoryServer: fail");
                }
            });
        }
    }

    private void updateStoryDownload() {
        int sizeChapterList = mAppDao.getSumChapter(mStoryId);
        int newChapter = mStory.getSumChapter() - sizeChapterList;

        mStoryDao.setStoryId(mStoryId);
        mStoryDao.setStoryName(mStory.getStoryName());
        mStoryDao.setAuthor(mStory.getAuthor());
        mStoryDao.setStatus(mStory.getStatus());
        mStoryDao.setSpecies(mStory.getSpecies());
        mStoryDao.setIntroduce(mStory.getIntroduce());
        mStoryDao.setImage(mStory.getImage());
        mStoryDao.setAgeLimit(mStory.getAgeLimit());
        mStoryDao.setSumChapter(mStory.getSumChapter());
        mStoryDao.setTotalLikes(mStory.getTotalLikes());
        mStoryDao.setTotalViews(mStory.getTotalViews());
        mStoryDao.setTotalComments(mStory.getTotalComments());
        mStoryDao.setRatePoint(mStory.getRatePoint());
        mStoryDao.setTimeUpdate(mStory.getTimeUpdate());
        mStoryDao.setIsFollow(mStory.getIsFollow() ? 1 : 0);
        mStoryDao.setChapterReading(mStory.getChapterReading());
        mStoryDao.setNewChapter(newChapter);

        mAppDao.updateStory(mStoryDao);
    }

    private void updateChapterReadServer() {
        List<ChapterRead> chapterReadList = mAppDao.getChapterReadList(mStoryId);

        new Api().apiInterface().getChapterListRead(mStoryId, mAccountId, 0).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ChapterRead>> call, @NonNull Response<List<ChapterRead>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chapterReadList.removeAll(new HashSet<>(response.body()));
                    for (ChapterRead chapterRead : chapterReadList) {
                        addChapterReadServer(chapterRead.getChapterId());
                    }
                }
                Log.e(TAG, "NHT updateChapterReadServer: success");
                onDestroy();
            }

            @Override
            public void onFailure(@NonNull Call<List<ChapterRead>> call, @NonNull Throwable t) {
                Log.e(TAG, "NHT updateChapterReadServer: fail");
            }
        });
    }

    private void addChapterReadServer(int chapterId) {
        new Api().apiInterface().getChapter(mStoryId, chapterId, 2, mAccountId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Chapter> call, @NonNull Response<Chapter> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "NHT addChapterReadServer: success");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Chapter> call, @NonNull Throwable t) {
                Log.e(TAG, "NHT addChapterReadServer: fail");
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "NHT CheckStoryService: stop");
        Toast.makeText(this, "Cập nhật hoàn tất", Toast.LENGTH_LONG).show();
    }
}
