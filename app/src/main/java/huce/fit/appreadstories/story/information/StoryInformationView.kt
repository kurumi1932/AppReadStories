package huce.fit.appreadstories.story.information

import huce.fit.appreadstories.model.Story

interface StoryInformationView {
    fun close()
    fun setRateStartLight(index: Int)
    fun setRateStartDark(index: Int)
    fun setData(story: Story?)
    fun checkAge(isCheckAge: kotlin.Boolean)
    fun checkInteractive(isLike: Int, likeNumber: kotlin.String?)
    fun followStory(isFollow: Int)
    fun openDialogRate()
    fun addRate(ratePoint: Int, rate: kotlin.String?)
    fun updateRate(ratePoint: Int, rate: kotlin.String?)
    fun deleteRate()
    fun reloadViewPaper(text: kotlin.String?)
    fun checkStory()
}