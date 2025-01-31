package huce.fit.appreadstories.chapter.list

import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead

interface ChapterListView {
    fun setData(chapterList: List<Chapter>)
    fun setData(chapterListRead: List<ChapterRead>, chapterReadingId: Int)
    fun reverse()
}