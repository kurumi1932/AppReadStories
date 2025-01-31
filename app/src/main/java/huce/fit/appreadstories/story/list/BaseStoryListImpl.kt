package huce.fit.appreadstories.story.list

import android.content.Context
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences

open class BaseStoryListImpl(val context: Context) : BaseStoryListPresenter {
    
    private var story = StorySharedPreferences(context)

    override fun getAccount(): AccountSharedPreferences {
        val account = AccountSharedPreferences(context)
        account.getSharedPreferences("Account", Context.MODE_PRIVATE)
        return account
    }

    override fun getStoryId(): Int {
        story.getSharedPreferences("Story", Context.MODE_PRIVATE)
        return story.getStoryId()
    }

    override fun setStoryId(storyId: Int) {
        story.setSharedPreferences("Story", Context.MODE_PRIVATE)
        story.setStoryId(storyId)
        story.myApply()
    }
}
