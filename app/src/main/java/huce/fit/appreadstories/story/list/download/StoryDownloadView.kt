package huce.fit.appreadstories.story.list.download

import huce.fit.appreadstories.model.Story

interface StoryDownloadView {
    fun setData(storyList: List<Story>)
    fun openStoryDownload()
    fun deleteStoryDownload()
}
