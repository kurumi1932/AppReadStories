package huce.fit.appreadstories.dialog.chapter_list;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.chapter.information.ChapterInformationView;
import huce.fit.appreadstories.model.Chapter;
import huce.fit.appreadstories.model.Story;
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterListDialogImpl implements ChapterListDialogPresenter {

    private static final String TAG = "ChapterListDialogImpl";
    private final ChapterListDialogView mRateDialogView;
    private final Context mContext;
    private final Story mStory = new Story();
    private final List<Chapter> mChapterList = new ArrayList<>();
    private int mStoryId;

    ChapterListDialogImpl(ChapterInformationView chapterInformationView, ChapterListDialogView rateDialogView) {
        mRateDialogView = rateDialogView;
        mContext = (Context) chapterInformationView;
        start();
    }

    private void start() {
        getStory();
    }

    private void getStory() {
        StorySharedPreferences storySharedPreferences = new StorySharedPreferences(mContext);
        storySharedPreferences.getSharedPreferences("Story", Context.MODE_PRIVATE);
        mStoryId = storySharedPreferences.getStoryId();
        Log.e(TAG, "NHT storyId: "+ mStoryId);
        mStory.setImage(storySharedPreferences.getImage());
        mStory.setStoryName(storySharedPreferences.getStoryName());
        mStory.setAuthor(storySharedPreferences.getAuthor());

        mRateDialogView.setData(mStory);
    }

    @Override
    public void getChapterList() {
        new Api().apiInterface().getChapterList(mStoryId).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(@NonNull Call<List<Chapter>> call, @NonNull Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mChapterList.clear();
                    mChapterList.addAll(response.body());
                    mRateDialogView.setChapterList(mChapterList);
                    mRateDialogView.show();
                }
                Log.e(TAG, "api getChapterList: success");
            }

            @Override
            public void onFailure(@NonNull Call<List<Chapter>> call, @NonNull Throwable t) {
                Log.e(TAG, "api getChapterList: fail");
            }
        });
    }
}
