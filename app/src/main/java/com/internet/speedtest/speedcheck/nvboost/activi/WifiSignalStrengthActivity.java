package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;

import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityWifiSignalStrengthBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;


public class WifiSignalStrengthActivity extends AppCompatActivity {
    ActivityWifiSignalStrengthBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWifiSignalStrengthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(this, binding.frameViewAdsMain);



        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tool.toolText.setText("Wifi Signal Details");



    }



    @Override
    protected void onResume() {
        super.onResume();
        SignalData();
    }

    public void SignalData(){
        if(ConnectivityReceiver.isConnected()) {
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(!wm.isWifiEnabled()){
                wm.setWifiEnabled(true);
            }

            new CountDownTimer(60000L, 1000) {
                public void onTick(long millisUntilFinished) {
                    getWifiStrengthPercentage(WifiSignalStrengthActivity.this);

                }
                public void onFinish() {
                    getWifiStrengthPercentage(WifiSignalStrengthActivity.this);

                }
            }.start();
        }else {
            Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }

    public void getWifiStrengthPercentage(Context context)
    {
        try
        {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            int rssi = wifiManager.getConnectionInfo().getRssi();
            int percentage = rssi <= -100 ? 0 : rssi >= -50 ? 100 : (int) (((rssi - (-100)) * 100) / 50.0f);
            binding.pbWiFiSignalStrength.setProgress(percentage);
            binding.txtWiFiStrengths.setText(String.valueOf(percentage)+" % Signal Strength");
            binding.txtWiFiStrength.setText(String.valueOf(percentage)+" % ");
        }
        catch (Exception e)
        {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}

