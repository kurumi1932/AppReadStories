package huce.fit.appreadstories.story.download

import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.Story


interface StoryDownloadView {
    fun setDataStory(story: Story)
    fun setChapterList(chapterList: List<Chapter>)
}
