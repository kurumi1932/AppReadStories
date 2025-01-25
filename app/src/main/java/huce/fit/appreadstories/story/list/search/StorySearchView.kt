package huce.fit.appreadstories.story.list.search

import huce.fit.appreadstories.model.Story

interface StorySearchView {

    fun setData(storyList: List<Story>)
}