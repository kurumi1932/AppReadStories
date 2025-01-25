package huce.fit.appreadstories.shared_preferences

import android.content.Context

class RateSharedPreferences(context: Context) : BaseSharedPreferences(context) {

    fun getRateId(): Int {
        return sharedPreferences.getInt("rateId", 0)
    }

    fun setRateId(rateId: Int) {
        edit.putInt("rateId", rateId)
    }

    fun getRatePoint(): Int {
        return sharedPreferences.getInt("ratePoint", 0)
    }

    fun setRatePoint(ratePoint: Int) {
        edit.putInt("ratePoint", ratePoint)
    }

    fun getRate(): String? {
        return sharedPreferences.getString("rate", "")
    }

    fun setRate(rate: String) {
        edit.putString("rate", rate)
    }

    fun getSuccess(): Int {
        return sharedPreferences.getInt("success", 0)
    }

    fun setSuccess(success: Int) {
        edit.putInt("success", success)
    }
}
