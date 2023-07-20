package com.internet.speedtest.speedcheck.nvboost.utils;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIPAddressFromHostname {

    public static String main(String args) {
        String ipAddr = "";

        try {

            InetAddress inetAddr = InetAddress.getByName(args);

            byte[] addr = inetAddr.getAddress();

            // Convert to dot representation
            for (int i = 0; i < addr.length; i++) {
                if (i > 0) {
                    ipAddr += ".";
                }
                ipAddr += addr[i] & 0xFF;
            }

            Log.e("ip","IP Address: " + ipAddr);

        }
        catch (UnknownHostException e) {
            Log.e("ip","Host not found: " + e.getMessage());

        }

        return  ipAddr;
    }

}

