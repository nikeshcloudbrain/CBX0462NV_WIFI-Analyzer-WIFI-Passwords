package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.animsh.animatedcheckbox.AnimatedCheckBox;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityAutoConnectBinding;
import com.internet.speedtest.speedcheck.nvboost.utils.MyIntentService;

import java.util.Calendar;

public class AutoConnectActivity extends AppCompatActivity {
    ActivityAutoConnectBinding binding;
    boolean Cod1, Cod2, Cod3, Cod4;
    boolean ACod1, ACod2, ACod3;
    WifiManager wifiManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAutoConnectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPref = getSharedPreferences("wifi", Context.MODE_PRIVATE);
        CTCAppLoadAds.getInstance().showNativeMediaMatch(this, binding.frameViewAdsMain);

        Cod1 = sharedPref.getBoolean("Cod1", false);
        Cod2 = sharedPref.getBoolean("Cod2", false);
        Cod3 = sharedPref.getBoolean("Cod3", false);
        Cod4 = sharedPref.getBoolean("Cod4", false);
        ACod1 = sharedPref.getBoolean("ACod1", false);
        ACod2 = sharedPref.getBoolean("ACod2", false);
        ACod3 = sharedPref.getBoolean("ACod3", false);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            binding.switchService.setOn(true);
        } else {
            binding.switchService.setOn(false);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);
        binding.switchService.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                if (isOn) {

                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                    finish();
                }
            }


        });


        if (Cod1) {
            binding.cbScreenOff.setChecked(true);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            registerReceiver(wifiReceiver, filter);

        } else {
            binding.cbScreenOff.setChecked(false);

        }

        if (Cod2) {
            binding.cbAtTimeOff.setChecked(true);

        } else {
            binding.cbAtTimeOff.setChecked(false);

        }

        if (Cod3) {
            binding.cbNotConnectedNetwork.setChecked(true);

        } else {
            binding.cbNotConnectedNetwork.setChecked(false);

        }
        if (Cod4) {
            binding.cbDisconnectCharger.setChecked(true);

        } else {
            binding.cbDisconnectCharger.setChecked(false);

        }

        if (ACod1) {
            binding.cbScreenOn.setChecked(true);

        } else {
            binding.cbScreenOn.setChecked(false);

        }
        if (ACod2) {
            binding.cbAtTimeOn.setChecked(true);

        } else {
            binding.cbAtTimeOn.setChecked(false);

        }
        if (ACod3) {
            binding.cbConnectCharger.setChecked(true);

        } else {
            binding.cbConnectCharger.setChecked(false);

        }

        binding.cbScreenOff.setOnCheckedChangeListener(new AnimatedCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AnimatedCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    Cod1 = true;
                    startService();
                    filter.addAction(Intent.ACTION_SCREEN_OFF);
                    registerReceiver(wifiReceiver, filter);

                } else {
                    Cod1 = false;

                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("Cod1", Cod1);
                editor.apply();
            }


        });


        binding.cbAtTimeOff.setOnCheckedChangeListener(new AnimatedCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AnimatedCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    Cod2 = true;
                    startService();

                    filter.addAction(Intent.ACTION_TIME_CHANGED);
                    registerReceiver(wifiReceiver, filter);

                } else {
                    Cod2 = false;

                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("Cod2", Cod2);
                editor.apply();
            }
        });

        binding.cbNotConnectedNetwork.setOnCheckedChangeListener(new AnimatedCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AnimatedCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    Cod3 = true;
                    startService();

                    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                    registerReceiver(wifiReceiver, filter);

                } else {
                    Cod3 = false;

                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("Cod3", Cod3);
                editor.apply();
            }
        });

        binding.cbDisconnectCharger.setOnCheckedChangeListener(new AnimatedCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AnimatedCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    Cod4 = true;
                    startService();

                    filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
                    registerReceiver(wifiReceiver, filter);

                } else {
                    Cod4 = false;

                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("Cod4", Cod4);
                editor.apply();
            }
        });

        binding.cbScreenOn.setOnCheckedChangeListener(new AnimatedCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AnimatedCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    ACod1 = true;

                    startService();

                    filter.addAction(Intent.ACTION_SCREEN_ON);
                    registerReceiver(wifiReceiver, filter);

                } else {
                    ACod1 = false;

                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("ACod1", ACod1);
                editor.apply();
            }
        });
        binding.cbAtTimeOn.setOnCheckedChangeListener(new AnimatedCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AnimatedCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    ACod2 = true;
                    startService();

                    filter.addAction(Intent.ACTION_TIME_CHANGED);
                    registerReceiver(wifiReceiver, filter);

                } else {
                    ACod2 = false;

                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("ACod2", ACod2);
                editor.apply();
            }
        });

        binding.cbConnectCharger.setOnCheckedChangeListener(new AnimatedCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AnimatedCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    ACod3 = true;
                    startService();

                    filter.addAction(Intent.ACTION_POWER_CONNECTED);
                    registerReceiver(wifiReceiver, filter);

                } else {
                    ACod3 = false;

                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("ACod3", ACod3);
                editor.apply();
            }
        });

        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.tool.toolText.setText("Wifi Connection");

    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info != null && info.isConnected()) {
                // WiFi is connected
                binding.switchService.setOn(true);
            } else {
                binding.switchService.setOn(false);


            }
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
            } else if (Cod2) {
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
            } else if (Cod3) {
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connMgr.getActiveNetworkInfo();
                if (info == null || !info.isConnected()) {
                    // Not connected to any network, turn off WiFi


                    startActivity(i);

                }
            } else if (Cod4) {
                if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                    // Disconnected from charger, turn off WiFi


                    startActivity(i);

                }
            } else if (ACod1) {
                Log.e("TAG", "onReceive1: " + ACod1);
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {


                    startActivity(i);

                }
            } else if (ACod2) {
                Log.e("TAG", "onReceive2: " + ACod2);

                if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
                    Calendar currentTime = Calendar.getInstance();
                    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = currentTime.get(Calendar.MINUTE);

                    // Check if it's 23:00 or later
                    if (hour == 6 && minute == 00) {

                        startActivity(i);

                    }

                }
            } else if (ACod3) {
                Log.e("TAG", "onReceive3: " + ACod3);

                if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                    Log.e("TAG", "onReceive3: " + ACod3);

                    startActivity(i);

                }
            }

        }
    };


    public void startService() {
        Intent serviceIntent = new Intent(this, MyIntentService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }



    @Override
    public void onBackPressed() {

        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }
}