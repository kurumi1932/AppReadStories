package huce.fit.appreadstories.controller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.model.TruyenTheoDoi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryInterfaceActivity extends AppCompatActivity {
    private ImageView ivBack, ivStory;
    private TextView tvStoryName, tvAuthor, tvStatus, tvChapter, tvSpecies, tvIntroduce;
    private Button btListChapter, btReadStory, btFollow, btComment;
    private int idStory, idAccount, idChapter;
    private String checkFollow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_interface);

        getSharedPreferences();
        idStory = getIntent().getIntExtra("idStory", idStory);
        Log.e("idstory", String.valueOf(idStory));
        readStory(idStory);

        ivBack = findViewById(R.id.ivBack);
        ivStory = findViewById(R.id.ivStory);
        tvStoryName = findViewById(R.id.tvStoryName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvStatus = findViewById(R.id.tvStatus);
        tvChapter = findViewById(R.id.tvChapter);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvIntroduce = findViewById(R.id.tvIntroduce);

        btListChapter = findViewById(R.id.btListChapter);
        btReadStory = findViewById(R.id.btReadStory);// chưa làm
        btFollow = findViewById(R.id.btFollow);
        btComment = findViewById(R.id.btComment);

        checkFollow();
        getData();
        processEvents();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idFragmentAccount", String.valueOf(idAccount));
    }

    private void checkFollow() {
        Api.apiInterface().checkStoryFollow(idAccount, idStory).enqueue(new Callback<TruyenTheoDoi>() {
            @Override
            public void onResponse(Call<TruyenTheoDoi> call, Response<TruyenTheoDoi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        checkFollow = "Đang theo dõi";
                    }
                    if (response.body().getSuccess() == 2) {
                        checkFollow = "Theo dõi";
                    }
                    btFollow.setText(checkFollow);
                }
            }

            @Override
            public void onFailure(Call<TruyenTheoDoi> call, Throwable t) {
                Toast.makeText(StoryInterfaceActivity.this, "Lỗi! Kiểm tra theo dõi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                Truyen tc = response.body();
                tvStoryName.setText(tc.getTentruyen());
                tvAuthor.setText(tc.getTacgia());
                tvStatus.setText(tc.getTrangthai());
                tvChapter.setText(String.valueOf(tc.getSochuong()));
                if (tc.getTheloai().equals(1)) {
                    tvSpecies.setText("Đô thị");
                } else if (tc.getTheloai().equals(2)) {
                    tvSpecies.setText("Tu tiên");
                } else if (tc.getTheloai().equals(3)) {
                    tvSpecies.setText("Mạt thế");
                } else if (tc.getTheloai().equals(4)) {
                    tvSpecies.setText("Trùng sinh");
                } else {
                    tvSpecies.setText("Ngôn tình");
                }
                tvIntroduce.setText(tc.getGioithieu());
                Picasso.get().load(tc.getAnh())
                        .into(ivStory);
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Toast.makeText(StoryInterfaceActivity.this, "Không tìm thấy truyện!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processEvents() {
        try {
            ivBack.setOnClickListener(v -> {
                finish();
            });
            btReadStory.setOnClickListener(v -> {
                Intent intent = new Intent(StoryInterfaceActivity.this, ChapterReadActivity.class);
                intent.putExtra("idStory", idStory);
                intent.putExtra("idChapter", idChapter);
                startActivity(intent);
            });
            btListChapter.setOnClickListener(v -> {
                Intent intent = new Intent(StoryInterfaceActivity.this, ChapterListActivity.class);
                intent.putExtra("idStory", idStory);
                startActivity(intent);
            });
            btFollow.setOnClickListener(v -> {
                add_delete_StoryFollow();
            });
            btComment.setOnClickListener(v -> {
                Intent intent = new Intent(StoryInterfaceActivity.this, CommentListActivity.class);
                intent.putExtra("idAccount", idAccount);
                intent.putExtra("idStory", idStory);
                startActivity(intent);
            });
        } catch (Exception ex) {
            Log.e("Events", ex.getMessage());
        }
    }

    private void readStory(int idStory){
        Api.apiInterface().firstChapter(idStory).enqueue(new Callback<ChuongTruyen>() {
            @Override
            public void onResponse(Call<ChuongTruyen> call, Response<ChuongTruyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    idChapter = response.body().getMachuong();
                }
            }

            @Override
            public void onFailure(Call<ChuongTruyen> call, Throwable t) {
                Log.e("Err_StoryInterface",t.toString());
            }
        });
    }

    private void add_delete_StoryFollow() {
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Truyen tc = response.body();
                    Api.apiInterface().add_delete_StoryFollow(idAccount, idStory, tc.getTentruyen(), tc.getTacgia(), tc.getTrangthai(), tc.getSochuong(), tc.getAnh()).enqueue(new Callback<TruyenTheoDoi>() {
                        @Override
                        public void onResponse(Call<TruyenTheoDoi> call1, Response<TruyenTheoDoi> response1) {
                            if (response1.isSuccessful() && response1.body() != null) {
                                if (response1.body().getSuccess() == 1) {
                                    checkFollow = "Đang theo dõi";
                                    Toast.makeText(StoryInterfaceActivity.this, "Đã theo dõi!", Toast.LENGTH_SHORT).show();
                                }
                                if (response1.body().getSuccess() == 2) {
                                    checkFollow = "Theo dõi";
                                    Toast.makeText(StoryInterfaceActivity.this, "Đã hủy theo dõi!", Toast.LENGTH_SHORT).show();
                                }
                                btFollow.setText(checkFollow);
                            }
                        }

                        @Override
                        public void onFailure(Call<TruyenTheoDoi> call1, Throwable t1) {
                            Log.e("Err_StoryInterface",t1.toString());
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_StoryInterface",t.toString());
            }
        });
    }

}