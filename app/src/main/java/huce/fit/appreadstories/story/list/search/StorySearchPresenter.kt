package huce.fit.appreadstories.story.list.search

import huce.fit.appreadstories.story.list.BaseStoryListPresenter

interface StorySearchPresenter : BaseStoryListPresenter {
    fun getStoryListSearch(name: String)
}
