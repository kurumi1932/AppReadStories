package huce.fit.appreadstories.story.list.filter

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.story.list.BaseStoryListImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryFilterImpl(
    val storyFilterView: StoryFilterView, context: Context
) : BaseStoryListImpl(context), StoryFilterPresenter {

    companion object {
        private const val TAG = "StoryFilterImpl"
    }

    private var speciesId = 0
    private var statusId = 0
    private var age = 0
    private var species = ""
    private var status = ""

    init {
        age = getAccount().getAge()
    }

    override fun setSpeciesId(speciesId: Int) {
        this.speciesId = speciesId
    }

    override fun setStatusId(statusId: Int) {
        this.statusId = statusId
    }

    override fun setSpecies(species: String) {
        this.species = species
    }

    override fun setStatus(status: String) {
        this.status = status
    }

    override fun getSpeciesId(): Int {
        return speciesId
    }

    override fun getStatusId(): Int {
        return statusId
    }

    override fun getData() {
        Log.e(TAG, "NHT mSpecies= $species status= $status")
        Api().apiInterface().getListStoriesFilter(species, status, age)
            .enqueue(object : Callback<List<Story>> {
                override fun onResponse(call: Call<List<Story>>, response: Response<List<Story>>) {
                    if (response.isSuccessful) {
                        val stories = response.body()
                        if (stories != null) {
                            storyFilterView.setData(stories)
                        }
                    }
                    Log.e(TAG, "api getData: success")
                }

                override fun onFailure(call: Call<List<Story>>, t: Throwable) {
                    Log.e(TAG, "api getData: fail")
                }
            })
    }
}
