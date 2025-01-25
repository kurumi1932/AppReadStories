package huce.fit.appreadstories.shared_preferences

import android.content.Context

class StorySharedPreferences(context: Context) : BaseSharedPreferences(context) {

    fun getStoryId(): Int {
        return sharedPreferences.getInt("storyId", 0)
    }

    fun setStoryId(storyId: Int) {
        edit.putInt("storyId", storyId)
    }

    fun getStoryName(): String? {
        return sharedPreferences.getString("storyName", "")
    }

    fun setStoryName(storyName: String) {
        edit.putString("storyName", storyName)
    }

    fun getAuthor(): String? {
        return sharedPreferences.getString("author", "")
    }

    fun setAuthor(author: String) {
        edit.putString("author", author)
    }

    fun getStatus(): String? {
        return sharedPreferences.getString("status", "")
    }

    fun setStatus(status: String) {
        edit.putString("status", status)
    }

    fun getSpecies(): String? {
        return sharedPreferences.getString("species", "")
    }

    fun setSpecies(species: String) {
        edit.putString("species", species)
    }

    fun getSumChapter(): Int {
        return sharedPreferences.getInt("sumChapter", 0)
    }

    fun setSumChapter(sumChapter: Int) {
        edit.putInt("sumChapter", sumChapter)
    }

    fun setTotalLikes(totalLikes: Int) {
        edit.putInt("totalLikes", totalLikes)
    }

    fun getTotalLikes(): Int {
        return sharedPreferences.getInt("totalLikes", 0)
    }

    fun setTotalViews(totalViews: Int) {
        edit.putInt("totalViews", totalViews)
    }

    fun getTotalViews(): Int {
        return sharedPreferences.getInt("totalViews", 0)
    }

    fun setTotalComments(totalComments: Int) {
        edit.putInt("totalComments", totalComments)
    }

    fun getTotalComments(): Int {
        return sharedPreferences.getInt("totalComments", 0)
    }

    fun getRatePoint(): Float {
        return sharedPreferences.getFloat("ratePoint", 0f)
    }

    fun setRatePoint(ratePoint: Float) {
        edit.putFloat("ratePoint", ratePoint)
    }

    fun getIntroduce(): String? {
        return sharedPreferences.getString("introduce", "")
    }

    fun setIntroduce(introduce: String) {
        edit.putString("introduce", introduce)
    }

    fun getImage(): String? {
        return sharedPreferences.getString("image", "")
    }

    fun setImage(image: String) {
        edit.putString("image", image)
    }

    fun getAgeLimit(): Int {
        return sharedPreferences.getInt("ageLimit", 18)
    }

    fun setAgeLimit(ageLimit: Int) {
        edit.putInt("ageLimit", ageLimit)
    }

    fun getTimeUpdate(): String? {
        return sharedPreferences.getString("timeUpdate", "")
    }

    fun setTimeUpdate(timeUpdate: String) {
        edit.putString("timeUpdate", timeUpdate)
    }

    fun getChapterReading(): Int {
        return sharedPreferences.getInt("chapterReadingId", 0)
    }

    fun setChapterReading(chapterId: Int) {
        edit.putInt("chapterReadingId", chapterId)
    }

    fun getIsFollow(): Boolean {
        return sharedPreferences.getBoolean("isFollow", false)
    }

    fun setIsFollow(isFollow: Boolean) {
        edit.putBoolean("isFollow", isFollow)
    }
}
