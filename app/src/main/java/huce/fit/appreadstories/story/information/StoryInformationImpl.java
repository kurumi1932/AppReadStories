package huce.fit.appreadstories.story.information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Locale;

import huce.fit.appreadstories.activity.ChapterListActivity;
import huce.fit.appreadstories.activity.ChapterReadActivity;
import huce.fit.appreadstories.activity.CommentListActivity;
import huce.fit.appreadstories.activity.StoryDownloadActivity;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.ChuongTruyen;
import huce.fit.appreadstories.model.DanhGia;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.service.CheckStoryService;
import huce.fit.appreadstories.shared_preferences.MySharedPreferences;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Story;
import huce.fit.appreadstories.story.BaseListStoryImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInformationImpl extends BaseListStoryImpl implements StoryInformationPresenter {

    private static final String TAG = "StoryInformationImpl";
    private final StoryInformationView mStoryInformationView;
    private final Context mContext;
    private Truyen mTruyen;
    private final Story mStory;
    private boolean isRate = false, isComment = false, isFollow = false;
    private final int mStoryId, mAccountId;
    private int mChapterReadingId;
    private final String mName;

    StoryInformationImpl(StoryInformationView storyInformationView, Intent intent) {
        super((Context) storyInformationView);
        mStoryInformationView = storyInformationView;
        mContext = (Context) storyInformationView;
        mStoryId = intent.getIntExtra("storyId", 0);
        mStory = AppDatabase.getInstance(mContext).appDao().getStory(mStoryId);

        if (isNetwork() && mStory != null) {
            checkStoryService();
        }

        MySharedPreferences mySharedPreferences = getSharedPreferences();
        mAccountId = mySharedPreferences.getIdAccount();
        mName = mySharedPreferences.getName();
    }

    @Override
    public void checkStoryService() {
        Log.e("StoryInterfaceActivity", "startCheckStoryService");
        Intent intent = new Intent(mContext, CheckStoryService.class);
        intent.putExtra("storyId", mStoryId);
        intent.putExtra("isFollow", isFollow);
        mContext.startService(intent);
        Log.e("intent", intent.toString());
    }

    @Override
    public int getStoryId() {
        return mStoryId;
    }

    @Override
    public void getData() {
        Api.apiInterface().getStory(mStoryId).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mTruyen = response.body();
                    mStoryInformationView.setData(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_getDataStory", t);
            }
        });
    }

    @Override
    public void getDataOffline() {
        mStoryInformationView.setData(mStory);
    }

    @Override
    public boolean checkAge() {
        int age;
        if (isNetwork()) {
            age = mTruyen.getGioihantuoi();
        } else {
            age = mStory.getAge();
        }
        return age < 18;
    }

    @Override
    public void checkInteractive() {
        Api.apiInterface().checkLikeStory(mStoryId, mAccountId).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Truyen truyen = response.body();
                    double progress = Double.parseDouble(truyen.getTylechuongdadoc());
                    String like = String.format(Locale.getDefault(), "%d\nYêu thích", truyen.getLuotthich());

                    if (progress > 4) {
                        isRate = progress > 19;
                        isComment = true;
                    } else {
                        isRate = false;
                        isComment = false;
                    }

                    mStoryInformationView.checkInteractive(truyen.getStorySuccess(), like);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_checkLikeStory", t);
            }
        });
    }

    public void likeStory() {
        Api.apiInterface().likeStory(mStoryId, mAccountId).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStorySuccess() == 1) {
                    checkInteractive();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_likeStory", t);
            }
        });
    }

    @Override
    public void followStory() {
        Api.apiInterface().followStory(mAccountId, mStoryId, mTruyen.getTentruyen(), mTruyen.getTacgia(),
                mTruyen.getGioihantuoi(), mTruyen.getTrangthai(), mTruyen.getTongchuong(), mTruyen.getAnh(),
                mTruyen.getThoigiancapnhat()).enqueue(new Callback<Truyen>() {
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int success = response.body().getStorySuccess();
                    switch (success) {
                        case 1:
                            isFollow = true;
                            break;
                        case 2:
                            isFollow = false;
                            break;
                    }
                    mStoryInformationView.followStory(success);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_add_delete_StoryFollow1", t);
            }
        });
    }

    @Override
    public void checkFollowStory() {
        Api.apiInterface().checkStoryFollow(mAccountId, mStoryId).enqueue(new Callback<Truyen>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Truyen> call, @NonNull Response<Truyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int success = response.body().getStorySuccess();
                    switch (success) {
                        case 1:
                            isFollow = true;
                            break;
                        case 2:
                            isFollow = false;
                            break;
                    }
                    mStoryInformationView.followStory(success);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Truyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_checkFollow", t);
            }
        });
    }

    @Override
    public void checkFollowStoryDownload() {
        int follow = mStory.getIsFollow();
        switch (follow) {
            case 1:
                isFollow = true;
                break;
            case 0:
                isFollow = false;
                break;
        }
        mStoryInformationView.followStory(follow == 0 ? 2 : 1);
    }

    @Override
    public void readStory() {
        Api.apiInterface().getChapterRead(mStoryId, mAccountId, 1).enqueue(new Callback<ChuongTruyen>() {
            @Override
            public void onResponse(@NonNull Call<ChuongTruyen> call, @NonNull Response<ChuongTruyen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getMachuong() == 0) {
                        Api.apiInterface().firstChapter(mStoryId).enqueue(new Callback<ChuongTruyen>() {
                            @Override
                            public void onResponse(@NonNull Call<ChuongTruyen> call, @NonNull Response<ChuongTruyen> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    mChapterReadingId = response.body().getMachuong();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ChuongTruyen> call, @NonNull Throwable t) {
                                Log.e("StoryInterface", "Err_readStory", t);
                            }
                        });
                    } else {
                        mChapterReadingId = response.body().getMachuong();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChuongTruyen> call, @NonNull Throwable t) {
                Log.e("StoryInterface", "Err_readStory", t);
            }
        });
    }

    @Override
    public void checkRateOfAccount() {
        Api.apiInterface().checkRateOfAccount(mStoryId, mAccountId).enqueue(new Callback<DanhGia>() {
            @Override
            public void onResponse(@NonNull Call<DanhGia> call, @NonNull Response<DanhGia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mStoryInformationView.getRate(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DanhGia> call, @NonNull Throwable t) {
                Log.e("Err_StoryInterface", "openDialogRate_checkRateOfAccount", t);
            }
        });
    }

    @Override
    public void addRate(int ratePoint, String rate) {
        Api.apiInterface().addRate(mStoryId, mAccountId, mName, ratePoint, rate).enqueue(new Callback<DanhGia>() {
            @Override
            public void onResponse(@NonNull Call<DanhGia> call, @NonNull Response<DanhGia> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getRatesuccess() == 1) {
                    getData();
                    mStoryInformationView.reloadViewPaper("Bạn đã đánh giá truyện!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<DanhGia> call, @NonNull Throwable t) {
                Log.e("Err_StoryInterface", "openDialogRate_add", t);
            }
        });
    }

    @Override
    public void updateRate(int rateId, int ratePoint, String rate) {
        Api.apiInterface().updateRate(rateId, ratePoint, rate).enqueue(new Callback<DanhGia>() {
            @Override
            public void onResponse(@NonNull Call<DanhGia> call, @NonNull Response<DanhGia> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getRatesuccess() == 1) {
                    getData();
                    mStoryInformationView.reloadViewPaper("Bạn đã đánh giá truyện!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<DanhGia> call, @NonNull Throwable t) {
                Log.e("Err_StoryInterface", "openDialogNotifyYesNo", t);
            }
        });
    }

    @Override
    public void deleteRate(int rateId) {
        Api.apiInterface().deleteRate(rateId).enqueue(new Callback<DanhGia>() {
            @Override
            public void onResponse(@NonNull Call<DanhGia> call, @NonNull Response<DanhGia> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getRatesuccess() == 1) {
                    getData();
                    mStoryInformationView.reloadViewPaper("Bạn đã xóa đánh giá!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<DanhGia> call, @NonNull Throwable t) {
                Log.e("Err_StoryInterface", "openDialogRate_delete", t);
            }
        });
    }

    @Override
    public boolean getIsRate() {
        return isRate;
    }

    @Override
    public void readStoryDownload() {
        mChapterReadingId = mStory.getChapterReading();
    }

    @Override
    public boolean enterComment() {
        if (isComment) {
            Intent intent = new Intent(mContext, CommentListActivity.class);
            intent.putExtra("idAccount", mAccountId);
            intent.putExtra("storyId", mStoryId);
            mContext.startActivity(intent);
        }
        return isComment;
    }

    @Override
    public void enterDownload() {
        Intent intent = new Intent(mContext, StoryDownloadActivity.class);
        intent.putExtra("storyId", mStoryId);
        intent.putExtra("isFollow", isFollow);
        intent.putExtra("idChapterReading", mChapterReadingId);
        mContext.startActivity(intent);
    }

    @Override
    public void enterChapter() {
        Intent intent = new Intent(mContext, ChapterListActivity.class);
        intent.putExtra("storyId", mStoryId);
        mContext.startActivity(intent);
    }

    @Override
    public void enterReadStory() {
        Intent intent = new Intent(mContext, ChapterReadActivity.class);
        intent.putExtra("storyId", mStoryId);
        intent.putExtra("idChapterReading", mChapterReadingId);
        mContext.startActivity(intent);
    }
}