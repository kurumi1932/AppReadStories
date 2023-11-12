package huce.fit.appreadstories.story.follow;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.story.BaseListStoryImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryFollowImpl extends BaseListStoryImpl implements StoryFollowPresenter {

    private final static String TAG = "StoryFollowImpl";
    private final StoryFollowView mStoryFollowView;
    private final List<Truyen> mListStory = new ArrayList<>();
    private final int mAccountId;

    StoryFollowImpl(StoryFollowView storyFollowView, Context context) {
        super(context);
        mStoryFollowView = storyFollowView;
        mAccountId = getSharedPreferences().getIdAccount();
    }

    @Override
    public void getData() {
        Api.apiInterface().getListStoriesFollow(mAccountId).enqueue(new Callback<List<Truyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<Truyen>> call, @NonNull Response<List<Truyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getMatruyen() != 0) {
                        mListStory.clear();
                        mListStory.addAll(response.body());
                        mStoryFollowView.setData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Truyen>> call, @NonNull Throwable t) {
                Log.e("Err_StoryFollow", "getData", t);
            }
        });
    }
}
