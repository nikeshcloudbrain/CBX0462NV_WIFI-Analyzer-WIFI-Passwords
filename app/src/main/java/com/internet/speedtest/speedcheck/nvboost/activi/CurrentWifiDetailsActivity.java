package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;


import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityCurrentWifiDetailsBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Iterator;

import kotlin.UByte;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CurrentWifiDetailsActivity extends AppCompatActivity {
ActivityCurrentWifiDetailsBinding binding;
    DhcpInfo d;
    String ip;
    private int signalLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCurrentWifiDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(this, binding.frameViewAdsMain);

        if(checkGPSStatus()){
            FullWifiDetails();
        }else {
            startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        }


        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tool.toolText.setText("Wifi Details");

    }

    private Boolean checkGPSStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullWifiDetails();
    }

    public void FullWifiDetails(){
        if (ConnectivityReceiver.isConnected()) {
            Context context =getApplicationContext();

            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            WifiInfo info = wm.getConnectionInfo();

            binding.txtDeviceInternalIP.setText(ip);
            binding.inIp.setText(ip);

            d = wm.getDhcpInfo();
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);

            binding.txtWiFiGateway.setText(intToIp(d.gateway));
            binding.txtWiFiSubnetMask.setText(msk(ip));
            binding.txtWiFiMACAddress.setText(mac());

            binding.txtWiFiSsid.setText(info.getSSID());
            binding.txtWiFiRssi.setText(String.valueOf(info.getRssi()));
            binding.txtWiFiSpeed.setText(String.valueOf(info.getLinkSpeed()));
            signalLevel = wm.calculateSignalLevel(info.getRssi(), 5);

            binding.txtWiFisignalSrtength.setText(signalStrength(signalLevel));
            binding.txtWiFiChannel.setText(String.valueOf(info.getNetworkId()));
            binding.txtWiFiBssid.setText(info.getBSSID());
            binding.txtWiFiFrequency.setText(String.valueOf(info.getFrequency()));
            binding.txtWiFiDNS.setText(intToIp(d.dns1) + "\n" + intToIp(d.dns2));
        }else{
            Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }
    public String intToIp(int i) {

        return  ( i & 0xFF)+ "." +((i >> 8 ) & 0xFF) + "." +((i >> 16 ) & 0xFF)+ "." +((i >> 24 ) & 0xFF ) ;



    }



    public String mac() {
        try {
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                NetworkInterface networkInterface = (NetworkInterface) it.next();
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b : hardwareAddress) {
                        sb.append(Integer.toHexString(b & UByte.MAX_VALUE) + ":");
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
            return "No Available";
        } catch (Exception e) {
            e.printStackTrace();
            return "No Available";
        }
    }



    public String msk(String ip) {
        String str = null;
        try {
            str = this.ip;
        } catch (Exception e) {
            try {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
        int parseInt = Integer.parseInt(str.split("\\.")[0]);
        return parseInt <= 127 ? "255.0.0.0" : parseInt <= 191 ? "255.255.0.0" : parseInt <= 223 ? "255.255.255.0" : (parseInt <= 239 || parseInt <= 254) ? "255.0.0.0" : "";
    }


    private String signalStrength(int signalLevel) {
        String result = "";
        switch (signalLevel) {
            case 0:
                result = "Very Low";
                break;
            case 1:
                result = "Low";
                break;
            case 2:
                result = "Medium";
                break;
            case 3:
                result = "High";
                break;
            case 4:
                result = "Exellent";
                break;
        }
        return result;
    }


    public class JsoupAsyncTask extends AsyncTask<Void, Void, Boolean> {
        String ip;

        private JsoupAsyncTask() {
            this.ip = "";
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            IpDetailActivity.this.progressbbar.setVisibility(0);
        }

        public Boolean doInBackground(Void... voidArr) {
            try {
                try {
                    this.ip = new OkHttpClient().newCall(new Request.Builder().url("https://api.ipify.org/").build()).execute().body().string().trim();
                    return null;
                } catch (Exception e) {
                    try {
                        this.ip = Jsoup.connect("http://checkip.amazonaws.com/").ignoreContentType(true).get().select("body").text().trim();
                        return null;
                    } catch (IOException e2) {
                        this.ip = "";
                        e2.printStackTrace();
                        return null;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
                return false;
            }
        }

        public void onPostExecute(Boolean bool) {
            try {
                binding.exIp.setText(ip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}