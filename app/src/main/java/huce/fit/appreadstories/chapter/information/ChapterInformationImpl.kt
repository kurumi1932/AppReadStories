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
import huce.fit.appreadstories.sqlite.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChapterInformationImpl(
    val chapterInformationView: ChapterInformationView, intent: Intent
) : ChapterInformationPresenter {

    companion object {
        const val TAG = "ChapterInformationImpl"
    }

    private var context = chapterInformationView as Context
    private var appDao = AppDatabase.getInstance(context).appDao()
    private var storySharedPreferences = StorySharedPreferences(context)
    private var settingSharedPreferences = SettingSharedPreferences(context)

    private var storyId = 0
    private var chapterId = 0
    private var accountId = 0
    private var chapterFirstId = 0
    private var chapterFinalId: Int = 0

    init {
        accountId = getAccount().getAccountId()
        getStory()
        storyId = storySharedPreferences.getStoryId()
        chapterFirstId = appDao.getChapterFirst(storyId)
        chapterFinalId = appDao.getChapterFinal(storyId)
        chapterId = intent.getIntExtra("chapterId", 0)
        if (chapterId == 0) {
            chapterId = storySharedPreferences.getChapterReading()
        }
        Log.e(TAG, "NHT chapterId: $chapterId")
        getSetting()
        val textSize = settingSharedPreferences.getTextSize()
        //lineSpacingMultiplier = lineheight/textsize = mult
        //lineSpacingExtra = lineheight - textsize = add
        //setLineSpacing = lineheight * mul + add
        val lineStretch = settingSharedPreferences.getLineStretch().toString().toFloat() / 100
        val textColor = settingSharedPreferences.getTextColor()
        val backgroundColor = settingSharedPreferences.getBackgroundColor()
        chapterInformationView.setTextSize(textSize)
        chapterInformationView.setLineStretch(lineStretch)
        chapterInformationView.setTextColor(textColor)
        chapterInformationView.setBackgroundColor(backgroundColor)
    }

    private fun getAccount(): AccountSharedPreferences {
        val accountSharedPreferences = AccountSharedPreferences(context)
        accountSharedPreferences.getSharedPreferences("Account", Context.MODE_PRIVATE)
        return accountSharedPreferences
    }

    private fun getStory() {
        storySharedPreferences.getSharedPreferences("Story", Context.MODE_PRIVATE)
    }

    override fun getSetting(): SettingSharedPreferences {
        settingSharedPreferences.getSharedPreferences("Setting", Context.MODE_PRIVATE)
        return settingSharedPreferences
    }

    override fun changeChapter(changeChapter: Int) {
        when (changeChapter) {
            1 -> getChapter(1, "Đây là chương đầu!")
            2 -> getChapter(chapterId)
            3 -> getChapter(3, "Đây là chương mới nhất!")
        }
    }

    override fun getChapter(chapterId: Int) {
        this.chapterId = chapterId
        if (isConnecting(context)) {
            Api().apiInterface().getChapter(storyId, chapterId, 2, accountId)
                .enqueue(object : Callback<Chapter> {
                    override fun onResponse(call: Call<Chapter>, response: Response<Chapter>) {
                        val chapter = response.body()
                        if (response.isSuccessful && chapter != null) {
                            chapterInformationView.setData(chapter, "")
                        }
                        Log.e(TAG, "api getChapter(1): success")
                    }

                    override fun onFailure(call: Call<Chapter>, t: Throwable) {
                        Log.e(TAG, "api getChapter(1): fail")
                    }
                })
        } else {
            chapterInformationView.setData(appDao.getChapter(storyId, chapterId), "")
        }
    }

    private fun getChapter(chapterChange: Int, text: String) {
        if (isConnecting(context)) {
            Api().apiInterface().getChapter(storyId, chapterId, chapterChange, accountId)
                .enqueue(object : Callback<Chapter> {
                    override fun onResponse(call: Call<Chapter>, response: Response<Chapter>) {
                        val chapter = response.body()
                        if (response.isSuccessful && chapter != null) {
                            chapterId = chapter.chapterId
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
                1 -> appDao.getNextChapter(storyId, chapterId, chapterFirstId)
                3 -> appDao.getPreviousChapter(storyId, chapterId, chapterFinalId)
            }
        }
    }

    override fun insertChapterRead(chapterId: Int) {
        val chapterRead =
            AppDatabase.getInstance(context).appDao().checkChapterRead(storyId, chapterId)
        if (chapterRead == null) {
            //update chapterReadId
            AppDatabase.getInstance(context).appDao().updateChapterReadId(storyId, chapterId)

            //insert chapterRead
            AppDatabase.getInstance(context).appDao().insertChapterRead(ChapterRead(storyId, chapterId))
        }
    }

}
