package huce.fit.appreadstories.story.filter;

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

public class StoryFilterImpl extends BaseListStoryImpl implements StoryFilterPresenter {

    private static final String TAG = "StoryFilterImpl";
    private final StoryFilterView mStoryFilterView;
    private final List<Truyen> mListStory = new ArrayList<>();
    private int mSpeciesId, mStatusId;
    private final int mAge;
    private String mSpecies, mStatus;

    StoryFilterImpl(StoryFilterView storyFilterView, Context context) {
        super(context);
        mStoryFilterView = storyFilterView;
        mAge = getSharedPreferences().getAge();
    }

    @Override
    public void setSpeciesId(int mSpeciesId) {
        this.mSpeciesId = mSpeciesId;
    }

    @Override
    public void setStatusId(int mStatusId) {
        this.mStatusId = mStatusId;
    }

    @Override
    public int getSpeciesId() {
        return mSpeciesId;
    }

    @Override
    public int getStatusId() {
        return mStatusId;
    }

    @Override
    public void setSpecies(String species) {
        mSpecies = species;
    }

    @Override
    public void setStatus(String status) {
        mStatus = status;
    }

    @Override
    public void getData() {
        Log.e(TAG, "NHT mSpecies= " + mSpecies + " mStatus= " + mStatus);
        Api.apiInterface().getListStoriesFilter(mSpecies, mStatus, mAge).enqueue(new Callback<List<Truyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<Truyen>> call, @NonNull Response<List<Truyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mListStory.clear();
                    mListStory.addAll(response.body());
                    mStoryFilterView.setData(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Truyen>> call, Throwable t) {
                Log.e("Err_StoryFilter", "getDataFilter", t);
            }
        });
    }
}
