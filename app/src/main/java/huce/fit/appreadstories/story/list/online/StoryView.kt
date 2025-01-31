package huce.fit.appreadstories.story.list.online

import huce.fit.appreadstories.model.Story

interface StoryView {
    fun getData(page: Int)
    fun setData(storyList: List<Story>)
}
