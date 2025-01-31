package huce.fit.appreadstories.story.list.filter

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.list.BaseStoryListImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryFilterImpl(val storyFilterView: StoryFilterView, context: Context) :
    BaseStoryListImpl(context), StoryFilterPresenter {

    companion object {
        private const val TAG = "StoryFilterImpl"
    }

    private var species = ""
    private var status = ""

    override fun setSpecies(species: String) {
        this.species = species
    }

    override fun setStatus(status: String) {
        this.status = status
    }

    override fun getData() {
        Log.e(TAG, "NHT mSpecies= $species status= $status")
        val age = getAccount().getAge()
        Api().apiInterface().getListStoriesFilter(species, status, age)
            .enqueue(object : Callback<List<Story>> {
                override fun onResponse(call: Call<List<Story>>, response: Response<List<Story>>) {
                    val storyServer = response.body()
                    if (response.isSuccessful && storyServer != null) {
                        storyFilterView.setData(storyServer)
                    }
                    Log.e(TAG, "api getData: success")
                }

                override fun onFailure(call: Call<List<Story>>, t: Throwable) {
                    Log.e(TAG, "api getData: fail")
                }
            })
    }
}
