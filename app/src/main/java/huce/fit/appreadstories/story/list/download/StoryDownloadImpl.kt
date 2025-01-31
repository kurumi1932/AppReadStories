package huce.fit.appreadstories.story.list.download

import android.content.Context
import huce.fit.appreadstories.sqlite.AppDatabase
import huce.fit.appreadstories.story.list.BaseStoryListImpl

class StoryDownloadImpl(private val storyDownloadView: StoryDownloadView, context: Context) :
    BaseStoryListImpl(context), StoryDownloadPresenter {

    companion object {
        private const val TAG = "StoryDownloadImpl"
    }

    private var mAppDao = AppDatabase.getInstance(context).appDao()

    override fun getData() {
        storyDownloadView.setData(mAppDao.allStory)
    }
}
