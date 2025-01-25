package huce.fit.appreadstories.chapter.list

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDao
import huce.fit.appreadstories.sqlite.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChapterListImpl(private val chapterListView: ChapterListView) : ChapterListPresenter {

    companion object{
        private const val TAG = "ChapterListImpl"
    }

    private var mContext: Context = chapterListView as Context
    private var mAppDao: AppDao = AppDatabase.getInstance(mContext).appDao()
    private var mAccountId = 0
    private var mStoryId = 0
    private var mChapterReadingId = 0

    init {
        val account = AccountSharedPreferences(mContext)
        account.getSharedPreferences("Account", Context.MODE_PRIVATE)
        val story = StorySharedPreferences(mContext)
        story.getSharedPreferences("Story", Context.MODE_PRIVATE)
        mAccountId = account.getAccountId()
        mStoryId = story.getStoryId()
        mChapterReadingId = story.getChapterReading()
    }

    override fun getData() {
        if (isConnecting(mContext)) {
            //list chapter read
            Api().apiInterface().getChapterListRead(mStoryId, mAccountId, 0)
                .enqueue(object : Callback<List<ChapterRead>> {
                    override fun onResponse(call: Call<List<ChapterRead>>, response: Response<List<ChapterRead>>) {
                        if (response.isSuccessful && response.body() != null) {
                            chapterListView.setData(response.body()!!, mChapterReadingId)
                        }
                        Log.e(TAG, "api getData getChapterListRead: success")
                    }

                    override fun onFailure(call: Call<List<ChapterRead>>, t: Throwable) {
                        Log.e(TAG, "api getData getChapterListRead: fail")
                    }
                })
            //list chapter
            Api().apiInterface().getChapterList(mStoryId)
                .enqueue(object : Callback<List<Chapter>> {
                    override fun onResponse(
                        call: Call<List<Chapter>>,
                        response: Response<List<Chapter>>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            chapterListView.setData(response.body()!!)
                        }
                        Log.e(TAG, "api getData getChapterList: success")
                    }

                    override fun onFailure(call: Call<List<Chapter>>, t: Throwable) {
                        Log.e(TAG, "api getData getChapterList: fail")
                    }
                })
        } else {
            chapterListView.setData(mAppDao.getChapterList(mStoryId))
            chapterListView.setData(mAppDao.getChapterReadList(mStoryId), mChapterReadingId)
        }
    }
}
