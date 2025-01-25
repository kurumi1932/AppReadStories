package huce.fit.appreadstories.chapter.information

import huce.fit.appreadstories.model.Chapter

interface ChapterInformationView {

    fun setBackgroundColor(backgroundColor: String)
    fun setTextColor(textColor: String)
    fun setTextSize(size: Int)
    fun setLineStretch(lineStretch: Float)
    fun setData(chapter: Chapter, text: String)
    fun reloadChapter(changeChapter: Int)
    fun moveChapter(chapterId: Int)
}