package huce.fit.appreadstories.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.controller.adapters.ChapterAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterListActivity extends AppCompatActivity {

    private List<ChuongTruyen> listChapter = new ArrayList<>(); //data source
    private List<ChuongTruyen> listChapterRead = new ArrayList<>();
    private ChapterAdapter chapterAdapter;
    private RecyclerView rcViewChapter;
    private ImageView ivBack, ivReverse;
    private SearchView svChapter;
    private ProgressBar pbReload;
    private int idAccount, idStory, idChapter;;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        idStory = getIntent().getIntExtra("idStory", 0);

        pbReload = findViewById(R.id.pbReLoad);
        rcViewChapter = findViewById(R.id.rcViewChapter);
        ivBack = findViewById(R.id.ivBack);
        svChapter = findViewById(R.id.svChapter);
        ivReverse = findViewById(R.id.ivReverse);

        svChapter.setMaxWidth(Integer.MAX_VALUE);

        getSharedPreferences();
        processEvents();
        getDataListChapterRead();
        getDataChapterRead();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idFragmentAccount", String.valueOf(idAccount));
    }

    private void getDataListChapterRead() {
        Api.apiInterface().getListChapterRead(idStory, idAccount, 0).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body().toString() != null) {
                    listChapterRead.clear();
                    listChapterRead.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call, Throwable t) {
                Log.e("Err_ChapterList", t.toString());
            }
        });
    }

    private void getDataChapterRead() {
        Api.apiInterface().getChapterRead(idStory, idAccount, 1).enqueue(new Callback<ChuongTruyen>() {
            @Override
            public void onResponse(Call<ChuongTruyen> call, Response<ChuongTruyen> response) {
                if (response.isSuccessful() && response.body().toString() != null) {
                    rcView(response.body().getMachuong());
                    getData();
                }
            }

            @Override
            public void onFailure(Call<ChuongTruyen> call, Throwable t) {
                Log.e("Err_ChapterList", t.toString());
            }
        });
    }

    private void rcView(int chapterRead) {
        rcViewChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter = new ChapterAdapter(this, listChapter, listChapterRead, chapterRead, (position, view1) -> {
            idChapter = listChapter.get(position).getMachuong();
            Log.e("idchapterclick: ", String.valueOf(idChapter));

            Intent intent = new Intent(this, ChapterReadActivity.class);
            intent.putExtra("idChapter", idChapter);
            intent.putExtra("idStory", idStory);
            startActivity(intent);
            finish();
        });//Đổ dữ liệu lên adpter
        rcViewChapter.setAdapter(chapterAdapter);
        //tạo dòng kẻ ngăn cách các item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcViewChapter.addItemDecoration(itemDecoration);
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> {
            finish();
        });

        svChapter.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    getData();
                } else {
                    getData(newText);
                }
                return false;
            }
        });

        ivReverse.setOnClickListener(view -> {
            Collections.reverse(listChapter);// đảo ngược dánh sách
            rcViewChapter.setAdapter(chapterAdapter);
        });
    }

    private void getData() {
        pbReload.setVisibility(View.VISIBLE);
        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response1) {
                if (response1.isSuccessful() && response1.body().toString() != null) {
                    listChapter.clear();
                    Log.e("body", response1.body().toString());
                    listChapter.addAll(response1.body());
                    chapterAdapter.notifyDataSetChanged();
                    pbReload.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call1, Throwable t1) {
                Log.e("Err_ChapterList", t1.toString());
            }
        });
    }

    private void getData(String numberChapter) {
        pbReload.setVisibility(View.VISIBLE);
        Api.apiInterface().searchChapter(idStory, numberChapter).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body().toString() != null) {
                    listChapter.clear();
                    Log.e("body", response.body().toString());
                    listChapter.addAll(response.body());
                    chapterAdapter.notifyDataSetChanged();
                    pbReload.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call, Throwable t) {
                Log.e("Err_ChapterList", t.toString());
            }
        });
    }
}
