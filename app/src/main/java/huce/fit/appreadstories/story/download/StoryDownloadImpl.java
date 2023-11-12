package huce.fit.appreadstories.story.download;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.sqlite.Story;

public class StoryDownloadImpl implements StoryDownloadPresenter{

    private static final String TAG = "StoryDownloadImpl";

    private final StoryDownloadView mStoryDownloadView;
    private List<Story> mListStory = new ArrayList<>();
    private Context mContext;

    StoryDownloadImpl(StoryDownloadView storyDownloadView, Context context) {
        mStoryDownloadView = storyDownloadView;
        mContext = context;
    }

    @Override
    public void getData() {
        mListStory = AppDatabase.getInstance(mContext).appDao().getAllStory();
        mStoryDownloadView.setData(mListStory);
    }

    public String getStoryName(int storyId){
        return mListStory.get(storyId).getNameStory();
    };
}
