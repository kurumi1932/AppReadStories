package huce.fit.appreadstories.story.list.download;

import android.content.Context;

import huce.fit.appreadstories.sqlite.AppDao;
import huce.fit.appreadstories.sqlite.AppDatabase;
import huce.fit.appreadstories.story.list.BaseStoryListImpl;

public class StoryDownloadImpl extends BaseStoryListImpl implements StoryDownloadPresenter {

    private static final String TAG = "StoryDownloadImpl";
    private final StoryDownloadView mStoryDownloadView;
    private final AppDao mAppDao;

    StoryDownloadImpl(StoryDownloadView storyDownloadView, Context context) {
        super(context);
        mStoryDownloadView = storyDownloadView;
        mAppDao = AppDatabase.getInstance(context).appDao();
    }

    @Override
    public void getData() {
        mStoryDownloadView.setData(mAppDao.getAllStory());
    }
}
