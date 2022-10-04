package huce.fit.appreadstories.controller.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicInteger;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.controller.adapters.ViewPagerStoryInterfaceAdapter;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.DanhGia;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.model.TruyenTheoDoi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryInterfaceActivity extends AppCompatActivity {
    private ImageView ivBack, ivStory, ivRate1, ivRate2, ivRate3, ivRate4, ivRate5;
    private TextView tvStoryName, tvAuthor, tvStatus, tvChapter1, tvSpecies, tvLike1, tvLike2, tvView, tvChapter2, tvComment, tvRate;
    private Button btReadStory, btFollow, btDownLoad;
    private LinearLayout llRate, llLike, llChapterList, llComment;
    private int idStory, idAccount, idChapter;
    private String name;
    private boolean isView=false;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerStoryInterfaceAdapter viewPagerStoryInterfaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_interface);

        getSharedPreferences();
        idStory = getIntent().getIntExtra("idStory", idStory);
        Log.e("idstory", String.valueOf(idStory));
        readStory();

        ivStory = findViewById(R.id.ivStory);
        tvStoryName = findViewById(R.id.tvStoryName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvStatus = findViewById(R.id.tvStatus);
        tvChapter1 = findViewById(R.id.tvChapter1);
        tvSpecies = findViewById(R.id.tvSpecies);

        tvRate = findViewById(R.id.tvRate);
        llRate = findViewById(R.id.llRate);//chưa làm
        ivRate1 = findViewById(R.id.ivRate1);
        ivRate2 = findViewById(R.id.ivRate2);
        ivRate3 = findViewById(R.id.ivRate3);
        ivRate4 = findViewById(R.id.ivRate4);
        ivRate5 = findViewById(R.id.ivRate5);

        tvLike1 = findViewById(R.id.tvLike1);
        tvLike2 = findViewById(R.id.tvLike2);
        llLike = findViewById(R.id.llLike);

        tvView = findViewById(R.id.tvView);

        tvChapter2 = findViewById(R.id.tvChapter2);
        llChapterList = findViewById(R.id.llChapterList);

        tvComment = findViewById(R.id.tvComment);//chưa làm
        llComment = findViewById(R.id.llComment);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        ivBack = findViewById(R.id.ivBack);
        btReadStory = findViewById(R.id.btReadStory);// chưa làm
        btFollow = findViewById(R.id.btFollow);
        btDownLoad = findViewById(R.id.btDownload);//chưa làm

        viewPagerStoryInterfaceAdapter = new ViewPagerStoryInterfaceAdapter(this, idStory);
        viewPager.setAdapter(viewPagerStoryInterfaceAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Giới thiệu");
                    break;
                case 1:
                    tab.setText("Đánh giá");
                    break;
            }
        }).attach();

        getDataStory();
        checkLikeStory();
        checkFollow();
        processEvents();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        name = sharedPreferences.getString("name", "");
        Log.e("idFragmentAccount", String.valueOf(idAccount));
    }

    private void getDataStory() {
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                Truyen tc = response.body();
                tvStoryName.setText(tc.getTentruyen());
                tvAuthor.setText(tc.getTacgia());
                tvStatus.setText(tc.getTrangthai());
                tvChapter1.setText(String.valueOf(tc.getSochuong()));
                tvSpecies.setText(tc.getTheloai());
                Picasso.get().load(tc.getAnh())
                        .into(ivStory);

                if (tc.getDiemdanhgia() >= 1) {
                    ivRate1.setImageResource(R.drawable.ic_rate);
                }
                if (tc.getDiemdanhgia() >= 2) {
                    ivRate2.setImageResource(R.drawable.ic_rate);
                }
                if (tc.getDiemdanhgia() >= 3) {
                    ivRate3.setImageResource(R.drawable.ic_rate);
                }
                if (tc.getDiemdanhgia() >= 4) {
                    ivRate4.setImageResource(R.drawable.ic_rate);
                }
                if (tc.getDiemdanhgia() == 5) {
                    ivRate5.setImageResource(R.drawable.ic_rate);
                }
                tvRate.setText(String.valueOf(tc.getDiemdanhgia()));
                tvLike1.setText(String.valueOf(tc.getLuotthich()));
                tvView.setText(String.valueOf(tc.getLuotxem()));
                tvChapter2.setText(String.valueOf(tc.getSochuong()));
                tvComment.setText(String.valueOf(tc.getLuotbinhluan()));
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_StoryInterface", t.toString());
            }
        });
    }

    private void checkFollow() {
        Api.apiInterface().checkStoryFollow(idAccount, idStory).enqueue(new Callback<TruyenTheoDoi>() {
            @Override
            public void onResponse(Call<TruyenTheoDoi> call, Response<TruyenTheoDoi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        btFollow.setText("Đang theo dõi");
                        btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_follow, 0, 0);
                        btFollow.setTextColor(getResources().getColor(R.color.medium_sea_green));
                    }
                    if (response.body().getSuccess() == 2) {
                        btFollow.setText("Theo dõi");
                        btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_unfollow, 0, 0);
                        btFollow.setTextColor(getResources().getColor(R.color.dim_gray));
                    }
                }
            }

            @Override
            public void onFailure(Call<TruyenTheoDoi> call, Throwable t) {
                Toast.makeText(StoryInterfaceActivity.this, "Lỗi! Kiểm tra theo dõi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLikeStory() {
        Api.apiInterface().checkLikeStory(idStory, idAccount).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Truyen t = response.body();
                    if (t.getStorysuccess() == 0) {
                        tvLike1.setTextColor(getResources().getColor(R.color.black));
                        tvLike2.setTextColor(getResources().getColor(R.color.dim_gray));
                    } else {
                        tvLike1.setTextColor(getResources().getColor(R.color.orange));
                        tvLike2.setTextColor(getResources().getColor(R.color.orange));
                    }
                    if(t.getChuongdangdoc()>0){
                        isView = true;
                    }else {
                        isView = false;
                    }
                    tvLike1.setText(String.valueOf(t.getLuotthich()));
                }
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_StoryInterface", t.toString());
            }
        });
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> {
            finish();
        });

        llRate.setOnClickListener(v -> {
            if (isView){
                openDialogRate();
            }else {
                Toast.makeText(StoryInterfaceActivity.this, "Bạn chưa đọc truyện, không thể đánh giá!", Toast.LENGTH_SHORT).show();
            }
        });

        llLike.setOnClickListener(v -> {
            likeStory();
        });

        llChapterList.setOnClickListener(v -> {
            Intent intent = new Intent(StoryInterfaceActivity.this, ChapterListActivity.class);
            intent.putExtra("idStory", idStory);
            startActivity(intent);
        });

        llComment.setOnClickListener(v -> {
            if (isView){
                Log.e("idStory: ",String.valueOf(idStory));
                Intent intent = new Intent(StoryInterfaceActivity.this, CommentListActivity.class);
                intent.putExtra("idAccount", idAccount);
                intent.putExtra("idStory", idStory);
                startActivity(intent);
            }else {
                Toast.makeText(StoryInterfaceActivity.this, "Bạn chưa đọc truyện, không thể bình luận!", Toast.LENGTH_SHORT).show();
            }
        });

        btFollow.setOnClickListener(v -> {
            add_delete_StoryFollow();
        });

        btReadStory.setOnClickListener(v -> {
            Intent intent = new Intent(StoryInterfaceActivity.this, ChapterReadActivity.class);
            intent.putExtra("idStory", idStory);
            intent.putExtra("idChapter", idChapter);
            startActivity(intent);
        });
    }

    private void openDialogRate() {

        final Dialog dialogRate = new Dialog(this);
        dialogRate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRate.setContentView(R.layout.dialog_rate);

        Window window = dialogRate.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // true click bên ngoài dialog có thể tắt dialog
        dialogRate.setCancelable(false);

        ImageView ivClose = dialogRate.findViewById(R.id.ivClose);
        ImageView ivRate1 = dialogRate.findViewById(R.id.ivRate1);
        ImageView ivRate2 = dialogRate.findViewById(R.id.ivRate2);
        ImageView ivRate3 = dialogRate.findViewById(R.id.ivRate3);
        ImageView ivRate4 = dialogRate.findViewById(R.id.ivRate4);
        ImageView ivRate5 = dialogRate.findViewById(R.id.ivRate5);
        EditText etRate = dialogRate.findViewById(R.id.etRate);
        TextView tvNumberTextRate = dialogRate.findViewById(R.id.tvNumberTextRate);
        Button btRate = dialogRate.findViewById(R.id.btRate);

        AtomicInteger idRate,pointRate;
        idRate = new AtomicInteger();
        pointRate = new AtomicInteger();
        Api.apiInterface().checkRateOfAccount(idStory, idAccount).enqueue(new Callback<DanhGia>() {
            @Override
            public void onResponse(Call<DanhGia> call, Response<DanhGia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DanhGia dg = response.body();
                    if (dg.getRatesuccess() == 1) {
                        idRate.set(dg.getMadanhgia());
                        pointRate.set(dg.getDiemdanhgia());
                        if (dg.getDiemdanhgia() >= 1) {
                            ivRate1.setImageResource(R.drawable.ic_rate);
                        }
                        if (dg.getDiemdanhgia() >= 2) {
                            ivRate2.setImageResource(R.drawable.ic_rate);
                        }
                        if (dg.getDiemdanhgia() >= 3) {
                            ivRate3.setImageResource(R.drawable.ic_rate);
                        }
                        if (dg.getDiemdanhgia() >= 4) {
                            ivRate4.setImageResource(R.drawable.ic_rate);
                        }
                        if (dg.getDiemdanhgia() == 5) {
                            ivRate5.setImageResource(R.drawable.ic_rate);
                        }
                        etRate.setText(dg.getDanhgia());
                        tvNumberTextRate.setText(dg.getDanhgia().length() + "/400");
                    }
                }
            }

            @Override
            public void onFailure(Call<DanhGia> call, Throwable t) {
                Log.e("Err_DialogRate", t.toString());
            }
        });

        ivClose.setOnClickListener(view -> {
            dialogRate.dismiss();
        });
        ivRate1.setOnClickListener(view -> {
            pointRate.set(1);
            ivRate1.setImageResource(R.drawable.ic_rate);
            ivRate2.setImageResource(R.drawable.ic_not_rate);
            ivRate3.setImageResource(R.drawable.ic_not_rate);
            ivRate4.setImageResource(R.drawable.ic_not_rate);
            ivRate5.setImageResource(R.drawable.ic_not_rate);
        });
        ivRate2.setOnClickListener(view -> {
            pointRate.set(2);
            ivRate1.setImageResource(R.drawable.ic_rate);
            ivRate2.setImageResource(R.drawable.ic_rate);
            ivRate3.setImageResource(R.drawable.ic_not_rate);
            ivRate4.setImageResource(R.drawable.ic_not_rate);
            ivRate5.setImageResource(R.drawable.ic_not_rate);
        });
        ivRate3.setOnClickListener(view -> {
            pointRate.set(3);
            ivRate1.setImageResource(R.drawable.ic_rate);
            ivRate2.setImageResource(R.drawable.ic_rate);
            ivRate3.setImageResource(R.drawable.ic_rate);
            ivRate4.setImageResource(R.drawable.ic_not_rate);
            ivRate5.setImageResource(R.drawable.ic_not_rate);
        });
        ivRate4.setOnClickListener(view -> {
            pointRate.set(4);
            ivRate1.setImageResource(R.drawable.ic_rate);
            ivRate2.setImageResource(R.drawable.ic_rate);
            ivRate3.setImageResource(R.drawable.ic_rate);
            ivRate4.setImageResource(R.drawable.ic_rate);
            ivRate5.setImageResource(R.drawable.ic_not_rate);
        });
        ivRate5.setOnClickListener(view -> {
            pointRate.set(5);
            ivRate1.setImageResource(R.drawable.ic_rate);
            ivRate2.setImageResource(R.drawable.ic_rate);
            ivRate3.setImageResource(R.drawable.ic_rate);
            ivRate4.setImageResource(R.drawable.ic_rate);
            ivRate5.setImageResource(R.drawable.ic_rate);
        });

        //event
        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etRate.getLineCount()>7){
                    Toast.makeText(StoryInterfaceActivity.this, "Vượt quá sô dòng giới hạn đánh giá!", Toast.LENGTH_SHORT).show();
                    etRate.getText().delete(etRate.getText().length() - 1, etRate.getText().length());
                }
                tvNumberTextRate.setText(s.toString().length() + "/400");
            }
        });
        btRate.setOnClickListener(view -> {
            if (idRate.get()>0) {
                Api.apiInterface().updateRate(idRate.get(), pointRate.get(),etRate.getText().toString()).enqueue(new Callback<DanhGia>() {
                    @Override
                    public void onResponse(Call<DanhGia> call, Response<DanhGia> response) {
                        if (response.body().getRatesuccess() == 1) {
                            getDataStory();
                            Toast.makeText(StoryInterfaceActivity.this, "Bạn đã đánh giá truyện!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DanhGia> call, Throwable t) {
                        Log.e("Err_DialogRate", t.toString());
                    }
                });
                dialogRate.dismiss();
            } else {
                if (pointRate.get() > 0) {
                    Api.apiInterface().addRate(idStory, idAccount, name, pointRate.get(), etRate.getText().toString()).enqueue(new Callback<DanhGia>() {
                        @Override
                        public void onResponse(Call<DanhGia> call, Response<DanhGia> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getRatesuccess() == 1) {
                                    getDataStory();
                                    Toast.makeText(StoryInterfaceActivity.this, "Bạn đã đánh giá truyện!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DanhGia> call, Throwable t) {
                            Log.e("Err_DialogRate", t.toString());
                        }
                    });
                    dialogRate.dismiss();
                } else {
                    Toast.makeText(StoryInterfaceActivity.this, "Vui lòng cho điểm dánh giá!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogRate.show();
    }

    private void likeStory() {
        Api.apiInterface().likeStory(idStory, idAccount).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStorysuccess() == 1) {
                        checkLikeStory();
                    }
                }
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_StoryInterface", t.toString());
            }
        });
    }

    private void readStory() {
        Api.apiInterface().firstChapter(idStory).enqueue(new Callback<ChuongTruyen>() {
            @Override
            public void onResponse(Call<ChuongTruyen> call, Response<ChuongTruyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    idChapter = response.body().getMachuong();
                }
            }

            @Override
            public void onFailure(Call<ChuongTruyen> call, Throwable t) {
                Log.e("Err_StoryInterface", t.toString());
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
                                    btFollow.setText("Đang theo dõi");
                                    btFollow.setTextColor(getResources().getColor(R.color.medium_sea_green));
                                    btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_follow, 0, 0);
                                }
                                if (response1.body().getSuccess() == 2) {
                                    btFollow.setText("Theo dõi");
                                    btFollow.setTextColor(getResources().getColor(R.color.dim_gray));
                                    btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_unfollow, 0, 0);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TruyenTheoDoi> call1, Throwable t1) {
                            Log.e("Err_StoryInterface", t1.toString());
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Truyen> call, Throwable t) {
                Log.e("Err_StoryInterface", t.toString());
            }
        });
    }

}