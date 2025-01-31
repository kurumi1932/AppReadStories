package huce.fit.appreadstories.account

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import huce.fit.appreadstories.shared_preferences.AccountSharedPreferences
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

open class BaseAccountImpl(val context: Context) : BaseAccountPresenter {

    companion object{
        private const val TAG = "BaseAccountImpl"
    }

    private var account = AccountSharedPreferences(context)
    val calendar: Calendar = Calendar.getInstance()

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun getAccount(): AccountSharedPreferences {
        account.getSharedPreferences("Account", Context.MODE_PRIVATE)
        return account
    }

    override fun setAccount(): AccountSharedPreferences {
        account.setSharedPreferences("Account", Context.MODE_PRIVATE)
        return account
    }

    override fun age(birthday: String): Int {
        val endDate = simpleDateFormat.format(Date())
        Log.e(TAG, "NHT birthday: $birthday")
        val date1 = simpleDateFormat.parse(birthday)
        val date2= simpleDateFormat.parse(endDate)
        val getDiff: Long
        return if (date1 != null && date2 != null) {
            getDiff = date2.time - date1.time
            Log.e(TAG, "NHT getDiff: $getDiff")
            val getDaysDiff = getDiff / (24 * 60 * 60 * 1000) //24h*60p*60s*1000
            Log.e(TAG, "NHT getDaysDiff: $getDaysDiff")
            Log.e(TAG, "NHT age: " + getDaysDiff.toInt() / 365)
            getDaysDiff.toInt() / 365//return
        }else{
            0
        }
    }

    fun checkDate(dateStr: String): Boolean {
        val dateArr = dateStr.split("-".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray() //tách từng giá trị vào mảng khi gặp -
        if (dateArr[0].length > 4) return false
        val year = dateArr[0].toInt()
        return if (year in 1901..3000) //năm ở vị trí 0 phải nhỏ hơn 3000 và lớn hơn 1900
            isValid(dateStr) else false
    }

    private fun isValid(dateStr: String): Boolean {
        @SuppressLint("SimpleDateFormat") val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        sdf.isLenient = false
        try {
            sdf.parse(dateStr) //convert date nếu sai thì chạy catch
        } catch (e: ParseException) {
            return false
        }
        return true
    }
}
