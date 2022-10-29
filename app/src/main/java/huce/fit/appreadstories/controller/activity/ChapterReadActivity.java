package huce.fit.appreadstories.controller.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.controller.adapters.ChapterDialogAdapter;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.Truyen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterReadActivity extends AppCompatActivity {
    private ConstraintLayout constrainLayout;
    private BottomNavigationView btNavigationView;
    private ProgressBar pbReLoad;
    private TextView tvNumberChapter, tvChapterName, tvPostPerson, tvPostDay, tvContent;
    private int idAccount, idStory, idChapter;
    private AtomicInteger status = new AtomicInteger(1);
    Handler handler = new Handler();

    public void setIdChapter(int idChapter) {
        this.idChapter = idChapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_read);

        getSharedPreferences();

        Intent intent = getIntent();
        idStory = intent.getIntExtra("idStory", 0);
        idChapter = intent.getIntExtra("idChapterReading", 0);

        constrainLayout = findViewById(R.id.constrainLayout);
        btNavigationView = findViewById(R.id.btNavigationView);

        pbReLoad = findViewById(R.id.pbReLoad);
        tvNumberChapter = findViewById(R.id.tvNumberChapter);
        tvChapterName = findViewById(R.id.tvChapterName);
        tvPostPerson = findViewById(R.id.tvPostPerson);
        tvPostDay = findViewById(R.id.tvPostDay);
        tvContent = findViewById(R.id.tvContent);

        btNavigationView.setVisibility(View.GONE);
        constrainLayout.setVisibility(View.GONE);

        getData( 2, "Không tìm thấy chương!");
        processEvents();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idFragmentAccount",String.valueOf(idAccount));
    }

    public void getData(int chapter_change, String text) {
        Log.e("1: ", String.valueOf(idStory));
        Log.e("2: ", String.valueOf(idChapter));
        Log.e("3: ", String.valueOf(chapter_change));
        Log.e("4: ", String.valueOf(idAccount));
        pbReLoad.setVisibility(View.VISIBLE);
        Api.apiInterface().getChapter(idStory, idChapter, chapter_change, idAccount).enqueue(new Callback<ChuongTruyen>() {
            @Override
            public void onResponse(Call<ChuongTruyen> call, Response<ChuongTruyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pbReLoad.setVisibility(View.GONE);
                    ChuongTruyen chuong = response.body();
                    if (chuong.getMachuong() != 0) {
                        tvNumberChapter.setText(chuong.getSochuong());
                        tvChapterName.setText(chuong.getTenchuong());
                        tvPostPerson.setText(chuong.getNguoidang());
                        tvPostDay.setText(chuong.getThoigiandang());
                        tvContent.setText(chuong.getNoidung());
                        setIdChapter(chuong.getMachuong());

                        constrainLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ChapterReadActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChuongTruyen> call, Throwable t) {
                Log.e("E120_ChapterRead", t.toString());
            }
        });
    }

    private void processEvents() {
        btNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btMenuBack:
                    finish();
                    break;
                case R.id.btMenuListChapter:
                    handler.postDelayed(() -> {
                        openDialogListChapter();
                    }, 300);
                    break;
                case R.id.btMenuComment:
                    Intent intent2 = new Intent(ChapterReadActivity.this, CommentListActivity.class);
                    intent2.putExtra("idStory", idStory);
                    startActivity(intent2);
                    break;
                case R.id.btMenuPrevious:
                    Log.e("idChapter_pre.", "previous");
                    getData(1, "Đây là chương đầu!");
                    break;
                case R.id.btMenuNext:
                    Log.e("idChapter_pre.", "next");
                    pbReLoad.setVisibility(View.VISIBLE);
                    getData(3, "Đây là chương cuối!");
                    break;
                default:
                    break;
            }
            return true;
        });

        //ẩn hiện btnavigation
        constrainLayout.setOnClickListener(v -> {
            status.getAndIncrement();
            if (status.get() % 2 != 0) {
                btNavigationView.setVisibility(View.GONE);
                Log.e("statusHide:", String.valueOf(status.get()));
            } else {
                btNavigationView.setVisibility(View.VISIBLE);
                Log.e("statusShow:", String.valueOf(status.get()));
            }
        });
    }

    private void openDialogListChapter() {
        List<ChuongTruyen> listChapter = new ArrayList<>();
        ChapterDialogAdapter chapterDialogAdapter;
        RecyclerView rcViewChapter;

        status.getAndIncrement();
        btNavigationView.setVisibility(View.GONE);

        final Dialog dialogListChapter = new Dialog(this);
        dialogListChapter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogListChapter.setContentView(R.layout.dialog_chapter_list);
        //hiệu ứng di chuyển dialog
        dialogListChapter.getWindow().getAttributes().windowAnimations = R.style.DialogChapterListAnimation;

        Window window = dialogListChapter.getWindow();
        if (window == null) {
            return;
        }

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        //setLayout
        windowAttributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        // vị trí dialog
        windowAttributes.gravity = Gravity.LEFT;
        //hiệu ứng di chuyển dialog
//        windowAttributes.windowAnimations = R.style.DialogChapterListAnimation;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
//        dialogListChapter.setCancelable(true);
        dialogListChapter.setCanceledOnTouchOutside(true);

        ImageView ivStory = dialogListChapter.findViewById(R.id.ivStory);
        ImageView ivReverse = dialogListChapter.findViewById(R.id.ivReverse);
        TextView tvStoryName = dialogListChapter.findViewById(R.id.tvStoryName);
        TextView tvAuthor = dialogListChapter.findViewById(R.id.tvAuthor);
        SearchView svChapter = dialogListChapter.findViewById(R.id.svChapter);

        //getDataStory
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                Truyen tc = response.body();
                tvStoryName.setText(tc.getTentruyen());
                tvAuthor.setText(tc.getTacgia());
                Picasso.get().load(tc.getAnh())
                        .into(ivStory);
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("E235_DialogChapterList", t.toString());
            }
        });

        //RecyclerView
        rcViewChapter = dialogListChapter.findViewById(R.id.rcListChapterOnChapterRead);
        rcViewChapter.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chapterDialogAdapter = new ChapterDialogAdapter(listChapter, (position, view1) -> {
            pbReLoad.setVisibility(View.VISIBLE);
            Log.e("getIdChapter", String.valueOf(listChapter.get(position).getMachuong()));
            setIdChapter(listChapter.get(position).getMachuong());

            handler.postDelayed(() -> {
                getData(2, "Không tìm thấy chương!");
                pbReLoad.setVisibility(View.GONE);
            }, 1500);

            dialogListChapter.dismiss();
        });//Đổ dữ liệu lên adpter
        rcViewChapter.setAdapter(chapterDialogAdapter);
        //tạo dòng kẻ ngăn cách các item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        rcViewChapter.addItemDecoration(itemDecoration);

        //getDataListChapter
        Api.apiInterface().getListChapter(idStory).enqueue(new Callback<List<ChuongTruyen>>() {
            @Override
            public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                if (response.isSuccessful() && response.body().toString() != null) {
                    listChapter.clear();
                    listChapter.addAll(response.body());
                    chapterDialogAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ChuongTruyen>> call, Throwable t) {
                Log.e("E272_DialogChapterList", t.toString());
            }
        });

        //event
        ivReverse.setOnClickListener(view -> {
            Collections.reverse(listChapter);// đảo ngược dánh sách
            rcViewChapter.setAdapter(chapterDialogAdapter);
        });

        svChapter.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Api.apiInterface().searchChapter(idStory, newText).enqueue(new Callback<List<ChuongTruyen>>() {
                    @Override
                    public void onResponse(Call<List<ChuongTruyen>> call, Response<List<ChuongTruyen>> response) {
                        if (response.isSuccessful() && response.body().toString() != null) {
                            listChapter.clear();
                            listChapter.addAll(response.body());
                            chapterDialogAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ChuongTruyen>> call, Throwable t) {
                        Log.e("E302_DialogChapterList", t.toString());
                    }
                });
                return false;
            }
        });

        dialogListChapter.show();
    }
}
