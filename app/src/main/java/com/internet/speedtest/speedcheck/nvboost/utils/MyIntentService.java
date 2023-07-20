package com.internet.speedtest.speedcheck.nvboost.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.internet.speedtest.speedcheck.nvboost.R;

import java.util.Calendar;


public class MyIntentService extends Service {


    boolean Cod1, Cod2, Cod3, Cod4, ACod1, ACod2, ACod3;


    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPref = getSharedPreferences("wifi", MODE_PRIVATE);

        Cod1 = sharedPref.getBoolean("Cod1", false);
        Log.e("TAG", "onStartCommand: " + Cod1);
        Cod2 = sharedPref.getBoolean("Cod2", false);
        Cod3 = sharedPref.getBoolean("Cod3", false);
        Cod4 = sharedPref.getBoolean("Cod4", false);
        ACod1 = sharedPref.getBoolean("ACod1", false);
        ACod2 = sharedPref.getBoolean("ACod2", false);
        ACod3 = sharedPref.getBoolean("ACod3", false);
        startForeground(1, createNotification());

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Notification createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chan = new NotificationChannel(
                    "MyChannelId",
                    "My Foreground Service",
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);


        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this, "MyChannelId");
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Wi-Fi Speed Test")
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setChannelId("MyChannelId")
                .build();

        startForeground(1, notification);

        return notificationBuilder.build();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        if (Cod1) {
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(receiver, filter);

        } else if (Cod2) {
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            registerReceiver(receiver, filter);

        } else if (Cod3) {

            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            registerReceiver(receiver, filter);
        } else if (Cod4) {

            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            registerReceiver(receiver, filter);
        } else if (ACod1) {
            filter.addAction(Intent.ACTION_SCREEN_ON); //further more
            registerReceiver(receiver, filter);

        } else if (ACod2) {
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            registerReceiver(receiver, filter);

        } else if (ACod3) {
            filter.addAction(Intent.ACTION_POWER_CONNECTED); //further more
            registerReceiver(receiver, filter);

        }


    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
            if (Cod1) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    // Screen is turned off, start a timer to turn off WiFi after 10 minutes
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Turn off WiFi
                            startActivity(i);
                        }
                    }, 10 * 60 * 1000);
                }
            }
            if (Cod2) {
                if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
                    // Get the current time
                    Calendar currentTime = Calendar.getInstance();
                    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = currentTime.get(Calendar.MINUTE);

                    // Check if it's 23:00 or later
                    if (hour >= 23 && minute >= 00) {
                        startActivity(i);
                    }
                }


            }
            if (Cod3) {

                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connMgr.getActiveNetworkInfo();
                if (info == null || !info.isConnected()) {
                    // Not connected to any network, turn off WiFi

                    startActivity(i);

                }
            }
            if (Cod4) {


                if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                    // Disconnected from charger, turn off WiFi
                    startActivity(i);

                }


            }
            if (ACod1) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

                    Log.e("go", "onReceive: screen on");
                    startActivity(i);

                }
            }


            if (ACod2) {
                if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
                    Calendar currentTime = Calendar.getInstance();
                    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = currentTime.get(Calendar.MINUTE);

                    // Check if it's 23:00 or later
                    if (hour == 6 && minute == 00) {
                        startActivity(i);

                    }

                }


            }
            if (ACod3) {
                if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                    startActivity(i);

                }
            }


        }
    };


}