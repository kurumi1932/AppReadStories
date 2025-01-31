package huce.fit.appreadstories.shared_preferences

import android.content.Context

class StorySharedPreferences(context: Context) : BaseSharedPreferences(context) {

    fun getStoryId(): Int {
        return sharedPreferences.getInt("storyId", 0)
    }

    fun setStoryId(storyId: Int) {
        edit.putInt("storyId", storyId)
    }

    fun getChapterReading(): Int {
        return sharedPreferences.getInt("chapterReadingId", 0)
    }

    fun setChapterReading(chapterId: Int) {
        edit.putInt("chapterReadingId", chapterId)
    }
}