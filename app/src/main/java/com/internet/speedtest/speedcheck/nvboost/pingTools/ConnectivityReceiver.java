package com.internet.speedtest.speedcheck.nvboost.pingTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.internet.speedtest.speedcheck.nvboost.BaseApplication;
import com.internet.speedtest.speedcheck.nvboost.utils.Contant;


/* loaded from: classes2.dex */
public class ConnectivityReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;

    /* loaded from: classes2.dex */
    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean z);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("ContentValues", "onReceive: ");
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            boolean z = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
            String connectivityStatusString = Contant.getConnectivityStatusString(context);
            Log.i("ContentValues", "onReceive: " + connectivityStatusString);
            ConnectivityReceiverListener connectivityReceiverListener2 = connectivityReceiverListener;
            if (connectivityReceiverListener2 != null) {
                connectivityReceiverListener2.onNetworkConnectionChanged(z);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
