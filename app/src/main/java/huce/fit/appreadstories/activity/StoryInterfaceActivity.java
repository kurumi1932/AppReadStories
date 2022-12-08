package huce.fit.appreadstories.activity;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.adapters.ViewPagerStoryInterfaceAdapter;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.DanhGia;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.model.TruyenTheoDoi;
import huce.fit.appreadstories.service.CheckStoryService;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Story;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryInterfaceActivity extends AppCompatActivity {
    private ImageView ivBack, ivStory, ivRate1, ivRate2, ivRate3, ivRate4, ivRate5;
    private TextView tvStoryName, tvAuthor, tvStatus, tvSpecies, tvLike, tvView, tvChapter, tvComment, tvRate;
    private Button btReadStory, btFollow, btDownLoad;
    private LinearLayout llRate;
    private int idStory, idAccount, idChapterReading;
    private String name;
    private boolean isRate = false, isComment = false, isFollow = false;
    private double progress;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerStoryInterfaceAdapter viewPagerStoryInterfaceAdapter;
    private Story story;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_interface);

        getSharedPreferences();
        idStory = getIntent().getIntExtra("idStory", 0);

        ivStory = findViewById(R.id.ivStory);
        tvStoryName = findViewById(R.id.tvStoryName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvStatus = findViewById(R.id.tvStatus);
        tvSpecies = findViewById(R.id.tvSpecies);

        tvRate = findViewById(R.id.tvRate);
        llRate = findViewById(R.id.llRate);
        ivRate1 = findViewById(R.id.ivRate1);
        ivRate2 = findViewById(R.id.ivRate2);
        ivRate3 = findViewById(R.id.ivRate3);
        ivRate4 = findViewById(R.id.ivRate4);
        ivRate5 = findViewById(R.id.ivRate5);

        tvLike = findViewById(R.id.tvLike);
        tvView = findViewById(R.id.tvView);
        tvChapter = findViewById(R.id.tvChapter);
        tvComment = findViewById(R.id.tvComment);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        ivBack = findViewById(R.id.ivBack);
        btReadStory = findViewById(R.id.btReadStory);
        btFollow = findViewById(R.id.btFollow);
        btDownLoad = findViewById(R.id.btDownload);

        story = AppDatabase.getInstance(this).appDao().getStory(idStory);
        if (isNetwork() && story != null) {
            startCheckStoryService();
        }
        getDataViewPager();
        processEvents();
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(this);
        return checkNetwork.isNetwork();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetwork()) {
            getDataStory();
            readStory();
            checkFollow();
            checkInteractive();// kiểm tra tương tác
            checkAge();
        } else {
            Story story = AppDatabase.getInstance(this).appDao().getStory(idStory);
            if (story == null) {
                finish();
            } else {
                getDataStory_Download();
                readStory_Download();
                checkFollow_Download();
            }
        }
    }

    private void startCheckStoryService() {
        Log.e("StoryInterfaceActivity", "startCheckStoryService");
        Intent intent = new Intent(StoryInterfaceActivity.this, CheckStoryService.class);
        intent.putExtra("idStory", idStory);
        intent.putExtra("isFollow", isFollow);
        startService(intent);
        Log.e("intent", intent.toString());
    }

    @SuppressLint("SetTextI18n")
    private void openDialogNotifyYesNo() {
        final Dialog dialogNotify = new Dialog(this);
        dialogNotify.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNotify.setContentView(R.layout.dialog_notify_yes_no);

        Window window = dialogNotify.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        dialogNotify.setCancelable(true);

        TextView tvTitle, tvContent, tvYes, tvNo;
        tvTitle = dialogNotify.findViewById(R.id.tvTitle);
        tvContent = dialogNotify.findViewById(R.id.tvContent);
        tvYes = dialogNotify.findViewById(R.id.tvYes);
        tvNo = dialogNotify.findViewById(R.id.tvNo);

        tvTitle.setText("Cảnh báo nội dung");
        tvContent.setText("Truyện bạn muốn xem có chứa nội dung nhạy cảm, phù hợp với lứa tuổi 18 trở lên.Hãy cân nhắc trước khi tiếp tục.");

        tvYes.setOnClickListener(view -> dialogNotify.dismiss());
        tvNo.setOnClickListener(view -> {
            dialogNotify.dismiss();
            finish();
        });

        dialogNotify.show();
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
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                Truyen t = response.body();
                if (t != null) {
                    tvStoryName.setText(t.getTentruyen());
                    tvAuthor.setText(t.getTacgia());
                    tvStatus.setText(t.getTrangthai());
                    tvSpecies.setText(t.getTheloai());

                    Picasso.get().load(t.getAnh())
                            .into(ivStory);

                    if (t.getDiemdanhgia() == 0) {
                        ivRate1.setImageResource(R.drawable.ic_not_rate);
                        ivRate2.setImageResource(R.drawable.ic_not_rate);
                        ivRate3.setImageResource(R.drawable.ic_not_rate);
                        ivRate4.setImageResource(R.drawable.ic_not_rate);
                        ivRate5.setImageResource(R.drawable.ic_not_rate);
                    }
                    if (t.getDiemdanhgia() >= 1) {
                        ivRate1.setImageResource(R.drawable.ic_rate);
                    }
                    if (t.getDiemdanhgia() >= 2) {
                        ivRate2.setImageResource(R.drawable.ic_rate);
                    }
                    if (t.getDiemdanhgia() >= 3) {
                        ivRate3.setImageResource(R.drawable.ic_rate);
                    }
                    if (t.getDiemdanhgia() >= 4) {
                        ivRate4.setImageResource(R.drawable.ic_rate);
                    }
                    if (t.getDiemdanhgia() == 5) {
                        ivRate5.setImageResource(R.drawable.ic_rate);
                    }
                    tvRate.setText(String.valueOf(t.getDiemdanhgia()));
                    tvLike.setText(String.format(Locale.getDefault(), "%d\nYêu thích", t.getLuotthich()));
                    tvView.setText(String.format(Locale.getDefault(), "%d\nLượt xem", t.getLuotxem()));
                    tvChapter.setText(String.format(Locale.getDefault(), "%s\nChương", t.getTongchuong()));
                    tvComment.setText(String.format(Locale.getDefault(), "%s\nBình luận", t.getLuotbinhluan()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_getDataStory", t);
            }
        });
    }

    private void getDataStory_Download() {
        tvStoryName.setText(story.getNameStory());
        tvAuthor.setText(story.getAuthor());
        tvStatus.setText(story.getStatus());
        tvSpecies.setText(story.getSpecies());
        Picasso.get().load(story.getImage()).into(ivStory);

        if (story.getRate() == 0) {
            ivRate1.setImageResource(R.drawable.ic_not_rate);
            ivRate2.setImageResource(R.drawable.ic_not_rate);
            ivRate3.setImageResource(R.drawable.ic_not_rate);
            ivRate4.setImageResource(R.drawable.ic_not_rate);
            ivRate5.setImageResource(R.drawable.ic_not_rate);
        }
        if (story.getRate() >= 1) {
            ivRate1.setImageResource(R.drawable.ic_rate);
        }
        if (story.getRate() >= 2) {
            ivRate2.setImageResource(R.drawable.ic_rate);
        }
        if (story.getRate() >= 3) {
            ivRate3.setImageResource(R.drawable.ic_rate);
        }
        if (story.getRate() >= 4) {
            ivRate4.setImageResource(R.drawable.ic_rate);
        }
        if (story.getRate() == 5) {
            ivRate5.setImageResource(R.drawable.ic_rate);
        }
        tvRate.setText(String.valueOf(story.getRate()));
        tvLike.setText(String.format(Locale.getDefault(), "%d\nYêu thích", story.getLike()));
        tvView.setText(String.format(Locale.getDefault(), "%d\nLượt xem", story.getView()));
        tvChapter.setText(String.format(Locale.getDefault(), "%d\nChương", story.getSumChapter()));
        tvComment.setText(String.format(Locale.getDefault(), "%d\nBình luận", story.getSumComment()));
    }

    private void getDataViewPager() {
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
    }

    private void checkFollow() {
        Api.apiInterface().checkStoryFollow(idAccount, idStory).enqueue(new Callback<TruyenTheoDoi>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<TruyenTheoDoi> call, @NonNull Response<TruyenTheoDoi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        isFollow = true;
                        btFollow.setText("Đang theo dõi");
                        btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_follow, 0, 0);
                        btFollow.setTextColor(getResources().getColor(R.color.medium_sea_green));
                    }
                    if (response.body().getSuccess() == 2) {
                        isFollow = false;
                        btFollow.setText("Theo dõi");
                        btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_unfollow, 0, 0);
                        btFollow.setTextColor(getResources().getColor(R.color.dim_gray));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TruyenTheoDoi> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_checkFollow", t);
            }
        });
    }

    private void checkAge() {
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Truyen tc = response.body();
                    if (tc.getGioihantuoi() == 18) {
                        openDialogNotifyYesNo();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_add_delete_StoryFollow", t);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void checkFollow_Download() {
        int follow = story.getIsFollow();
        if (follow == 1) {
            isFollow = true;
            btFollow.setText("Đang theo dõi");
            btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_follow, 0, 0);
            btFollow.setTextColor(getResources().getColor(R.color.medium_sea_green));
        }
        if (follow == 0) {
            btFollow.setText("Theo dõi");
            btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_unfollow, 0, 0);
            btFollow.setTextColor(getResources().getColor(R.color.dim_gray));
        }
    }

    private void checkInteractive() {
        Api.apiInterface().checkLikeStory(idStory, idAccount).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Truyen t = response.body();
                    if (t.getStorysuccess() == 0) {
                        tvLike.setTextColor(getResources().getColor(R.color.black));
                    } else {
                        tvLike.setTextColor(getResources().getColor(R.color.orange));
                    }

                    progress = Double.parseDouble(t.getTylechuongdadoc());
                    if (progress >= 5) {
                        isRate = false;
                        isComment = true;
                    }
                    if (progress >= 20) {
                        isRate = true;
                    }
                    if (progress < 5) {
                        isRate = false;
                        isComment = false;
                    }

                    tvLike.setText(String.format(Locale.getDefault(), "%d\nYêu thích", t.getLuotthich()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_checkLikeStory", t);
            }
        });
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> finish());

        if (isNetwork()) {
            llRate.setOnClickListener(v -> {
                if (isRate) {
                    openDialogRate();
                } else {
                    Toast.makeText(StoryInterfaceActivity.this, "Tiến độ đọc truyện chưa đạt 20%\n\tKhông thể đánh giá!", Toast.LENGTH_SHORT).show();
                }
            });

            tvLike.setOnClickListener(v -> likeStory());

            tvComment.setOnClickListener(v -> {
                if (isComment) {
                    Log.e("idStory: ", String.valueOf(idStory));
                    Intent intent = new Intent(StoryInterfaceActivity.this, CommentListActivity.class);
                    intent.putExtra("idAccount", idAccount);
                    intent.putExtra("idStory", idStory);
                    startActivity(intent);
                } else {
                    Toast.makeText(StoryInterfaceActivity.this, "Tiến độ đọc truyện chưa đạt 5%\n\tKhông thể bình luận!", Toast.LENGTH_SHORT).show();
                }
            });

            btFollow.setOnClickListener(v -> add_delete_StoryFollow());

            btDownLoad.setOnClickListener(v -> {
                Intent intent = new Intent(StoryInterfaceActivity.this, StoryDownloadActivity.class);
                intent.putExtra("idStory", idStory);
                intent.putExtra("isFollow", isFollow);
                intent.putExtra("idChapterReading", idChapterReading);
                startActivity(intent);
            });
        }

        tvChapter.setOnClickListener(v -> {
            Intent intent = new Intent(StoryInterfaceActivity.this, ChapterListActivity.class);
            intent.putExtra("idStory", idStory);
            startActivity(intent);
        });

        btReadStory.setOnClickListener(v -> {
            Intent intent = new Intent(StoryInterfaceActivity.this, ChapterReadActivity.class);
            intent.putExtra("idStory", idStory);
            intent.putExtra("idChapterReading", idChapterReading);
            startActivity(intent);
        });
    }

    private void likeStory() {
        Api.apiInterface().likeStory(idStory, idAccount).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStorysuccess() == 1) {
                        checkInteractive();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_likeStory", t);
            }
        });
    }

    private void readStory() {
        Api.apiInterface().getChapterReading(idStory, idAccount, 1).enqueue(new Callback<ChuongTruyen>() {
            @Override
            public void onResponse(@NonNull Call<ChuongTruyen> call, @NonNull Response<ChuongTruyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int id = response.body().getMachuong();
                    if (id != 0) {
                        idChapterReading = response.body().getMachuong();
                    }
                    if (id == 0) {
                        Api.apiInterface().firstChapter(idStory).enqueue(new Callback<ChuongTruyen>() {
                            @Override
                            public void onResponse(@NonNull Call<ChuongTruyen> call, @NonNull Response<ChuongTruyen> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    idChapterReading = response.body().getMachuong();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ChuongTruyen> call, @NonNull Throwable t) {
                                Log.e("StoryInterface", "Err_readStory", t);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChuongTruyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_readStory", t);
            }
        });
    }

    private void readStory_Download() {
        idChapterReading = story.getChapterReading();
    }

    private void add_delete_StoryFollow() {
        Api.apiInterface().getStory(idStory).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Truyen tc = response.body();
                    Api.apiInterface().add_delete_StoryFollow(idAccount, idStory, tc.getTentruyen(), tc.getTacgia(), tc.getGioihantuoi(), tc.getTrangthai(), tc.getTongchuong(), tc.getAnh(), tc.getThoigiancapnhat()).enqueue(new Callback<TruyenTheoDoi>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(@NonNull Call<TruyenTheoDoi> call1, @NonNull Response<TruyenTheoDoi> response1) {
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
                        public void onFailure(@NonNull Call<TruyenTheoDoi> call1, @NonNull Throwable t1) {
                            Log.e("StoryInterface", "Err_add_delete_StoryFollow1", t1);
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_add_delete_StoryFollow", t);
            }
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

        ImageView ivClose, ivRate1, ivRate2, ivRate3, ivRate4, ivRate5;
        Button btRate, btDeleteRate;

        ivClose = dialogRate.findViewById(R.id.ivClose);
        ivRate1 = dialogRate.findViewById(R.id.ivRate1);
        ivRate2 = dialogRate.findViewById(R.id.ivRate2);
        ivRate3 = dialogRate.findViewById(R.id.ivRate3);
        ivRate4 = dialogRate.findViewById(R.id.ivRate4);
        ivRate5 = dialogRate.findViewById(R.id.ivRate5);
        EditText etRate = dialogRate.findViewById(R.id.etRate);
        TextView tvNumberTextRate = dialogRate.findViewById(R.id.tvNumberTextRate);
        btRate = dialogRate.findViewById(R.id.btRate);
        btDeleteRate = dialogRate.findViewById(R.id.btDeleteRate);

        AtomicInteger idRate, pointRate;
        idRate = new AtomicInteger();
        pointRate = new AtomicInteger();
        //kiểm tra xem tài khoản đã đánh giá chưa
        Api.apiInterface().checkRateOfAccount(idStory, idAccount).enqueue(new Callback<DanhGia>() {
            @Override
            public void onResponse(@NonNull Call<DanhGia> call, @NonNull Response<DanhGia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DanhGia dg = response.body();
                    LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) btRate.getLayoutParams();
                    int rateSuccess = dg.getRatesuccess();
                    if (rateSuccess == 1) {
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
                        tvNumberTextRate.setText(String.format(Locale.getDefault(), "%d/400", dg.getDanhgia().length()));
                        btDeleteRate.setVisibility(View.VISIBLE);
                        lay.weight = 1;
                    }

                    if (rateSuccess != 1) {
                        btDeleteRate.setVisibility(View.GONE);
                        lay.weight = 0;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DanhGia> call, @NonNull Throwable t) {
                Log.e("Err_StoryInterface", "openDialogRate_checkRateOfAccount", t);
            }
        });

        //event
        ivClose.setOnClickListener(view -> dialogRate.dismiss());
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

        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etRate.getLineCount() > 7) {
                    Toast.makeText(StoryInterfaceActivity.this, "Vượt quá sô dòng giới hạn đánh giá!", Toast.LENGTH_SHORT).show();
                    etRate.getText().delete(etRate.getText().length() - 1, etRate.getText().length());
                }
                tvNumberTextRate.setText(String.format(Locale.getDefault(), "%d/400", s.toString().length()));
            }
        });

        btDeleteRate.setOnClickListener(view -> {//delete rate
            Api.apiInterface().deleteRate(idRate.get()).enqueue(new Callback<DanhGia>() {
                @Override
                public void onResponse(@NonNull Call<DanhGia> call, @NonNull Response<DanhGia> response) {
                    if (response.body() != null && response.body().getRatesuccess() == 1) {
                        getDataStory();
                        viewPager.setAdapter(viewPagerStoryInterfaceAdapter);//reload view pager
                        Toast.makeText(StoryInterfaceActivity.this, "Bạn đã xóa đánh giá!", Toast.LENGTH_SHORT).show();
                        dialogRate.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DanhGia> call, @NonNull Throwable t) {
                    Log.e("Err_StoryInterface", "openDialogRate_delete", t);
                }
            });
        });

        btRate.setOnClickListener(view -> {
            if (idRate.get() > 0) {//update rate
                dialogRate.dismiss();
                openDialogNotifyYesNo(idRate.get(), pointRate.get(), etRate.getText().toString());
            } else {//add rate
                if (pointRate.get() > 0) {
                    Api.apiInterface().addRate(idStory, idAccount, name, pointRate.get(), etRate.getText().toString()).enqueue(new Callback<DanhGia>() {
                        @Override
                        public void onResponse(@NonNull Call<DanhGia> call, @NonNull Response<DanhGia> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getRatesuccess() == 1) {
                                    getDataStory();
                                    viewPager.setAdapter(viewPagerStoryInterfaceAdapter);//reload view pager
                                    Toast.makeText(StoryInterfaceActivity.this, "Bạn đã đánh giá truyện!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DanhGia> call, @NonNull Throwable t) {
                            Log.e("Err_StoryInterface", "openDialogRate_add", t);
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

    @SuppressLint("SetTextI18n")
    private void openDialogNotifyYesNo(int idRate, int pointRate, String rate) {
        final Dialog dialogNotify = new Dialog(this);
        dialogNotify.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNotify.setContentView(R.layout.dialog_notify_yes_no);

        Window window = dialogNotify.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        dialogNotify.setCancelable(true);

        TextView tvTitle, tvContent, tvYes, tvNo;
        tvTitle = dialogNotify.findViewById(R.id.tvTitle);
        tvContent = dialogNotify.findViewById(R.id.tvContent);
        tvYes = dialogNotify.findViewById(R.id.tvYes);
        tvNo = dialogNotify.findViewById(R.id.tvNo);

        tvTitle.setText("Sửa đánh giá");
        tvContent.setText("Bạn có có chắc muốn thay đổi đánh giá?");

        tvYes.setOnClickListener(view -> {
            Api.apiInterface().updateRate(idRate, pointRate, rate).enqueue(new Callback<DanhGia>() {
                @Override
                public void onResponse(@NonNull Call<DanhGia> call, @NonNull Response<DanhGia> response) {
                    if (response.body() != null && response.body().getRatesuccess() == 1) {
                        getDataStory();
                        viewPager.setAdapter(viewPagerStoryInterfaceAdapter);//reload view pager
                        Toast.makeText(StoryInterfaceActivity.this, "Bạn đã đánh giá truyện!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DanhGia> call, @NonNull Throwable t) {
                    Log.e("Err_StoryInterface", "openDialogNotifyYesNo", t);
                }
            });
            dialogNotify.dismiss();
        });
        tvNo.setOnClickListener(view -> {
            dialogNotify.dismiss();
            openDialogRate();
        });

        dialogNotify.show();
    }
}