package huce.fit.appreadstories.controller.activity;

import android.content.Intent;
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
    private ChapterAdapter chapterAdapter;
    private RecyclerView rcViewChapter;
    private ImageView ivBack;
    private SearchView svChapter;
    private ProgressBar pbReload;

    private int idStory;
    private int idChapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        idStory = getIntent().getIntExtra("idStory", 0);

        pbReload = findViewById(R.id.pbReLoad);
        rcViewChapter = findViewById(R.id.rcViewChapter);
        ivBack = findViewById(R.id.ivBack);
        svChapter = findViewById(R.id.svChapter);

        svChapter.setMaxWidth(Integer.MAX_VALUE);

        processEvents();
        getData();
        rcView();
    }

    private void rcView() {
        rcViewChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter = new ChapterAdapter(listChapter, (position, view1) -> {
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
                getData(newText);
                return false;
            }
        });
    }

    private void getData() {
        pbReload.setVisibility(View.VISIBLE);
        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
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
