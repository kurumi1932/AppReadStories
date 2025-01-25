package huce.fit.appreadstories.story.list

import android.content.Context
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences

open class BaseStoryListImpl(val context: Context) : BaseStoryListPresenter {

    companion object{
        private const val TAG = "BaseStoryListImpl"
    }

    private var mStory: StorySharedPreferences = StorySharedPreferences(context)

    override fun getAccount(): AccountSharedPreferences {
        val account = AccountSharedPreferences(context)
        account.getSharedPreferences("Account", Context.MODE_PRIVATE)
        return account
    }

    private fun setStory() {
        mStory.setSharedPreferences("Story", Context.MODE_PRIVATE)
    }

    private fun getStory() {
        mStory.getSharedPreferences("Story", Context.MODE_PRIVATE)
    }

    override fun getStoryId(): Int {
        getStory()
        return mStory.getStoryId()
    }

    override fun setStoryId(storyId: Int) {
        setStory()
        mStory.setStoryId(storyId)
        mStory.myApply()
    }
}
