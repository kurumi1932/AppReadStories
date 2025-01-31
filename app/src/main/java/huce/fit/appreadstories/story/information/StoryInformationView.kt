package huce.fit.appreadstories.story.information

import huce.fit.appreadstories.model.Story

interface StoryInformationView {
    fun close()
    fun setRateStartLight(index: Int)
    fun setRateStartDark(index: Int)
    fun setData(story: Story)
    fun setRateId(rateId: Int)
    fun checkInteractive(isLike: Int, likeNumber: String)
    fun followStory(isFollow: Int)
    fun openDialogRate()
    fun addRate(ratePoint: Int, rate: String)
    fun updateRate(ratePoint: Int, rate: String)
    fun deleteRate()
    fun reloadViewPaper(text: String)
    fun checkStory()
}