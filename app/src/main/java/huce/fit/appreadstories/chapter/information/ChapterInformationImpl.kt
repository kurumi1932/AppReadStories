package huce.fit.appreadstories.chapter.information

import android.content.Context
import android.content.Intent
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.ChapterRead
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.SettingSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDao
import huce.fit.appreadstories.sqlite.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChapterInformationImpl(
    val chapterInformationView: ChapterInformationView,
    intent: Intent
) : ChapterInformationPresenter {

    companion object {
        const val TAG = "ChapterInformationImpl"
    }

    private var mContext: Context = chapterInformationView as Context
    private var mAppDao: AppDao = AppDatabase.getInstance(mContext).appDao()
    private var mStory = StorySharedPreferences(mContext)
    private var mSetting = SettingSharedPreferences(mContext)

    private var mStoryId = 0
    private var mChapterId = 0
    private var mAccountId = 0
    private var chapterFirstId = 0
    private var chapterFinalId: Int = 0

    init {
        mAccountId = getAccount().getAccountId()
        getStory()
        mStoryId = mStory.getStoryId()
        chapterFirstId = mAppDao.getChapterFirst(mStoryId)
        chapterFinalId = mAppDao.getChapterFinal(mStoryId)
        mChapterId = intent.getIntExtra("chapterId", 0)
        if (mChapterId == 0) {
            mChapterId = mStory.getChapterReading()
        }
        Log.e(TAG, "NHT chapterId: $mChapterId")
        getSetting()
        val mTextSize = mSetting.getTextSize()
        //lineSpacingMultiplier = lineheight/textsize = mult
        //lineSpacingExtra = lineheight - textsize = add
        //setLineSpacing = lineheight * mul + add
        val mLineStretch = mSetting.getLineStretch().toString().toFloat() / 100
        val mTextColor = mSetting.getTextColor()
        val mBackgroundColor = mSetting.getBackgroundColor()
        chapterInformationView.setTextSize(mTextSize)
        chapterInformationView.setLineStretch(mLineStretch)
        chapterInformationView.setTextColor(mTextColor!!)
        chapterInformationView.setBackgroundColor(mBackgroundColor!!)
    }

    private fun getAccount(): AccountSharedPreferences {
        val accountSharedPreferences = AccountSharedPreferences(mContext)
        accountSharedPreferences.getSharedPreferences("Account", Context.MODE_PRIVATE)
        return accountSharedPreferences
    }

    private fun getStory() {
        mStory.getSharedPreferences("Story", Context.MODE_PRIVATE)
    }

    override fun getSetting(): SettingSharedPreferences {
        mSetting.getSharedPreferences("Setting", Context.MODE_PRIVATE)
        return mSetting
    }

    override fun changeChapter(changeChapter: Int) {
        when (changeChapter) {
            1 -> getChapter(1, "Đây là chương đầu!")
            2 -> getChapter(mChapterId)
            3 -> getChapter(3, "Đây là chương mới nhất!")
        }
    }

    override fun getChapter(chapterId: Int) {
        mChapterId = chapterId
        if (isConnecting(mContext)) {
            Api().apiInterface().getChapter(mStoryId, chapterId, 2, mAccountId)
                .enqueue(object : Callback<Chapter> {
                    override fun onResponse(call: Call<Chapter>, response: Response<Chapter>) {
                        if (response.isSuccessful && response.body() != null) {
                            val chapter = response.body()
                            chapterInformationView.setData(chapter!!, "")
                        }
                        Log.e(TAG, "api getChapter(1): success")
                    }

                    override fun onFailure(call: Call<Chapter>, t: Throwable) {
                        Log.e(TAG, "api getChapter(1): fail")
                    }
                })
        } else {
            chapterInformationView.setData(mAppDao.getChapter(mStoryId, chapterId), "")
        }
    }

    private fun getChapter(chapterChange: Int, text: String) {
        if (isConnecting(mContext)) {
            Api().apiInterface().getChapter(mStoryId, mChapterId, chapterChange, mAccountId)
                .enqueue(object : Callback<Chapter> {
                    override fun onResponse(call: Call<Chapter>, response: Response<Chapter>) {
                        if (response.isSuccessful && response.body() != null) {
                            val chapter = response.body()
                            mChapterId = chapter!!.chapterId
                            chapterInformationView.setData(chapter, text)
                        }
                        Log.e(TAG, "api getChapter(2): success")
                    }

                    override fun onFailure(call: Call<Chapter>, t: Throwable) {
                        Log.e(TAG, "api getChapter(2): fail")
                    }
                })
        } else {
            when (chapterChange) {
                1 -> mAppDao.getNextChapter(mStoryId, mChapterId, chapterFirstId)
                3 -> mAppDao.getPreviousChapter(mStoryId, mChapterId, chapterFinalId)
            }
        }
    }

    override fun insertChapterRead(chapterId: Int) {
        var chapterRead =
            AppDatabase.getInstance(mContext).appDao().checkChapterRead(mStoryId, chapterId)
        if (chapterRead == null) {
            //update chapterReadId
            AppDatabase.getInstance(mContext).appDao().updateChapterReadId(mStoryId, chapterId)

            //insert chapterRead
            chapterRead = ChapterRead()
            chapterRead.storyId = mStoryId
            chapterRead.chapterId = chapterId
            AppDatabase.getInstance(mContext).appDao().insertChapterRead(chapterRead)
        }
    }

}
