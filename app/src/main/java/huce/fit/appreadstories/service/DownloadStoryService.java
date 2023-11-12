package huce.fit.appreadstories.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import huce.fit.appreadstories.R;
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
    private String nameStory;
    private boolean isFollow, checkDownLoad1, checkDownLoad2, checkDownLoad3;

    private static final String CHENNAL_ID = "download_story_service";
    private int count = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("DownloadStoryService", "Download Story Start");
        getSharedPreferences();

        if (intent != null) {
            Toast.makeText(this, "Bắt đầu tải về", Toast.LENGTH_LONG).show();

            idStory = intent.getIntExtra("storyId", 0);
            nameStory = intent.getStringExtra("nameStory");
            isFollow = intent.getBooleanExtra("isFollow", false);
            idChapterReading = intent.getIntExtra("idChapterReading", 0);
            Log.e("storyId", String.valueOf(idStory));
            Log.e("nameStory", nameStory);
            Log.e("idChapterReading", String.valueOf(idChapterReading));
            Log.e("isFollow", String.valueOf(isFollow));

            downloadStory(this);
        }
        return START_NOT_STICKY;
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idAccount", String.valueOf(idAccount));
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
                    Log.e("DownloadStoryService", "E_downloadStory", t);
                }
            });
        }

        checkDownLoad1 = true;
        Log.e("DownloadStoryService", "1");
        stopDownloadService();
    }

    private void downloadChapter(Context context) {
        List<Chapter> listChapterOffline = AppDatabase.getInstance(this).appDao().getAllChapter(idStory);

        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChuongTruyen>> call, @NonNull Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (listChapterOffline.size() == 0) {//truyện chưa tải về máy
                        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<ChuongTruyen>> call, @NonNull Response<List<ChuongTruyen>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int sumChapter = response.body().size();

                                    for (int i = 0; i < sumChapter; i++) {
                                        ChuongTruyen c = response.body().get(i);
                                        chapter.setIdChapter(c.getMachuong());
                                        chapter.setIdStory(c.getMatruyen());
                                        chapter.setNumberChapter(c.getSochuong());
                                        chapter.setNameChapter(c.getTenchuong());
                                        chapter.setContent(c.getNoidung());
                                        chapter.setPoster(c.getNguoidang());
                                        chapter.setPostDay(c.getThoigiandang());
                                        AppDatabase.getInstance(context).appDao().insertChapter(chapter);

                                        count++;
                                        sendNotification(count, false);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<ChuongTruyen>> call, @NonNull Throwable t) {
                                Log.e("DownloadStoryService", "E_downloadChapter1", t);
                            }
                        });
                    }
                    if (listChapterOffline.size() > 0) {//truyện đã tải về máy
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

                        int sumChapter = listChapter.size();
                        for (int i = 0; i < sumChapter; i++) {
                            ChuongTruyen ct = listChapter.get(i);

                            chapter.setIdChapter(ct.getMachuong());
                            chapter.setIdStory(ct.getMatruyen());
                            chapter.setNumberChapter(ct.getSochuong());
                            chapter.setNameChapter(ct.getTenchuong());
                            chapter.setContent(ct.getNoidung());
                            chapter.setPoster(ct.getNguoidang());
                            chapter.setPostDay(ct.getThoigiandang());
                            AppDatabase.getInstance(context).appDao().insertChapter(chapter);

                            count++;
                            sendNotification(count, false);

                            story = new Story();
                            int newChapter = story.getNewChapter() - 1;
                            story.setNewChapter(newChapter);
                            AppDatabase.getInstance(context).appDao().updateStory(story);
                        }
                    }

                    checkDownLoad2 = true;
                    Log.e("DownloadStoryService", "2");
                    stopDownloadService();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChuongTruyen>> call, @NonNull Throwable t) {
                Log.e("DownloadStoryService", "E_downloadChapter2", t);
            }
        });

    }

    private void downloadChapterRead(Context context) {
        List<ChapterRead> listChapterRead = AppDatabase.getInstance(context).appDao().getAllChapterRead(idStory);
        if (listChapterRead.size() == 0) {
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
                    Log.e("DownloadStoryService", "E_downloadChapterRead", t);

                }
            });
        }

        checkDownLoad3 = true;
        Log.e("DownloadStoryService", "3");
        stopDownloadService();
    }

    private void sendNotification(int count, boolean finish) {
        int icon;
        if (finish) {
            icon = R.drawable.ic_download;
        } else {
            icon = R.drawable.ic_check;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHENNAL_ID);
        builder.setContentTitle(nameStory)
                .setContentText(String.format(Locale.getDefault(), "Chương tải về: %d", count))
                .setSmallIcon(icon)
                .setOngoing(true)// thông báo không thể loại bỏ khi vẫn còn hoạt động
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void stopDownloadService() {
        if (checkDownLoad1 && !checkDownLoad2 && !checkDownLoad3) {
            downloadChapter(this);
        }
        if (checkDownLoad1 && checkDownLoad2 && !checkDownLoad3) {
            downloadChapterRead(this);
        }
        if (checkDownLoad1 && checkDownLoad2 && checkDownLoad3) {
            sendNotification(count, true);
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

        //tắt notification
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        Log.e("DownloadStoryService", "Download Story Stop");
        Toast.makeText(this, "Tải thành công", Toast.LENGTH_LONG).show();
    }
}
