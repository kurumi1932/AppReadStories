package huce.fit.appreadstories.chapter.information

import huce.fit.appreadstories.shared_preferences.SettingSharedPreferences

interface ChapterInformationPresenter {
    fun getSetting(): SettingSharedPreferences
    fun changeChapter(changeChapter: Int)
    fun getChapter(chapterId: Int)
    fun insertChapterRead(chapterId: Int)
}