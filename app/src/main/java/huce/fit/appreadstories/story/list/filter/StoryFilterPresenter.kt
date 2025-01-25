package huce.fit.appreadstories.story.list.filter

import huce.fit.appreadstories.story.list.BaseStoryListPresenter

interface StoryFilterPresenter : BaseStoryListPresenter {

    fun setSpeciesId(speciesId: Int)

    fun setStatusId(statusId: Int)

    fun setSpecies(species: String)

    fun setStatus(status: String)

    fun getSpeciesId(): Int

    fun getStatusId(): Int

    fun getData()
}