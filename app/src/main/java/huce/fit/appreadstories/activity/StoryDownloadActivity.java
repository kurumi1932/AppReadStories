package huce.fit.appreadstories.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.adapters.ChapterDownloadActivityAdapter;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.service.DownloadStoryService;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Chapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryDownloadActivity extends AppCompatActivity {
    private LinearLayout llStoryDownload;
    private ImageView ivBack, ivStory;
    private TextView tvStoryName, tvAuthor, tvStatus, tvChapter, tvSpecies;
    private Button btDownloadStory;
    private ProgressBar pbReLoad;

    private int idStory, idChapterReading;
    private boolean isFollow;

    private ChapterDownloadActivityAdapter chapterDownloadActivityAdapter;
    private RecyclerView rcViewChapter;
    private final List<ChuongTruyen> listChapter = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_download);

        idStory = getIntent().getIntExtra("idStory", 0);
        isFollow = getIntent().getBooleanExtra("isFollow", false);
        idChapterReading = getIntent().getIntExtra("idChapterReading", 0);
        Log.e("idStory:", String.valueOf(idStory));
        Log.e("isFollow:", String.valueOf(isFollow));
        Log.e("idChapterRead:", String.valueOf(idChapterReading));

        llStoryDownload = findViewById(R.id.llStoryDownload);
        ivStory = findViewById(R.id.ivStory);
        tvStoryName = findViewById(R.id.tvStoryName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvStatus = findViewById(R.id.tvStatus);
        tvChapter = findViewById(R.id.tvChapter);
        tvSpecies = findViewById(R.id.tvSpecies);
        rcViewChapter = findViewById(R.id.rcViewChapter);

        ivBack = findViewById(R.id.ivBack);
        btDownloadStory = findViewById(R.id.btDownloadStory);
        pbReLoad = findViewById(R.id.pbReLoad);

        hide();
        getDataStory();
        getDataListChapter();
        rcView();
        show();
        processEvents();
    }

    private void hide() {
        llStoryDownload.setVisibility(View.INVISIBLE);
        pbReLoad.setVisibility(View.VISIBLE);
    }

    private void show() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            pbReLoad.setVisibility(View.GONE);
            llStoryDownload.setVisibility(View.VISIBLE);
        }, 2000);
    }

    private void getDataStory() {
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call,@NonNull Response<Truyen> response) {
                Truyen t = response.body();
                if (t != null) {
                    tvStoryName.setText(t.getTentruyen());
                    tvAuthor.setText(t.getTacgia());
                    tvStatus.setText(t.getTrangthai());
                    tvChapter.setText(String.valueOf(t.getTongchuong()));
                    tvSpecies.setText(t.getTheloai());
                    Picasso.get().load(t.getAnh()).into(ivStory);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call,@NonNull Throwable t) {
                Log.e("Err_StoryDownload", "getDataStory", t);
            }
        });
    }

    private void getDataListChapter() {
        List<Chapter> listChapterOffline = AppDatabase.getInstance(this).appDao().getAllChapter(idStory);

        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChuongTruyen>> call,@NonNull Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listChapter.clear();
                    listChapter.addAll(response.body());
                    if (listChapterOffline != null) {
                        for (int i = 0; i < listChapterOffline.size(); i++) {
                            for (int j = 0; j < listChapter.size(); j++) {
                                ChuongTruyen c = listChapter.get(j);
                                if (c.getMachuong() == listChapterOffline.get(i).getIdChapter()) {
                                    listChapter.remove(listChapter.get(j));
                                    break;
                                }
                            }
                        }
                    }
                    chapterDownloadActivityAdapter.notifyDataSetChanged();
                    Log.e("StoryDownloadActivity", "Success_getDataListChapter(1)");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChuongTruyen>> call,@NonNull Throwable t) {
                Log.e("StoryDownloadActivity", "Err_getDataListChapter(1)", t);
            }
        });
    }

    private void rcView() {
        rcViewChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterDownloadActivityAdapter = new ChapterDownloadActivityAdapter(listChapter);//Đổ dữ liệu lên adpter
        rcViewChapter.setAdapter(chapterDownloadActivityAdapter);
        //tạo dòng kẻ ngăn cách các item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcViewChapter.addItemDecoration(itemDecoration);
    }

    private void processEvents() {
        ivBack.setOnClickListener(view -> finish());

        btDownloadStory.setOnClickListener(view -> {
            if (listChapter.size() > 0) {
                startDownloadService();
            }
        });
    }

    // Start the service
    public void startDownloadService() {
        Intent intent = new Intent(StoryDownloadActivity.this, DownloadStoryService.class);
        intent.putExtra("idStory", idStory);
        intent.putExtra("isFollow", isFollow);
        intent.putExtra("idChapterReading", idChapterReading);
        startService(intent);
    }
}
