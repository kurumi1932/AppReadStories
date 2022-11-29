package huce.fit.appreadstories.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
    private int idAccount, idStory, idChapterReading;
    private boolean isFollow, checkStory1, checkStory2, checkStory3;

    @Nullable
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

            updateStoryDownload(this);
            Toast.makeText(this, "Bắt đầu kiểm tra cập nhật của truyện!", Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
    }

    private void updateStoryServer(Context context) {
        Story story = AppDatabase.getInstance(context).appDao().getStory(idStory);

        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>(){

            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {

            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {

            }
        });
    }

    private void updateStoryDownload(Context context) {
        Story story = new Story();
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                Truyen t = response.body();
                if (t != null) {
                    story.setNameStory(t.getTentruyen());
                    story.setAuthor(t.getTacgia());
                    story.setAge(t.getGioihantuoi());
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
                    checkStory1 = true;
                    stopCheckStoryService();
                }
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_CheckStoryService", "updateStoryDownload", t);
            }
        });
    }

    private void updateChapter(Context context) {
        List<Chapter> listChapter = AppDatabase.getInstance(context).appDao().getAllChapter(idStory);
        Chapter chapter = new Chapter();

        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (int i = 0; i < listChapter.size(); i++) {
                        for (int j = 0; j < response.body().size(); j++) {
                            ChuongTruyen c = response.body().get(j);
                            if (c.getMachuong() == listChapter.get(i).getIdChapter()) {
                                response.body().remove(i);
                                break;
                            }
                            if (j == (response.body().size() - 1)) {// cuối vòng lặp mà id vẫn khác nhau thì insert chapter vào room database
                                if (c.getMachuong() != listChapter.get(i).getIdChapter()) {
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
                    }
                    checkStory2 = true;
                    stopCheckStoryService();
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call1, Throwable t) {
                Log.e("Err_CheckStoryService", "updateChapter", t);
            }
        });
    }

    private void updateChapterRead(Context context) {
        List<ChapterRead> listChapterRead = AppDatabase.getInstance(context).appDao().getAllChapterRead(idStory);

        Api.apiInterface().getListChapterRead(idStory, idAccount, 0).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body().toString() != null) {
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
                                        public void onResponse(Call<ChuongTruyen> call, Response<ChuongTruyen> response) {
                                            if (response.isSuccessful() && response.body().toString() != null) {
                                                checkStory3 = true;
                                                stopCheckStoryService();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ChuongTruyen> call, Throwable t) {
                                            Log.e("Err_CheckStoryService", "updateChapterRead", t);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call, Throwable t) {
                Log.e("Err_CheckStoryService", "updateChapterRead", t);
            }
        });
    }

    private void stopCheckStoryService() {
        if (checkStory1 && !checkStory2 && !checkStory3) {
            updateChapter(this);
        }
        if (checkStory1 && checkStory2 && !checkStory3) {
            updateChapterRead(this);
        }
        if (checkStory1 && checkStory2 && checkStory3) {
            onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Kiểm tra cập nhật của truyện hoàn tất!", Toast.LENGTH_LONG).show();
    }
}
