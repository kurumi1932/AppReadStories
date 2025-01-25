package huce.fit.appreadstories.dialog.story_download;

import android.content.Context;

import huce.fit.appreadstories.shared_preferences.StorySharedPreferences;
import huce.fit.appreadstories.sqlite.AppDao;
import huce.fit.appreadstories.sqlite.AppDatabase;

public class StoryDownloadDialogImpl implements StoryDownloadDialogPresenter{

    private final Context mContext;
    private final AppDao mAppDao;

    public StoryDownloadDialogImpl(Context context) {
        mContext = context;
        mAppDao = AppDatabase.getInstance(context).appDao();
    }

    @Override
    public String getStoryName(){
        StorySharedPreferences storySharedPreferences = new StorySharedPreferences(mContext);
        storySharedPreferences.setSharedPreferences("Story", Context.MODE_PRIVATE);
        return mAppDao.getStory(storySharedPreferences.getStoryId()).getStoryName();
    };
}
