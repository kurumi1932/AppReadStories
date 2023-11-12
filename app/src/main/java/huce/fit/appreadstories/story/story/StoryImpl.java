package huce.fit.appreadstories.story.story;

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

public class StoryImpl extends BaseListStoryImpl implements StoryPresenter{

    private static final String TAG = "StoryImpl";

    private final StoryView mStoryView;
    private final List<Truyen> mListStory = new ArrayList<>();
    private boolean isLoading;
    private boolean isLastPage;
    private final int mAge;
    private int mCurrentPage = 1;// page hiện tại
    private int mTotalPage; // tổng số trang
    private static final int ITEMS_PAGE = 8;// số item có trong trang
    StoryImpl(StoryView storyView, Context context) {
        super(context);
        mStoryView = storyView;
        mAge = getSharedPreferences().getAge();
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean isLastPage() {
        return isLastPage;
    }

    @Override
    public void totalPage() {
        Log.e("ageTotal", String.valueOf(mAge));
        Api.apiInterface().getListStories(mAge).enqueue(new Callback<List<Truyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<Truyen>> call, @NonNull Response<List<Truyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("number_item:", String.valueOf(response.body().size()));
                    mTotalPage = (response.body().size() / ITEMS_PAGE);
                    Log.e("number_page:", String.valueOf(mTotalPage));
                    if ((mTotalPage * ITEMS_PAGE) < response.body().size()) {
                        mTotalPage += 1;
                        Log.e("number_page:", String.valueOf(mTotalPage));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Truyen>> call, @NonNull Throwable t) {
                Log.e("Err_StoryFagment", "totalPage", t);
            }
        });
    }

    @Override
    public void getData(int page) {
        Api.apiInterface().getListStories(mAge).enqueue(new Callback<List<Truyen>>() {
            @Override
            public void onResponse(@NonNull Call<List<Truyen>> call, @NonNull Response<List<Truyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (page * ITEMS_PAGE > response.body().size()) {
                        for (int i = (page - 1) * ITEMS_PAGE; i < response.body().size(); i++) {
                            mListStory.add(response.body().get(i));
                        }
                    } else {
                        for (int i = (page - 1) * ITEMS_PAGE; i < (page * ITEMS_PAGE); i++) {
                            mListStory.add(response.body().get(i));
                        }
                    }
                    mStoryView.setData(mListStory);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Truyen>> call, @NonNull Throwable t) {
                Log.e("Err_StoryFragment", "getData", t);
            }
        });
    }

    @Override
    public void loadNextPage() {
        isLoading = true;
        mCurrentPage++;

        mStoryView.getData(mCurrentPage);
        isLoading = false;

        if (mCurrentPage == mTotalPage) {
            isLastPage = true;
        }
    }

    @Override
    public void swipeData() {
        if (isNetwork()) {
            mListStory.clear();
            isLastPage = false;
            mCurrentPage = 1;
            mStoryView.getData(1);
        } else {
            mStoryView.hide();
        }
    }
}
