package huce.fit.appreadstories.story.list.online

import huce.fit.appreadstories.story.list.BaseStoryListPresenter

interface StoryPresenter : BaseStoryListPresenter {
    fun totalPage()
    fun getStory(page: Int)
    fun loadNextPage()
    fun isLoading(): Boolean
    fun isLastPage(): Boolean
    fun swipeData()
}
