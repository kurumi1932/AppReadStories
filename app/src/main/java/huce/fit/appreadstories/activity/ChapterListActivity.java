package huce.fit.appreadstories.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.adapters.ChapterAdapter;
import huce.fit.appreadstories.adapters.ChapterDownloadAdapter;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Chapter;
import huce.fit.appreadstories.sqlite.ChapterRead;
import huce.fit.appreadstories.sqlite.Story;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterListActivity extends AppCompatActivity {

    private List<ChuongTruyen> listChapter;
    private List<ChuongTruyen> listChapterRead;
    private List<Chapter> listChapter_Download;
    private List<ChapterRead> listChapterRead_Download;
    private ChapterAdapter chapterAdapter;
    private RecyclerView rcViewChapter;
    private ImageView ivBack, ivReverse;
    private SearchView svChapter;
    private ProgressBar pbReload;
    private int idAccount, idStory, idChapterReading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        getSharedPreferences();
        idStory = getIntent().getIntExtra("idStory", 0);

        pbReload = findViewById(R.id.pbReLoad);
        rcViewChapter = findViewById(R.id.rcViewChapter);
        ivBack = findViewById(R.id.ivBack);
        svChapter = findViewById(R.id.svChapter);
        ivReverse = findViewById(R.id.ivReverse);

        svChapter.setMaxWidth(Integer.MAX_VALUE);

        listChapter = new ArrayList<>();
        listChapterRead = new ArrayList<>();

        getDataListChapter();
        getDataListChapterRead();
        getDataChapterReading();

        processEvents();
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(this);
        return checkNetwork.isNetwork();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idAccount", String.valueOf(idAccount));
    }

    private void getDataListChapter() {
        pbReload.setVisibility(View.VISIBLE);
        if (isNetwork()) {
            Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
                @Override
                public void onResponse(@NonNull Call<List<ChuongTruyen>> call,@NonNull Response<List<ChuongTruyen>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        listChapter.clear();
                        listChapter.addAll(response.body());

                        if (chapterAdapter != null) {
                            chapterAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ChuongTruyen>> call,@NonNull Throwable t) {
                    Log.e("Err_ChapterList", "getDataListChapter", t);
                }
            });
        } else {
            listChapter_Download = AppDatabase.getInstance(this).appDao().getAllChapter(idStory);
            rcView_Download(idChapterReading);
        }
        pbReload.setVisibility(View.GONE);
    }

    private void getDataListChapterRead() {
        if (isNetwork()) {
            Api.apiInterface().getListChapterRead(idStory, idAccount, 0).enqueue(new Callback<List<ChuongTruyen>>() {
                @Override
                public void onResponse(@NonNull Call<List<ChuongTruyen>> call,@NonNull Response<List<ChuongTruyen>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        listChapterRead.clear();
                        listChapterRead.addAll(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ChuongTruyen>> call,@NonNull Throwable t) {
                    Log.e("Err_ChapterList", "getDataListChapterRead", t);
                }
            });
        } else {
            listChapterRead_Download = AppDatabase.getInstance(this).appDao().getAllChapterRead(idStory);
        }
    }

    private void getDataChapterReading() {
        if (isNetwork()) {
            Api.apiInterface().getChapterReading(idStory, idAccount, 1).enqueue(new Callback<ChuongTruyen>() {
                @Override
                public void onResponse(@NonNull Call<ChuongTruyen> call,@NonNull Response<ChuongTruyen> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        idChapterReading = response.body().getMachuong();
                        rcView(idChapterReading);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ChuongTruyen> call,@NonNull Throwable t) {
                    Log.e("Err_ChapterList", "getDataChapterReading", t);
                }
            });
        } else {
            Story story = AppDatabase.getInstance(this).appDao().getStory(idStory);
            idChapterReading = story.getChapterReading();
            rcView_Download(idChapterReading);
        }
    }

    private void rcView(int id_ChapterReading) {
        rcViewChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter = new ChapterAdapter(this, listChapter, listChapterRead, id_ChapterReading, (position, view1, isLongClick) -> {
//            Log.e("idchapterclick: ", String.valueOf(position));
            Intent intent = new Intent(this, ChapterReadActivity.class);
            intent.putExtra("idChapterReading", position);
            intent.putExtra("idStory", idStory);
            startActivity(intent);
            finish();
        });//Đổ dữ liệu lên adpter
        rcViewChapter.setAdapter(chapterAdapter);
    }

    private void rcView_Download(int id_ChapterReading) {
        rcViewChapter.setLayoutManager(new LinearLayoutManager(this));
        ChapterDownloadAdapter chapterDownloadAdapter = new ChapterDownloadAdapter(this, listChapter_Download, listChapterRead_Download, id_ChapterReading, (position, view1, isLongClick) -> {

            Intent intent = new Intent(this, ChapterReadActivity.class);
            intent.putExtra("idChapterReading", position);
            intent.putExtra("idStory", idStory);
            startActivity(intent);
            finish();
        });//Đổ dữ liệu lên adpter
        rcViewChapter.setAdapter(chapterDownloadAdapter);
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> finish());

        svChapter.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getDataSearchChapter(newText.trim());
                return false;
            }
        });

        ivReverse.setOnClickListener(view -> {
            if (isNetwork()) {
                Collections.reverse(listChapter);// đảo ngược dánh sách

                //Đầu trang
                LinearLayoutManager llmanager = (LinearLayoutManager) rcViewChapter.getLayoutManager();
                if (llmanager != null) {
                    llmanager.scrollToPosition(0);
                }

                rcView(idChapterReading);
            } else {
                Collections.reverse(listChapter_Download);// đảo ngược dánh sách

                //Đầu trang
                LinearLayoutManager llmanager = (LinearLayoutManager) rcViewChapter.getLayoutManager();
                if (llmanager != null) {
                    llmanager.scrollToPosition(0);
                }

                rcView_Download(idChapterReading);
            }
        });
    }

    private void getDataSearchChapter(String numberChapter) {
        pbReload.setVisibility(View.VISIBLE);
        if (numberChapter.equals("")) {
            getDataListChapter();
        } else {
            if (isNetwork()) {
                Api.apiInterface().searchChapter(idStory, numberChapter).enqueue(new Callback<List<ChuongTruyen>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ChuongTruyen>> call,@NonNull Response<List<ChuongTruyen>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            listChapter.clear();
                            listChapter.addAll(response.body());

                            chapterAdapter.notifyDataSetChanged();
                            pbReload.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ChuongTruyen>> call,@NonNull Throwable t) {
                        Log.e("Err_ChapterList", "getDataSearchChapter", t);
                    }
                });
            } else {
                Log.e("search", "oke");
                listChapter_Download = AppDatabase.getInstance(this).appDao().getAllChapterByNumberChapter(idStory, numberChapter);
                rcView_Download(idChapterReading);
                pbReload.setVisibility(View.GONE);
            }
        }
    }
}
