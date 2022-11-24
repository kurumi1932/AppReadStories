package huce.fit.appreadstories.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Chapter;
import huce.fit.appreadstories.sqlite.ChapterRead;
import huce.fit.appreadstories.sqlite.Story;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadService extends Service {

    private Story story = new Story();
    private Chapter chapter = new Chapter();
    private ChapterRead chapterRead = new ChapterRead();
    private int idAccount, idStory, idChapterReading;
    private boolean isFollow, checkDownLoad1, checkDownLoad2, checkDownLoad3;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idAccount", String.valueOf(idAccount));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getSharedPreferences();
        if (intent != null) {
            idStory = intent.getIntExtra("idStory", 0);
            isFollow = intent.getBooleanExtra("isFollow", false);
            idChapterReading = intent.getIntExtra("idChapterReading", 0);

            Log.e("idStory", String.valueOf(idStory));
            Log.e("idChapterReading", String.valueOf(idChapterReading));
            Log.e("isFollow", String.valueOf(isFollow));

            downloadStory(this);
        }

        Toast.makeText(this, "Download Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    private void downloadStory(Context context) {
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                Truyen t = response.body();

                if (t != null) {
                    story.setIdStory(t.getMatruyen());
                    story.setNameStory(t.getTentruyen());
                    story.setAuthor(t.getTacgia());
                    story.setSumChapter(t.getTongchuong());
                    story.setStatus(t.getTrangthai());
                    story.setSpecies(t.getTheloai());
                    story.setTimeUpdate(t.getThoigiancapnhat());
                    story.setImage(t.getAnh());
                    story.setIntroduce(t.getGioithieu());
                    story.setRate(t.getDiemdanhgia());
                    story.setLike(t.getLuotthich());
                    story.setView(t.getLuotxem());
                    story.setSumComment(t.getLuotbinhluan());
                    if (isFollow) {
                        story.setIsFollow(1);
                    } else {
                        story.setIsFollow(0);
                    }
                    story.setChapterReading(idChapterReading);

                    AppDatabase.getInstance(context).appDao().insertStory(story);
                    checkDownLoad1 = true;
                    stopDownloadService();
                }
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_DownloadService", "downloadStory", t);
            }
        });
    }

    private void downloadChapter(Context context) {
        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        ChuongTruyen c = response.body().get(i);
                        chapter.setIdChapter(c.getMachuong());
                        chapter.setIdStory(c.getMatruyen());
                        chapter.setSumChapter(c.getSochuong());
                        chapter.setNameChapter(c.getTenchuong());
                        chapter.setContent(c.getNoidung());
                        chapter.setPoster(c.getNguoidang());
                        chapter.setTimePost(c.getThoigiandang());
                        AppDatabase.getInstance(context).appDao().insertChapter(chapter);
                    }
                    checkDownLoad2 = true;
                    stopDownloadService();
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call1, Throwable t) {
                Log.e("Err_DownloadService", "downloadChapter", t);
            }
        });
    }

    private void downloadChapterRead(Context context) {
        Api.apiInterface().getListChapterRead(idStory, idAccount, 0).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body().toString() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        ChuongTruyen c = response.body().get(i);
                        chapterRead.setIdStory(c.getMatruyen());
                        chapterRead.setIdChapter(c.getMachuong());
                        AppDatabase.getInstance(context).appDao().insertChapterRead(chapterRead);
                    }
                    checkDownLoad3 = true;
                    stopDownloadService();
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call, Throwable t) {
                Log.e("Err_DownloadService", "downloadChapterRead", t);
            }
        });
    }

    private void stopDownloadService() {
        if (checkDownLoad1 && !checkDownLoad2 && !checkDownLoad3) {
            downloadChapter(this);
        }
        if (checkDownLoad1 && checkDownLoad2 && !checkDownLoad3) {
            downloadChapterRead(this);
        }
        if (checkDownLoad1 && checkDownLoad2 && checkDownLoad3) {
            onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stopping the player when service is destroyed
        Toast.makeText(this, "Download Success", Toast.LENGTH_LONG).show();
    }
}
