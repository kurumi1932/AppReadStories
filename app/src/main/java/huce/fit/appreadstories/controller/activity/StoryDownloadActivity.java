package huce.fit.appreadstories.controller.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.controller.adapters.ChapterDownloadAdapter;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Chapter;
import huce.fit.appreadstories.sqlite.Story;
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
    private ChapterDownloadAdapter chapterDownloadAdapter;
    private RecyclerView rcViewChapter;
    private List<ChuongTruyen> listChapter = new ArrayList<>();

    private Story story = new Story();
    private Chapter chapter = new Chapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_download);

        idStory = getIntent().getIntExtra("idStory", 0);
        isFollow = getIntent().getBooleanExtra("isFollow", false);
        idChapterReading = getIntent().getIntExtra("idChapterReading", 0);
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
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                Truyen t = response.body();
                if (t != null) {
                    tvStoryName.setText(t.getTentruyen());
                    tvAuthor.setText(t.getTacgia());
                    tvStatus.setText(t.getTrangthai());
                    tvChapter.setText(String.valueOf(t.getSochuong()));
                    tvSpecies.setText(t.getTheloai());
                    Picasso.get().load(t.getAnh()).into(ivStory);
                }
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_StoryDownload", "getDataStory", t);
            }
        });
    }

    private void getDataListChapter() {
        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listChapter.clear();
                    listChapter.addAll(response.body());
                    chapterDownloadAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call1, Throwable t) {
                Log.e("Err_StoryDownload", "getDataListChapter", t);
            }
        });
    }

    private void rcView() {
        rcViewChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterDownloadAdapter = new ChapterDownloadAdapter(listChapter);//Đổ dữ liệu lên adpter
        rcViewChapter.setAdapter(chapterDownloadAdapter);
        //tạo dòng kẻ ngăn cách các item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcViewChapter.addItemDecoration(itemDecoration);
    }

    private void processEvents() {
        ivBack.setOnClickListener(view -> finish());
        btDownloadStory.setOnClickListener(view -> {
            downloadStory(this);
            downloadChapter(this);
            Toast.makeText(StoryDownloadActivity.this, "Bạn đã tải truyện về máy!", Toast.LENGTH_SHORT).show();
        });
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
                    story.setSumChapter(t.getSochuong());
                    story.setStatus(t.getTrangthai());
                    story.setSpecies(t.getTheloai());
                    story.setTimeUpdate(t.getThoigiancapnhat());
                    story.setImage(t.getAnh());
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
                }
                AppDatabase.getInstance(context).appDao().insertStory(story);
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_StoryDownload", "downloadStory", t);
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
                        chapter.setNumberChapter(c.getSochuong());
                        chapter.setNameChapter(c.getTenchuong());
                        chapter.setContent(c.getNoidung());
                        chapter.setPoster(c.getNguoidang());
                        chapter.setTimePost(c.getThoigiandang());
                        AppDatabase.getInstance(context).appDao().insertChapter(chapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call1, Throwable t) {
                Log.e("Err_StoryDownload", "downloadChapter", t);
            }
        });
    }
}
