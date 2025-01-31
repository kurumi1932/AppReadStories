package huce.fit.appreadstories.dialog.chapter_list

import huce.fit.appreadstories.dialog.BaseDialogView
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.Story

interface ChapterListDialogView : BaseDialogView {
    fun setData(story: Story)
    fun setChapterList(chapterList: List<Chapter>)
}
