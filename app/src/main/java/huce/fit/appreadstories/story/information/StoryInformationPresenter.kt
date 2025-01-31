package huce.fit.appreadstories.story.information

import huce.fit.appreadstories.model.Story

interface StoryInformationPresenter {
    fun start()
    fun showRate(ratePoint: Int)
    fun getDataOffline(storyDao: Story)
    fun checkAge(): Boolean
    fun checkInteractive()
    fun checkFollowStory()
    fun checkFollowStoryDownload()
    fun likeStory()
    fun followStory()
    fun readStory()
    fun readStoryDownload()

    fun checkRateOfAccount()
    fun setRateId(rateId: Int)
    fun addRate(ratePoint: Int, rate: String)
    fun updateRate(ratePoint: Int, rate: String)
    fun deleteRate()
    fun enterComment()
    fun enterDownload()
    fun enterChapter()
    fun enterReadStory()
}