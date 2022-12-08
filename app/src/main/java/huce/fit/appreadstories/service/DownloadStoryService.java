package huce.fit.appreadstories.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
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

public class DownloadStoryService extends Service {
    private Story story;
    private final Chapter chapter = new Chapter();
    private final ChapterRead chapterRead = new ChapterRead();
    private int idAccount, idStory, idChapterReading;
    private boolean isFollow, checkDownLoad1, checkDownLoad2, checkDownLoad3;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
            Toast.makeText(this, "Download Started", Toast.LENGTH_LONG).show();
        }
        return START_NOT_STICKY;
    }

    private void downloadStory(Context context) {
        story = AppDatabase.getInstance(context).appDao().getStory(idStory);
        if (story == null) {
            story = new Story();
            Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
                @Override
                public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                    Truyen t = response.body();

                    if (t != null) {
                        story.setIdStory(t.getMatruyen());
                        story.setNameStory(t.getTentruyen());
                        story.setAuthor(t.getTacgia());
                        story.setAge(t.getGioihantuoi());
                        story.setSumChapter(t.getTongchuong());
                        story.setNewChapter(0);
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
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                    Log.e("Err_DownloadService", "downloadStory", t);
                }
            });
        }

        checkDownLoad1 = true;
        stopDownloadService();
    }

    private void downloadChapter(Context context) {
        List<Chapter> listChapterOffline = AppDatabase.getInstance(this).appDao().getAllChapter(idStory);

        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChuongTruyen>> call, @NonNull Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (listChapterOffline == null) {
                        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<ChuongTruyen>> call, @NonNull Response<List<ChuongTruyen>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    for (int i = 0; i < response.body().size(); i++) {
                                        ChuongTruyen c = response.body().get(i);
                                        chapter.setIdChapter(c.getMachuong());
                                        chapter.setIdStory(c.getMatruyen());
                                        chapter.setNumberChapter(c.getSochuong());
                                        chapter.setNameChapter(c.getTenchuong());
                                        chapter.setContent(c.getNoidung());
                                        chapter.setPoster(c.getNguoidang());
                                        chapter.setPostDay(c.getThoigiandang());
                                        AppDatabase.getInstance(context).appDao().insertChapter(chapter);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<ChuongTruyen>> call, @NonNull Throwable t) {
                                Log.e("Err_DownloadService", "downloadChapter", t);
                            }
                        });
                    }
                    if (listChapterOffline != null) {
                        List<ChuongTruyen> listChapter = new ArrayList<>(response.body());
                        for (int i = 0; i < listChapterOffline.size(); i++) {
                            for (int j = 0; j < listChapter.size(); j++) {
                                ChuongTruyen c = listChapter.get(j);
                                if (c.getMachuong() == listChapterOffline.get(i).getIdChapter()) {
                                    listChapter.remove(listChapter.get(j));
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < listChapter.size(); i++) {
                            ChuongTruyen ct = listChapter.get(i);

                            chapter.setIdChapter(ct.getMachuong());
                            chapter.setIdStory(ct.getMatruyen());
                            chapter.setNumberChapter(ct.getSochuong());
                            chapter.setNameChapter(ct.getTenchuong());
                            chapter.setContent(ct.getNoidung());
                            chapter.setPoster(ct.getNguoidang());
                            chapter.setPostDay(ct.getThoigiandang());
                            AppDatabase.getInstance(context).appDao().insertChapter(chapter);

                            Story story = new Story();
                            story.setNewChapter(0);
                            AppDatabase.getInstance(context).appDao().updateStory(story);
                        }
                    }

                    checkDownLoad2 = true;
                    stopDownloadService();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChuongTruyen>> call, @NonNull Throwable t) {
                Log.e("Err_DownloadService", "downloadChapter", t);
            }
        });

    }


    private void downloadChapterRead(Context context) {
        List<ChapterRead> listChapterRead = AppDatabase.getInstance(context).appDao().getAllChapterRead(idStory);
        if (listChapterRead == null) {
            Api.apiInterface().getListChapterRead(idStory, idAccount, 0).enqueue(new Callback<List<ChuongTruyen>>() {
                @Override
                public void onResponse(@NonNull Call<List<ChuongTruyen>> call, @NonNull Response<List<ChuongTruyen>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            ChuongTruyen c = response.body().get(i);
                            chapterRead.setIdStory(idStory);
                            chapterRead.setIdChapter(c.getMachuong());
                            AppDatabase.getInstance(context).appDao().insertChapterRead(chapterRead);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ChuongTruyen>> call, @NonNull Throwable t) {
                    Log.e("Err_DownloadService", "downloadChapterRead", t);
                }
            });
        }
        checkDownLoad3 = true;
        stopDownloadService();
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
        checkDownLoad1 = false;
        checkDownLoad2 = false;
        checkDownLoad3 = false;
        // Stopping the player when service is destroyed
        Toast.makeText(this, "Download Success", Toast.LENGTH_LONG).show();
    }
}
