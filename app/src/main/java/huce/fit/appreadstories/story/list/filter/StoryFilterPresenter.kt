package huce.fit.appreadstories.story.list.filter

import huce.fit.appreadstories.story.list.BaseStoryListPresenter

interface StoryFilterPresenter : BaseStoryListPresenter {
    fun setSpecies(species: String)
    fun setStatus(status: String)
    fun getData()
}