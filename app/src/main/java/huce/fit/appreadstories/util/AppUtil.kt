package huce.fit.appreadstories.util

import android.content.Context
import android.widget.Toast

object AppUtil {

    fun setToast(context: Context, content: String){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }
}