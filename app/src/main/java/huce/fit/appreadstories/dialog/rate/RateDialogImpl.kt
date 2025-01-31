package huce.fit.appreadstories.dialog.rate

import android.content.Context
import android.util.Log
import huce.fit.appreadstories.api.Api
import huce.fit.appreadstories.model.Rate
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import huce.fit.appreadstories.shared_preferences.StorySharedPreferences
import huce.fit.appreadstories.story.information.StoryInformationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RateDialogImpl(
    private val storyInformationView: StoryInformationView,
    private val rateDialogView: RateDialogView
) : RateDialogPresenter {

    companion object {
        private var TAG = "RateDialogImpl"
    }

    private var mContext = storyInformationView as Context
    private lateinit var mRate: Rate
    private var mRatePoint = 0

    init {
        setRate()
    }

    private fun setRate() {
        val accountSharedPreferences = AccountSharedPreferences(mContext)
        accountSharedPreferences.getSharedPreferences("Account", Context.MODE_PRIVATE)

        val storySharedPreferences = StorySharedPreferences(mContext)
        storySharedPreferences.getSharedPreferences("Story", Context.MODE_PRIVATE)

        val accountId = accountSharedPreferences.getAccountId()
        val storyId = storySharedPreferences.getStoryId()

        Api().apiInterface().checkRateOfAccount(storyId, accountId)
            .enqueue(object : Callback<Rate> {
                override fun onResponse(call: Call<Rate>, response: Response<Rate>) {
                    val rateServer = response.body()
                    if (response.isSuccessful && rateServer != null) {
                        mRate = rateServer
                        rateDialogView.setData(rateServer)
                        storyInformationView.setRateId(rateServer.rateId)
                    }
                    Log.e(TAG, "api checkRateOfAccount: success")
                }

                override fun onFailure(call: Call<Rate>, t: Throwable) {
                    Log.e(TAG, "api checkRateOfAccount: false")
                }
            })
    }


    override fun setRatePoint(ratePoint: Int) {
        mRatePoint = ratePoint
    }

    override fun rate(rateContent: String) {
        if (mRate.rateSuccess == 1) { //update rate
            storyInformationView.updateRate(mRatePoint, rateContent)
        } else { //add rate
            if (mRatePoint > 0) {
                storyInformationView.addRate(mRatePoint, rateContent)
                rateDialogView.dismiss()
            } else {
                rateDialogView.noRatePoint()
            }
        }
    }
}
