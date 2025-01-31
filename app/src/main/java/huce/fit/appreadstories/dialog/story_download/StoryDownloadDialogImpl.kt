package huce.fit.appreadstories.dialog.story_download

import android.content.Context
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDatabase

class StoryDownloadDialogImpl(val context: Context) : StoryDownloadDialogPresenter {

    override fun getStoryName(): String {
        val storySharedPreferences = StorySharedPreferences(context)
        storySharedPreferences.setSharedPreferences("Story", Context.MODE_PRIVATE)
        val appDao = AppDatabase.getInstance(context).appDao()
        return appDao.getStory(storySharedPreferences.getStoryId()).storyName
    }
}
