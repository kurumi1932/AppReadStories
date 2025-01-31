package huce.fit.appreadstories.story.information;

import static huce.fit.appreadstories.checknetwork.NetworkKt.isConnecting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Locale;

import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.chapter.information.ChapterInformationActivity;
import huce.fit.appreadstories.chapter.list.ChapterListActivity;
import huce.fit.appreadstories.comment.CommentActivity;
import huce.fit.appreadstories.model.Chapter;
import huce.fit.appreadstories.model.Rate;
import huce.fit.appreadstories.model.Story;
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences;
import huce.fit.appreadstories.shared_preferences.RateSharedPreferences;
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences;
import huce.fit.appreadstories.sqlite.AppDao;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.story.download.StoryDownloadActivity;
import huce.fit.appreadstories.story.list.BaseStoryListImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInformationImpl extends BaseStoryListImpl implements StoryInformationPresenter {

    private static final String TAG = "StoryInformationImpl";
    private final StoryInformationView mStoryInformationView;
    private StorySharedPreferences mStory;
    private Story mStoryDao;
    private AppDao mAppDao;
    private boolean isFirst = true;
    private int mStoryId, mAccountId, mRateId;
    private String mName;

    StoryInformationImpl(StoryInformationView storyInformationView) {
        super((Context) storyInformationView);
        mStoryInformationView = storyInformationView;

        init();
    }

    private void init(){
        mAppDao = AppDatabase.getInstance(getContext()).appDao();
        getAccountSharedPreferences();
        getStorySharedPreferences();
    }

    private void getAccountSharedPreferences() {
        AccountSharedPreferences account = getAccount();
        mAccountId = account.getAccountId();
        mName = account.getName();
    }

    private void getStorySharedPreferences() {
        mStory = new StorySharedPreferences(getContext());
        mStory.getSharedPreferences("Story", Context.MODE_PRIVATE);
        mStoryId = mStory.getStoryId();
        mStory.setSharedPreferences("Story", Context.MODE_PRIVATE);
    }

    @Override
    public void start() {
        mStoryDao = mAppDao.getStory(mStoryId);
        if (isConnecting(getContext())) {
            getStory();
            readStory();
            checkFollowStory();
            checkInteractive();
            if (mStoryDao != null) {
                mStoryInformationView.checkStory();
            }
        } else {
            if (mStoryDao != null) {
                getDataOffline();
                readStoryDownload();
                checkFollowStoryDownload();
            }
        }
    }

    @Override
    public int getStoryId() {
        return mStoryId;
    }

    private void getStory() {
        new Api().apiInterface().getStory(mStoryId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Story> call, @NonNull Response<Story> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mStoryDao = response.body();
                    mStoryInformationView.setData(mStoryDao);
                    if (isFirst) {
                        mStory.setStoryName(mStoryDao.getStoryName());
                        mStory.setAuthor(mStoryDao.getAuthor());
                        mStory.setStatus(mStoryDao.getStatus());
                        mStory.setSpecies(mStoryDao.getSpecies());
                        mStory.setSumChapter(mStoryDao.getSumChapter());
                        mStory.setTotalLikes(mStoryDao.getTotalLikes());
                        mStory.setTotalViews(mStoryDao.getTotalViews());
                        mStory.setTotalComments(mStoryDao.getTotalComments());
                        mStory.setRatePoint(mStoryDao.getRatePoint());
                        mStory.setIntroduce(mStoryDao.getIntroduce());
                        mStory.setImage(mStoryDao.getImage());
                        mStory.setAgeLimit(mStoryDao.getAgeLimit());
                        mStory.setTimeUpdate(mStoryDao.getTimeUpdate());
                        mStory.myApply();

                        mStoryInformationView.checkAge(checkAge());
                        isFirst = false;
                    }
                }
                Log.e(TAG, "api getStory: success");
            }

            @Override
            public void onFailure(@NonNull Call<Story> call, @NonNull Throwable t) {
                Log.e(TAG, "api getStory: false");
            }
        });
    }

    @Override
    public void showRate(int ratePoint) {
        for (int i = 0; i < ratePoint; i++) {
            mStoryInformationView.setRateStartLight(i);
        }
        for (int i = ratePoint; i < 5; i++) {
            mStoryInformationView.setRateStartDark(i);
        }
    }

    @Override
    public void getDataOffline() {
        mStoryInformationView.setData(mStoryDao);
        mStoryInformationView.checkAge(checkAge());
    }

    @Override
    public boolean checkAge() {
        return mStoryDao.getAgeLimit() < 18;
    }

    @Override
    public void checkInteractive() {
        new Api().apiInterface().checkLikeStory(mStoryId, mAccountId).enqueue(new Callback<Story>() {
            @Override
            public void onResponse(@NonNull Call<Story> call, @NonNull Response<Story> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Story story = response.body();
                    String totalLikes = String.format(Locale.getDefault(), "%d\nYêu thích", story.getTotalLikes());

                    mStoryInformationView.checkInteractive(story.getStorySuccess(), totalLikes);
                }
                Log.e(TAG, "api checkInteractive: success");
            }

            @Override
            public void onFailure(@NonNull Call<Story> call, @NonNull Throwable t) {
                Log.e(TAG, "api checkInteractive: false");
            }
        });
    }

    public void likeStory() {
        new Api().apiInterface().likeStory(mStoryId, mAccountId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Story> call, @NonNull Response<Story> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStorySuccess() == 1) {
                    checkInteractive();
                }
                Log.e(TAG, "api likeStory: success");
            }

            @Override
            public void onFailure(@NonNull Call<Story> call, @NonNull Throwable t) {
                Log.e(TAG, "api likeStory: false");
            }
        });
    }

    @Override
    public void followStory() {
        new Api().apiInterface().followStory(mAccountId, mStoryId, mStoryDao.getStoryName(), mStoryDao.getAuthor(),
                mStoryDao.getAgeLimit(), mStoryDao.getStatus(), mStoryDao.getSumChapter(), mStoryDao.getImage(),
                mStoryDao.getTimeUpdate()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Story> call, @NonNull Response<Story> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int success = response.body().getStorySuccess();
                    mStory.setIsFollow(success == 1);
                    mStory.myApply();
                    mStoryInformationView.followStory(success);
                }
                Log.e(TAG, "api followStory: success");
            }

            @Override
            public void onFailure(@NonNull Call<Story> call, @NonNull Throwable t) {
                Log.e(TAG, "api followStory: false");
            }
        });
    }

    @Override
    public void checkFollowStory() {
        new Api().apiInterface().checkStoryFollow(mAccountId, mStoryId).enqueue(new Callback<>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Story> call, @NonNull Response<Story> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int success = response.body().getStorySuccess();
                    mStory.setIsFollow(success == 1);
                    mStory.myApply();
                    mStoryInformationView.followStory(success);
                }
                Log.e(TAG, "api checkFollowStory: success");
            }

            @Override
            public void onFailure(@NonNull Call<Story> call, @NonNull Throwable t) {
                Log.e(TAG, "api checkFollowStory: false");
            }
        });
    }

    @Override
    public void checkFollowStoryDownload() {
        int follow = mStoryDao.getIsFollow();
        mStoryInformationView.followStory(follow == 0 ? 2 : 1);
    }

    @Override
    public void readStory() {
        new Api().apiInterface().getChapterReadId(mStoryId, mAccountId, 1).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Chapter> call, @NonNull Response<Chapter> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getChapterId() == 0) {
                        new Api().apiInterface().firstChapter(mStoryId).enqueue(new Callback<>() {
                            @Override
                            public void onResponse(@NonNull Call<Chapter> call, @NonNull Response<Chapter> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    mStory.setChapterReading(response.body().getChapterId());
                                    mStory.myApply();
                                }
                                Log.e(TAG, "api readStory firstChapter: success");
                            }

                            @Override
                            public void onFailure(@NonNull Call<Chapter> call, @NonNull Throwable t) {
                                Log.e(TAG, "api readStory firstChapter: false");
                            }
                        });
                    } else {
                        mStory.setChapterReading(response.body().getChapterId());
                        mStory.myApply();
                    }
                }
                Log.e(TAG, "api readStory getChapterReadId: success");
            }

            @Override
            public void onFailure(@NonNull Call<Chapter> call, @NonNull Throwable t) {
                Log.e(TAG, "api readStory getChapterReadId: false");
            }
        });
    }

    @Override
    public void checkRateOfAccount() {
        new Api().apiInterface().checkRateOfAccount(mStoryId, mAccountId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Rate> call, @NonNull Response<Rate> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setRate(response.body());
                    mStoryInformationView.openDialogRate();
                }
                Log.e(TAG, "api checkRateOfAccount: success");
            }

            @Override
            public void onFailure(@NonNull Call<Rate> call, @NonNull Throwable t) {
                Log.e(TAG, "api checkRateOfAccount: false");
            }
        });
    }

    private void setRate(Rate rateData) {
        mRateId = rateData.getRateId();
        RateSharedPreferences rate = new RateSharedPreferences(getContext());
        rate.setSharedPreferences("Rate", Context.MODE_PRIVATE);
        rate.setRateId(mRateId);
        rate.setRatePoint(rateData.getRatePoint());
        rate.setRate(rateData.getRate());
        rate.setSuccess(rateData.getSuccess());
        rate.myApply();
    }

    @Override
    public void addRate(int ratePoint, String rate) {
        new Api().apiInterface().addRate(mStoryId, mAccountId, mName, ratePoint, rate).enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(@NonNull Call<Rate> call, @NonNull Response<Rate> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess() == 1) {
                    getStory();
                    mStoryInformationView.reloadViewPaper("Bạn đã đánh giá truyện!");
                }
                Log.e(TAG, "api addRate: success");
            }

            @Override
            public void onFailure(@NonNull Call<Rate> call, @NonNull Throwable t) {
                Log.e(TAG, "api addRate: false");
            }
        });
    }

    @Override
    public void updateRate(int ratePoint, String rate) {
        new Api().apiInterface().updateRate(mRateId, ratePoint, rate).enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(@NonNull Call<Rate> call, @NonNull Response<Rate> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess() == 1) {
                    getStory();
                    mStoryInformationView.reloadViewPaper("Bạn đã thay đổi đánh giá truyện!");
                }
                Log.e(TAG, "api updateRate: success");
            }

            @Override
            public void onFailure(@NonNull Call<Rate> call, @NonNull Throwable t) {
                Log.e(TAG, "api updateRate: false");
            }
        });
    }

    @Override
    public void deleteRate() {
        new Api().apiInterface().deleteRate(mRateId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Rate> call, @NonNull Response<Rate> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getSuccess() == 1) {
                    getStory();
                    mStoryInformationView.reloadViewPaper("Bạn đã xóa đánh giá!");
                }
                Log.e(TAG, "api deleteRate: success");
            }

            @Override
            public void onFailure(@NonNull Call<Rate> call, @NonNull Throwable t) {
                Log.e(TAG, "api deleteRate: false");
            }
        });
    }

    @Override
    public void readStoryDownload() {
        mStory.setChapterReading(mStoryDao.getChapterReading());
    }

    @Override
    public void enterComment() {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void enterDownload() {
        Intent intent = new Intent(getContext(), StoryDownloadActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void enterChapter() {
        Intent intent = new Intent(getContext(), ChapterListActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void enterReadStory() {
        Intent intent = new Intent(getContext(), ChapterInformationActivity.class);
        getContext().startActivity(intent);
    }
}