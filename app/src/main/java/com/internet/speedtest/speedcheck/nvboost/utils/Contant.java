package com.internet.speedtest.speedcheck.nvboost.utils;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Contant {
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static int TYPE_WIFI = 1;

    public static int getConnectivityStatus(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == 1) {
                return TYPE_WIFI;
            }
            if (activeNetworkInfo.getType() == 0) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }


    public static String getConnectivityStatusString(Context context) {
        int connectivityStatus = getConnectivityStatus(context);
        try {
            if (connectivityStatus == TYPE_WIFI) {
                return "Wifi enabled";
            }
            if (connectivityStatus == TYPE_MOBILE) {
                return "Mobile data enabled";
            }
            if (connectivityStatus != TYPE_NOT_CONNECTED) {
                return null;
            }
            return "Not connected to Internet";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
