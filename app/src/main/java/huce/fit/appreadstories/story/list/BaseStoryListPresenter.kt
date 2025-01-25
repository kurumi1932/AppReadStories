package huce.fit.appreadstories.story.list

import huce.fit.appreadstories.shared_preferences.BaseSharedPreferences

interface BaseStoryListPresenter {
    fun getAccount(): BaseSharedPreferences
    fun getStoryId(): Int
    fun setStoryId(storyId: Int)
}