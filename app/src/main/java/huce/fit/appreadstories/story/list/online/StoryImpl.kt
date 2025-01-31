package huce.fit.appreadstories.story.list.online

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.list.BaseStoryListImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryImpl(val storyView: StoryView, context: Context): BaseStoryListImpl(context) , StoryPresenter {
    
    companion object{
        private const val TAG = "StoryImpl"
        private const val ITEMS_PAGE = 8 // số item có trong trang
        private const val START_PAGE = 1
    }

    private val storyList: MutableList<Story> = ArrayList()
    private var isLoading = false
    private var isLastPage = false
    private var age = 0
    private var currentPage = 0 // page hiện tại
    private var totalPage = 0 // tổng số trang

    init{
        age = getAccount().getAge()
        currentPage = START_PAGE
        totalPage()
    }

    override fun isLoading() = isLoading

    override fun isLastPage() = isLastPage

    override fun totalPage() {
        Api().apiInterface().getListStories(age).enqueue(object : Callback<List<Story>> {
            override fun onResponse(call: Call<List<Story>>, response: Response<List<Story>>) {
                val listStoryServer = response.body()
                if (response.isSuccessful && listStoryServer != null) {
                    Log.e("NHT number_item:", listStoryServer.size.toString())
                    totalPage = (listStoryServer.size / ITEMS_PAGE)
                    if ((totalPage * ITEMS_PAGE) < listStoryServer.size) {
                        ++totalPage
                    }
                    Log.e("NHT number_page:", totalPage.toString())
                }
                Log.e(TAG, "api totalPage: success")
            }

            override fun onFailure(call: Call<List<Story>>, t: Throwable) {
                Log.e(TAG, "api totalPage: fail")
            }
        })
    }

    override fun getStory(page: Int) {
        Api().apiInterface().getListStories(age).enqueue(object : Callback<List<Story>> {
            override fun onResponse(call: Call<List<Story>>, response: Response<List<Story>>) {
                val listStoryServer = response.body()
                if (response.isSuccessful && listStoryServer != null) {
                    if (page * ITEMS_PAGE > listStoryServer.size) {
                        for (i in (page - 1) * ITEMS_PAGE until listStoryServer.size) {
                            storyList.add(listStoryServer[i])
                        }
                    } else {
                        for (i in (page - 1) * ITEMS_PAGE until (page * ITEMS_PAGE)) {
                            storyList.add(listStoryServer[i])
                        }
                    }
                    storyView.setData(storyList)
                }
                Log.e(TAG, "api getStory: success")
            }

            override fun onFailure(call: Call<List<Story>>, t: Throwable) {
                Log.e(TAG, "api getStory: fail")
            }
        })
    }

    override fun loadNextPage() {
        isLoading = true
        storyView.getData(++currentPage)
        isLoading = false

        if (currentPage == totalPage) {
            isLastPage = true
        }
    }

    override fun swipeData() {
        storyList.clear()
        isLastPage = false
        currentPage = START_PAGE
        storyView.getData(START_PAGE)
    }
}