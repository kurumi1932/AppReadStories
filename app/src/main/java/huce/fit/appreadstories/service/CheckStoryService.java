package huce.fit.appreadstories.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class CheckStoryService extends Service {
    private int idAccount, idStory;
    private boolean isFollow, checkStory1, checkStory2, checkStory3;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("CheckStoryService", "Check Story Start");
        getSharedPreferences();

        if (intent != null) {
            Toast.makeText(this, "Kiểm tra cập nhật của truyện!", Toast.LENGTH_LONG).show();

            idStory = intent.getIntExtra("idStory", 0);
            isFollow = intent.getBooleanExtra("isFollow", false);
            Log.e("idStory", String.valueOf(idStory));
            Log.e("isFollow", String.valueOf(isFollow));

            updateStoryServer(this);
        }
        return START_STICKY;
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idAccount", String.valueOf(idAccount));
    }

    private void updateStoryServer(Context context) {
        Story story = AppDatabase.getInstance(context).appDao().getStory(idStory);
        if (story != null) {
            int idChapterReading = story.getChapterReading();
            if (idChapterReading > 0) {
                Api.apiInterface().getChapter(idStory, idChapterReading, 2, idAccount).enqueue(new Callback<ChuongTruyen>() {
                    @Override
                    public void onResponse(@NonNull Call<ChuongTruyen> call, @NonNull Response<ChuongTruyen> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("CheckStoryService", "S_updateStoryServer");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ChuongTruyen> call, @NonNull Throwable t) {
                        Log.e("CheckStoryService", "E_updateStoryServer", t);
                    }
                });
            }
        }

        checkStory1 = true;
        Log.e("CheckStoryService", "1");
        stopCheckStoryService();
    }

    private void updateStoryDownload(Context context) {
        Story story = AppDatabase.getInstance(context).appDao().getStory(idStory);
        List<Chapter> listChapterDownload = AppDatabase.getInstance(context).appDao().getAllChapter(idStory);

        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Truyen t = response.body();
                    int newChapter = response.body().getTongchuong() - listChapterDownload.size();
                    Log.e("newChapter", String.valueOf(newChapter));

                    story.setNameStory(t.getTentruyen());
                    story.setAuthor(t.getTacgia());
                    story.setAge(t.getGioihantuoi());
                    story.setSumChapter(t.getTongchuong());
                    story.setNewChapter(newChapter);
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
                    AppDatabase.getInstance(context).appDao().updateStory(story);

                    checkStory2 = true;
                    Log.e("CheckStoryService", "2");
                    stopCheckStoryService();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("CheckStoryService", "E_updateStoryDownload", t);
            }
        });
    }

    private void updateChapterReadServer() {
        List<ChapterRead> listChapterRead = AppDatabase.getInstance(this).appDao().getAllChapterRead(idStory);

        Api.apiInterface().getListChapterRead(idStory, idAccount, 0).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChuongTruyen>> call, @NonNull Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        for (int j = 0; j < listChapterRead.size(); j++) {
                            if (response.body().get(i).getMachuong() == listChapterRead.get(j).getIdChapter()) {//kiểm tra chương đã đọc trong máy vơi server
                                listChapterRead.remove(j);
                                break;
                            }
                            if (j == (listChapterRead.size() - 1)) {// cuối vòng lặp mà id vẫn khác nhau thì insert chapterRead lên server
                                if (response.body().get(i).getMachuong() != listChapterRead.get(j).getIdChapter()) {
                                    Api.apiInterface().getChapter(idStory, listChapterRead.get(j).getIdChapter(), 2, idAccount).enqueue(new Callback<ChuongTruyen>() {
                                        @Override
                                        public void onResponse(@NonNull Call<ChuongTruyen> call, @NonNull Response<ChuongTruyen> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                Log.e("CheckStoryService", "S_updateChapterReadServer");
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<ChuongTruyen> call, @NonNull Throwable t) {
                                            Log.e("CheckStoryService", "E_updateChapterReadServer1", t);
                                        }
                                    });
                                }
                            }
                        }
                    }
                    checkStory3 = true;
                    Log.e("CheckStoryService", "3");
                    stopCheckStoryService();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChuongTruyen>> call, @NonNull Throwable t) {
                Log.e("CheckStoryService", "E_updateChapterReadServer2", t);
            }
        });
    }

    private void stopCheckStoryService() {
        if (checkStory1 && !checkStory2 && !checkStory3) {
            updateStoryDownload(this);
        }
        if (checkStory1 && checkStory2 && !checkStory3) {
            updateChapterReadServer();
        }
        if (checkStory1 && checkStory2 && checkStory3) {
            onDestroy();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkStory1 = false;
        checkStory2 = false;
        checkStory3 = false;

        Log.e("CheckStoryService", "Check Story Stop");
        Toast.makeText(this, "Kiểm tra hoàn tất", Toast.LENGTH_LONG).show();
    }
}
