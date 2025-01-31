package huce.fit.appreadstories.story.information;

import static huce.fit.appreadstories.checknetwork.NetworkKt.isConnecting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.adapters.ViewPagerStoryInterfaceAdapter;
import huce.fit.appreadstories.dialog.confirm.ConfirmCheckAgeDialog;
import huce.fit.appreadstories.dialog.rate.RateDialog;
import huce.fit.appreadstories.model.Story;


public class StoryInformationActivity extends AppCompatActivity implements StoryInformationView {

    private ViewPager2 viewPager;
    private LinearLayout linearLayout;
    private ImageView ivBack, ivStory;
    private TextView tvStoryName, tvAuthor, tvStatus, tvSpecies, tvLike, tvView, tvSumChapter, tvComment, tvRate;
    private Button btReadStory, btFollow, btDownLoad;
    private StoryInformationPresenter mStoryInformationPresenter;
    private ViewPagerStoryInterfaceAdapter viewPagerStoryInterfaceAdapter;

    private static final int[] RATE = {R.id.ivRate1, R.id.ivRate2, R.id.ivRate3, R.id.ivRate4, R.id.ivRate5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_interface);

        init();
        processEvents();
    }

    private void init() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        linearLayout = findViewById(R.id.linearLayoutRate);
        tvStoryName = findViewById(R.id.tvStoryName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvStatus = findViewById(R.id.tvStatus);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvRate = findViewById(R.id.tvRate);
        tvLike = findViewById(R.id.tvLike);
        tvView = findViewById(R.id.tvView);
        tvSumChapter = findViewById(R.id.tvSumChapter);
        tvComment = findViewById(R.id.tvComment);
        ivBack = findViewById(R.id.ivBack);
        ivStory = findViewById(R.id.ivStory);
        btReadStory = findViewById(R.id.btReadStory);
        btFollow = findViewById(R.id.btFollow);
        btDownLoad = findViewById(R.id.btDownload);

        mStoryInformationPresenter = new StoryInformationImpl(this);
        viewPagerStoryInterfaceAdapter = new ViewPagerStoryInterfaceAdapter(this);
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

    @Override
    protected void onResume() {
        super.onResume();
        mStoryInformationPresenter.start();
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void setRateStartLight(int index){
        ImageView ivRate = findViewById(RATE[index]);
        ivRate.setImageResource(R.drawable.ic_rate);
    }

    @Override
    public void setRateStartDark(int index){
        ImageView ivRate = findViewById(RATE[index]);
        ivRate.setImageResource(R.drawable.ic_not_rate);
    }

    @Override
    public void setData(Story story) {
        if (story == null) {
            finish();
            return;
        }
        tvStoryName.setText(story.getStoryName());
        tvAuthor.setText(story.getAuthor());
        tvStatus.setText(story.getStatus());
        tvSpecies.setText(story.getSpecies());
        Picasso.get().load(story.getImage()).into(ivStory);
        tvRate.setText(String.valueOf(story.getRatePoint()));
        mStoryInformationPresenter.showRate((int) story.getRatePoint());
        tvLike.setText(String.format(Locale.getDefault(), "%d\nYêu thích", story.getTotalLikes()));
        tvView.setText(String.format(Locale.getDefault(), "%d\nLượt xem", story.getTotalViews()));
        tvSumChapter.setText(String.format(Locale.getDefault(), "%d\nChương", story.getSumChapter()));
        tvComment.setText(String.format(Locale.getDefault(), "%d\nBình luận", story.getTotalComments()));
    }

    @Override
    public void checkAge(boolean isCheckAge) {
        if (isCheckAge) {
            ConfirmCheckAgeDialog confirmCheckAgeDialog = new ConfirmCheckAgeDialog(this, this);
            confirmCheckAgeDialog.show();
        }
    }

    @Override
    public void checkInteractive(int isLike, String likeNumber) {
        if (isLike == 0) {
            tvLike.setTextColor(getResources().getColor(R.color.black));
        } else {
            tvLike.setTextColor(getResources().getColor(R.color.orange));
        }
        tvLike.setText(likeNumber);
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> finish());

        if (isConnecting(this)) {
            linearLayout.setOnClickListener(v -> {
                //kiểm tra xem tài khoản đã đánh giá chưa
                new Handler().postDelayed(() -> mStoryInformationPresenter.checkRateOfAccount(), 1000);
            });

            tvLike.setOnClickListener(v -> mStoryInformationPresenter.likeStory());

            tvComment.setOnClickListener(v -> {
                mStoryInformationPresenter.enterComment();
            });

            btFollow.setOnClickListener(v -> mStoryInformationPresenter.followStory());

            btDownLoad.setOnClickListener(v ->
                    mStoryInformationPresenter.enterDownload());
        }
        tvSumChapter.setOnClickListener(v -> mStoryInformationPresenter.enterChapter());
        btReadStory.setOnClickListener(v -> mStoryInformationPresenter.enterReadStory());
    }

    @Override
    public void followStory(int isFollow) {
        switch (isFollow) {
            case 1:
                btFollow.setText("Đang theo dõi");
                btFollow.setTextColor(getResources().getColor(R.color.medium_sea_green));
                btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_follow, 0, 0);
                break;
            case 2:
                btFollow.setText("Theo dõi");
                btFollow.setTextColor(getResources().getColor(R.color.dim_gray));
                btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_unfollow, 0, 0);
                break;
        }
    }

    @Override
    public void openDialogRate() {
        RateDialog rateDialog = new RateDialog(this, this);
        rateDialog.show();
    }

    @Override
    public void addRate(int ratePoint, String rate) {
        mStoryInformationPresenter.addRate(ratePoint, rate);
    }

    @Override
    public void deleteRate() {
        mStoryInformationPresenter.deleteRate();
    }

    @Override
    public void updateRate(int ratePoint, String rate) {
        mStoryInformationPresenter.updateRate(ratePoint, rate);
    }

    @Override
    public void reloadViewPaper(String text) {
        viewPager.setAdapter(viewPagerStoryInterfaceAdapter);//reload view pager
        Toast.makeText(StoryInformationActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkStory() {
        Intent intent = new Intent(StoryInformationActivity.this, CheckStoryService.class);
        startService(intent);
    }
}