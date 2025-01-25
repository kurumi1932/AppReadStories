package huce.fit.appreadstories.story.list.filter

import huce.fit.appreadstories.model.Story

interface StoryFilterView {
    fun setData(storyList: List<Story>)
}