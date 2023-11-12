package huce.fit.appreadstories.checknetwork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckNetwork {
    private final Context context;

    public CheckNetwork(Context context) {
        this.context = context;
    }

    public boolean isNetwork() {
        boolean isWifiConn = false;
        boolean isMobileConn = false;

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        Log.e("Wifi connected: ", String.valueOf(isWifiConn));
        Log.e("Mobile connected: ", String.valueOf(isMobileConn));
        return isWifiConn || isMobileConn;
    }
}
