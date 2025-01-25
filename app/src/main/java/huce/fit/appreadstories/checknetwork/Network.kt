package huce.fit.appreadstories.checknetwork

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

@SuppressLint("ObsoleteSdkInt")
fun isConnecting(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val cap = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            when {
                cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }

        else -> {
            val activeNetwork = connectivityManager.activeNetworkInfo ?: return false
            when (activeNetwork.type) {
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_VPN -> true
                else -> false
            }
        }
    }
}