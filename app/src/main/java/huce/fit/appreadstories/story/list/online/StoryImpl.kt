package huce.fit.appreadstories.story.list.online;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.Story;
import huce.fit.appreadstories.story.list.BaseStoryListImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryImpl extends BaseStoryListImpl implements StoryPresenter {

    private static final String TAG = "StoryImpl";

    private final StoryView mStoryView;
    private final List<Story> mStoryList = new ArrayList<>();
    private boolean isLoading;
    private boolean isLastPage;
    private static final int ITEMS_PAGE = 8;// số item có trong trang
    private static final int START_PAGE = 1;
    private final int mAge;
    private int mCurrentPage;// page hiện tại
    private int mTotalPage; // tổng số trang

    StoryImpl(StoryView storyView, Context context) {
        super(context);
        mStoryView = storyView;
        mAge = getAccount().getAge();
        mCurrentPage = START_PAGE;
        totalPage();
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
        new Api().apiInterface().getListStories(mAge).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Story>> call, @NonNull Response<List<Story>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("NHT number_item:", String.valueOf(response.body().size()));
                    mTotalPage = (response.body().size() / ITEMS_PAGE);
                    if ((mTotalPage * ITEMS_PAGE) < response.body().size()) {
                        ++mTotalPage;
                    }
                    Log.e("NHT number_page:", String.valueOf(mTotalPage));
                }
                Log.e(TAG, "api totalPage: success");
            }

            @Override
            public void onFailure(@NonNull Call<List<Story>> call, @NonNull Throwable t) {
                Log.e(TAG, "api totalPage: fail");
            }
        });
    }

    @Override
    public void getStory(int page) {
        new Api().apiInterface().getListStories(mAge).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Story>> call, @NonNull Response<List<Story>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (page * ITEMS_PAGE > response.body().size()) {
                        for (int i = (page - 1) * ITEMS_PAGE; i < response.body().size(); i++) {
                            mStoryList.add(response.body().get(i));
                        }
                    } else {
                        for (int i = (page - 1) * ITEMS_PAGE; i < (page * ITEMS_PAGE); i++) {
                            mStoryList.add(response.body().get(i));
                        }
                    }
                    mStoryView.setData(mStoryList);
                }
                Log.e(TAG, "api getStory: success");
            }

            @Override
            public void onFailure(@NonNull Call<List<Story>> call, @NonNull Throwable t) {
                Log.e(TAG, "api getStory: fail");
            }
        });
    }

    @Override
    public void loadNextPage() {
        isLoading = true;
        mStoryView.getData(++mCurrentPage);
        isLoading = false;

        if (mCurrentPage == mTotalPage) {
            isLastPage = true;
        }
    }

    @Override
    public void swipeData() {
        mStoryList.clear();
        isLastPage = false;
        mCurrentPage = START_PAGE;
        mStoryView.getData(START_PAGE);
    }
}
