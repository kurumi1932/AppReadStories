package huce.fit.appreadstories.story.information.sub_fragment.rate;

import static huce.fit.appreadstories.checknetwork.NetworkKt.isConnecting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.Rate;
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences;
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInterfaceRateFragmentImpl implements StoryInterfaceRateFragmentPresenter{

    private static final String TAG = "StoryInterfaceRateFragmentImpl";
    private final StoryInterfaceRateFragmentView mStoryInterfaceRateFragmentView;
    private final Context mContext;
    private int mStoryId;

    public StoryInterfaceRateFragmentImpl(StoryInterfaceRateFragmentView storyInterfaceRateFragmentView, Context context) {
        mStoryInterfaceRateFragmentView = storyInterfaceRateFragmentView;
        mContext = context;
        getStory();
    }

    private void getStory() {
        StorySharedPreferences story = new StorySharedPreferences(mContext);
        story.getSharedPreferences("Story", Context.MODE_PRIVATE);
        mStoryId = story.getStoryId();
    }

    @Override
    public void getRateList() {
        if (isConnecting(mContext)) {
            new Api().apiInterface().getRateList(mStoryId).enqueue(new Callback<>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(@NonNull Call<List<Rate>> call, @NonNull Response<List<Rate>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mStoryInterfaceRateFragmentView.setData(response.body());
                    }
                    Log.e(TAG, "api getRateList: success");
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(@NonNull Call<List<Rate>> call, @NonNull Throwable t) {
                    Log.e(TAG, "api getRateList: fail");
                }
            });
        } else {
            mStoryInterfaceRateFragmentView.noNetwork();
        }
    }
}
