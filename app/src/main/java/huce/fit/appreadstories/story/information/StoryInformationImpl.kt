package huce.fit.appreadstories.story.information

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.chapter.information.ChapterInformationActivity
import huce.fit.appreadstories.chapter.list.ChapterListActivity
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.comment.CommentActivity
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.model.Rate
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.sqlite.AppDatabase
import huce.fit.appreadstories.story.download.StoryDownloadActivity
import huce.fit.appreadstories.story.list.BaseStoryListImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryInformationImpl(val storyInformationView: StoryInformationView) :
    BaseStoryListImpl(storyInformationView as Context), StoryInformationPresenter {

    companion object {
        private const val TAG = "StoryInformationImpl"
    }

    private var appDao = AppDatabase.getInstance(context).appDao()
    private var storySharedPreferences = StorySharedPreferences(context)
    private lateinit var story: Story
    private var storyId = 0
    private var accountId: Int = 0
    private var rateId: Int = 0
    private var name: String = ""

    init {
        getAccountSharedPreferences()
        getStorySharedPreferences()
    }


    private fun getAccountSharedPreferences() {
        val account = getAccount()
        accountId = account.getAccountId()
        name = account.getName()
    }

    private fun getStorySharedPreferences() {
        storySharedPreferences.getSharedPreferences("Story", Context.MODE_PRIVATE)
        storyId = storySharedPreferences.getStoryId()
        storySharedPreferences.setSharedPreferences("Story", Context.MODE_PRIVATE)
    }

    override fun start() {
        val storyDao = appDao.getStory(storyId)
        if (isConnecting(context)) {
            getStory()
            readStory()
            checkFollowStory()
            checkInteractive()
            if (storyDao != null) {
                storyInformationView.checkStory()
            }
        } else {
            if (storyDao != null) {
                getDataOffline(storyDao)
                readStoryDownload()
                checkFollowStoryDownload()
            }
        }
    }

    override fun getStoryId(): Int {
        return storyId
    }

    private fun getStory() {
        Api().apiInterface().getStory(storyId).enqueue(object : Callback<Story> {
            override fun onResponse(call: Call<Story>, response: Response<Story>) {
                val storyServer = response.body()
                if (response.isSuccessful && storyServer != null) {
                    story = storyServer
                    storyInformationView.setData(storyServer)
                }
                Log.e(TAG, "api getStory: success")
            }

            override fun onFailure(call: Call<Story>, t: Throwable) {
                Log.e(TAG, "api getStory: false")
            }
        })
    }

    override fun showRate(ratePoint: Int) {
        for (i in 0 until ratePoint) {
            storyInformationView.setRateStartLight(i)
        }
        for (i in ratePoint..4) {
            storyInformationView.setRateStartDark(i)
        }
    }

    override fun getDataOffline(storyDao: Story) {
        storyInformationView.setData(storyDao)
    }

    override fun checkAge() = story.ageLimit < 18

    override fun checkInteractive() {
        Api().apiInterface().checkLikeStory(storyId, accountId)
            .enqueue(object : Callback<Story> {
                override fun onResponse(call: Call<Story>, response: Response<Story>) {
                    val story = response.body()
                    if (response.isSuccessful && story != null) {
                        val totalLikes = "${story.totalLikes}\nYêu thích"
                        storyInformationView.checkInteractive(story.storySuccess, totalLikes)
                    }
                    Log.e(TAG, "api checkInteractive: success")
                }

                override fun onFailure(call: Call<Story>, t: Throwable) {
                    Log.e(TAG, "api checkInteractive: false")
                }
            })
    }

    override fun likeStory() {
        Api().apiInterface().likeStory(storyId, accountId).enqueue(object : Callback<Story> {
            override fun onResponse(call: Call<Story>, response: Response<Story>) {
                val story = response.body()
                if (response.isSuccessful && story != null && story.storySuccess == 1) {
                    checkInteractive()
                }
                Log.e(TAG, "api likeStory: success")
            }

            override fun onFailure(call: Call<Story>, t: Throwable) {
                Log.e(TAG, "api likeStory: false")
            }
        })
    }

    override fun followStory() {
        Api().apiInterface().followStory(
            accountId, storyId, story.storyName, story.author, story.ageLimit, story.status,
            story.sumChapter, story.image.toString(), story.timeUpdate
        ).enqueue(object : Callback<Story> {
            override fun onResponse(call: Call<Story>, response: Response<Story>) {
                val storyFollow = response.body()
                if (response.isSuccessful && storyFollow != null) {
                    storyInformationView.followStory(storyFollow.storySuccess)
                }
                Log.e(TAG, "api followStory: success")
            }

            override fun onFailure(call: Call<Story>, t: Throwable) {
                Log.e(TAG, "api followStory: false")
            }
        })
    }

    override fun checkFollowStory() {
        Api().apiInterface().checkStoryFollow(accountId, storyId)
            .enqueue(object : Callback<Story> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Story>, response: Response<Story>) {
                    val story = response.body()
                    if (response.isSuccessful && story != null) {
                        storyInformationView.followStory(story.storySuccess)
                    }
                    Log.e(TAG, "api checkFollowStory: success")
                }

                override fun onFailure(call: Call<Story>, t: Throwable) {
                    Log.e(TAG, "api checkFollowStory: false")
                }
            })
    }

    override fun checkFollowStoryDownload() {
        storyInformationView.followStory(if (story.isFollow == 0) 2 else 1)
    }

    override fun readStory() {
        Api().apiInterface().getChapterReadId(storyId, accountId, 1)
            .enqueue(object : Callback<Chapter> {
                override fun onResponse(call: Call<Chapter>, response: Response<Chapter>) {
                    val chapter = response.body()
                    if (response.isSuccessful && chapter != null) {
                        if (chapter.chapterId == 0) {
                            Api().apiInterface().firstChapter(storyId)
                                .enqueue(object : Callback<Chapter> {
                                    override fun onResponse(
                                        call: Call<Chapter>, response: Response<Chapter>
                                    ) {
                                        val firstChapter = response.body()
                                        if (response.isSuccessful && firstChapter != null) {
                                            storySharedPreferences.setChapterReading(firstChapter.chapterId)
                                            storySharedPreferences.myApply()
                                        }
                                        Log.e(TAG, "api readStory firstChapter: success")
                                    }

                                    override fun onFailure(call: Call<Chapter>, t: Throwable) {
                                        Log.e(TAG, "api readStory firstChapter: false")
                                    }
                                })
                        } else {
                            storySharedPreferences.setChapterReading(chapter.chapterId)
                            storySharedPreferences.myApply()
                        }
                    }
                    Log.e(TAG, "api readStory getChapterReadId: success")
                }

                override fun onFailure(call: Call<Chapter>, t: Throwable) {
                    Log.e(TAG, "api readStory getChapterReadId: false")
                }
            })
    }

    override fun checkRateOfAccount() {
        Api().apiInterface().checkRateOfAccount(storyId, accountId)
            .enqueue(object : Callback<Rate> {
                override fun onResponse(call: Call<Rate>, response: Response<Rate>) {
                    val rateServer = response.body()
                    if (response.isSuccessful && rateServer != null) {
                        storyInformationView.openDialogRate()
                    }
                    Log.e(TAG, "api checkRateOfAccount: success")
                }

                override fun onFailure(call: Call<Rate>, t: Throwable) {
                    Log.e(TAG, "api checkRateOfAccount: false")
                }
            })
    }

    override fun setRateId(rateId: Int) {
        this.rateId = rateId
    }

    override fun addRate(ratePoint: Int, rate: String) {
        Api().apiInterface().addRate(
            storyId, accountId, name, ratePoint, rate
        ).enqueue(object : Callback<Rate> {
            override fun onResponse(call: Call<Rate>, response: Response<Rate>) {
                val rateServer = response.body()
                if (response.isSuccessful && rateServer != null && rateServer.rateSuccess == 1) {
                    getStory()
                    storyInformationView.reloadViewPaper("Bạn đã đánh giá truyện!")
                }
                Log.e(TAG, "api addRate: success")
            }

            override fun onFailure(call: Call<Rate>, t: Throwable) {
                Log.e(TAG, "api addRate: false")
            }
        })
    }

    override fun updateRate(ratePoint: Int, rate: String) {
        Api().apiInterface().updateRate(rateId, ratePoint, rate)
            .enqueue(object : Callback<Rate> {
                override fun onResponse(call: Call<Rate>, response: Response<Rate>) {
                    val rateServer = response.body()
                    if (response.isSuccessful && rateServer != null && rateServer.rateSuccess == 1) {
                        getStory()
                        storyInformationView.reloadViewPaper("Bạn đã thay đổi đánh giá truyện!")
                    }
                    Log.e(TAG, "api updateRate: success")
                }

                override fun onFailure(call: Call<Rate>, t: Throwable) {
                    Log.e(TAG, "api updateRate: false")
                }
            })
    }

    override fun deleteRate() {
        Api().apiInterface().deleteRate(rateId).enqueue(object : Callback<Rate> {
            override fun onResponse(call: Call<Rate>, response: Response<Rate>) {
                val rateServer = response.body()
                if (response.isSuccessful && rateServer != null && rateServer.rateSuccess == 1) {
                    getStory()
                    storyInformationView.reloadViewPaper("Bạn đã xóa đánh giá!")
                }
                Log.e(TAG, "api deleteRate: success")
            }

            override fun onFailure(call: Call<Rate>, t: Throwable) {
                Log.e(TAG, "api deleteRate: false")
            }
        })
    }

    override fun readStoryDownload() {
        storySharedPreferences.setChapterReading(story.chapterReading)
    }

    override fun enterComment() {
        val intent = Intent(context, CommentActivity::class.java)
        context.startActivity(intent)
    }

    override fun enterDownload() {
        val intent = Intent(context, StoryDownloadActivity::class.java)
        context.startActivity(intent)
    }

    override fun enterChapter() {
        val intent = Intent(context, ChapterListActivity::class.java)
        context.startActivity(intent)
    }

    override fun enterReadStory() {
        val intent = Intent(context, ChapterInformationActivity::class.java)
        context.startActivity(intent)
    }
}