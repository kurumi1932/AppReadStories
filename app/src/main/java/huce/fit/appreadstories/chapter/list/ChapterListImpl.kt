package huce.fit.appreadstories.chapter.list

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChapterListImpl(private val chapterListView: ChapterListView) : ChapterListPresenter {

    companion object{
        private const val TAG = "ChapterListImpl"
    }

    private var context = chapterListView as Context
    private var appDao = AppDatabase.getInstance(context).appDao()
    private var accountId = 0
    private var storyId = 0
    private var chapterReadingId = 0

    init {
        val account = AccountSharedPreferences(context)
        account.getSharedPreferences("Account", Context.MODE_PRIVATE)
        val story = StorySharedPreferences(context)
        story.getSharedPreferences("Story", Context.MODE_PRIVATE)
        accountId = account.getAccountId()
        storyId = story.getStoryId()
        chapterReadingId = story.getChapterReading()
    }

    override fun getData() {
        if (isConnecting(context)) {
            //list chapter read
            Api().apiInterface().getChapterListRead(storyId, accountId, 0)
                .enqueue(object : Callback<List<ChapterRead>> {
                    override fun onResponse(call: Call<List<ChapterRead>>, response: Response<List<ChapterRead>>) {
                        val chapterReadServer = response.body()
                        if (response.isSuccessful && chapterReadServer != null) {
                            chapterListView.setData(chapterReadServer, chapterReadingId)
                        }
                        Log.e(TAG, "api getData getChapterListRead: success")
                    }

                    override fun onFailure(call: Call<List<ChapterRead>>, t: Throwable) {
                        Log.e(TAG, "api getData getChapterListRead: fail")
                    }
                })
            //list chapter
            Api().apiInterface().getChapterList(storyId)
                .enqueue(object : Callback<List<Chapter>> {
                    override fun onResponse(
                        call: Call<List<Chapter>>, response: Response<List<Chapter>>
                    ) {
                        val chapterListServer = response.body()
                        if (response.isSuccessful && chapterListServer != null) {
                            chapterListView.setData(chapterListServer)
                        }
                        Log.e(TAG, "api getData getChapterList: success")
                    }

                    override fun onFailure(call: Call<List<Chapter>>, t: Throwable) {
                        Log.e(TAG, "api getData getChapterList: fail")
                    }
                })
        } else {
            chapterListView.setData(appDao.getChapterList(storyId))
            chapterListView.setData(appDao.getChapterReadList(storyId), chapterReadingId)
        }
    }
}
