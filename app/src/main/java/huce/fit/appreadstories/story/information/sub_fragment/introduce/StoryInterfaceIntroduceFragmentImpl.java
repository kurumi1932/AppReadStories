package huce.fit.appreadstories.story.information.sub_fragment.introduce;

import static huce.fit.appreadstories.checknetwork.NetworkKt.isConnecting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.Story;
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences;
import huce.fit.appreadstories.sqlite.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInterfaceIntroduceFragmentImpl implements StoryInterfaceIntroduceFragmentPresenter{

    private static final String TAG = "StoryInterfaceIntroduceFragmentImpl";
    private final StoryInterfaceIntroduceFragmentView mStoryInterfaceIntroduceFragmentView;
    private final Context mContext;
    private int mStoryId;

    public StoryInterfaceIntroduceFragmentImpl(StoryInterfaceIntroduceFragmentView storyInterfaceIntroduceFragmentView, Context context) {
        mStoryInterfaceIntroduceFragmentView = storyInterfaceIntroduceFragmentView;
        mContext = context;
        getStory();
    }

    private void getStory() {
        StorySharedPreferences story = new StorySharedPreferences(mContext);
        story.getSharedPreferences("Story", Context.MODE_PRIVATE);
        mStoryId = story.getStoryId();
    }

    @Override
    public void getIntroduce() {
        if (isConnecting(mContext)) {
            new Api().apiInterface().getStory(mStoryId).enqueue(new Callback<Story>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(@NonNull Call<Story> call, @NonNull Response<Story> response) {
                    if (response.body() != null) {
                        mStoryInterfaceIntroduceFragmentView.setIntroduce(response.body().getIntroduce());
                    }
                    Log.e(TAG, "api getIntroduce: success");
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(@NonNull Call<Story> call, @NonNull Throwable t) {
                    Log.e(TAG, "api getIntroduce: fail");
                }
            });
        }else{
            Story story = AppDatabase.getInstance(mContext).appDao().getStory(mStoryId);
            mStoryInterfaceIntroduceFragmentView.setIntroduce(story.getIntroduce());
        }
    }
}
