package com.internet.speedtest.speedcheck.nvboost;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internet.speedtest.speedcheck.nvboost.activi.SpeedGraphActivity;
import com.hsalf.smileyrating.SmileyRating;
import com.internet.speedtest.speedcheck.nvboost.activi.StartActivity;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.FragmentMoreBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;

import org.jsoup.Jsoup;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class MoreFragment extends Fragment {

    FragmentMoreBinding binding;
    public MoreFragment() {
        // Required empty public constructor
    }

    String ip;

    DhcpInfo d;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentMoreBinding.inflate(getLayoutInflater());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(getActivity(), binding.frameViewAdsMain);
        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        WifiDeta();
    }

    public void WifiDeta(){
        if(ConnectivityReceiver.isConnected()) {
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            WifiManager wm = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            WifiInfo info = wm.getConnectionInfo();
            d=wm.getDhcpInfo();

            binding.localIpAddress.setText(ip);
            binding.macAdd.setText(info.getBSSID());
            binding.gatWay.setText(intToIp(d.gateway));
            binding.subMsk.setText(msk(ip));
            binding.dnsA1.setText(intToIp(d.dns1));
            binding.dnsA2.setText(intToIp(d.dns2));
            binding.broadCst.setText(msk(ip));
            binding.frq.setText(String.valueOf(info.getFrequency())+"MHz");
            binding.cha.setText(String.valueOf(convertFrequencyToChannel(info.getFrequency())));
            binding.ServerAdd.setText(intToIp(d.serverAddress));
            if (wm.is5GHzBandSupported()){
                binding.Is5gh.setText("Yes");

            }else{
                binding.Is5gh.setText("No");

            }
            if(wm.isWifiEnabled()){
                binding.wifiEnable.setText("Yes");

            }else {
                binding.wifiEnable.setText("No");

            }
            if(wm.isP2pSupported()){
                binding.P2p.setText("Yes");

            }else{
                binding.P2p.setText("No");

            }
            if(wm.isTdlsSupported()){
                binding.Tdls.setText("Yes");

            }else{
                binding.Tdls.setText("No");

            }

            int minutes = d.leaseDuration / 60;
            binding.LeasTime.setText(String.valueOf(minutes)+"Min");

            mStartRX = TrafficStats.getTotalRxBytes();
            mStartTX = TrafficStats.getTotalTxBytes();

            if (mStartRX == TrafficStats.UNSUPPORTED
                    || mStartTX == TrafficStats.UNSUPPORTED) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Uh Oh!");
                alert.setMessage("Your device does not support traffic stat monitoring.");
                alert.show();

            } else {
                mHandler.postDelayed(mRunnable, 1000);

            }


            binding.rx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {

                        startActivity(new Intent(getActivity(), SpeedGraphActivity.class));
                    });
                }
            });

            binding.tx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {

                        startActivity(new Intent(getActivity(), SpeedGraphActivity.class));
                    });

                }
            });

            new CountDownTimer(60000L, 1000) {
                public void onTick(long millisUntilFinished) {
                    getWifiStrengthPercentage(getActivity());

                }
                public void onFinish() {
                    getWifiStrengthPercentage(getActivity());

                }
            }.start();
        }else{
            Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void getWifiStrengthPercentage(Context context)
    {
        try
        {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();

            int rssi = wifiManager.getConnectionInfo().getRssi();
            int percentage = rssi <= -100 ? 0 : rssi >= -50 ? 100 : (int) (((rssi - (-100)) * 100) / 50.0f);
            binding.signalPercentage.setText(String.valueOf(percentage)+" % ");
            binding.signalStrength.setText(String.valueOf(rssi));
binding.linkValue.setText(String.valueOf(info.getLinkSpeed()));


            int smileyRating = 1; // Default to the lowest rating
            if (rssi > -50) {
                smileyRating = 5; // Highest rating
            } else if (rssi > -60) {
                smileyRating = 4;
            } else if (rssi > -70) {
                smileyRating = 3;
            } else if (rssi > -80) {
                smileyRating = 2;
            }

            binding.smileRating.setRating(smileyRating);

        }
        catch (Exception e)
        {
        }
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
                binding.publicIp.setText(ip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            long resetdownload=TrafficStats.getTotalRxBytes();

            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;

            binding.rx.setText(Long.toString(rxBytes)+" bytes");

            if (rxBytes >= 1024) {

                long rxKb = rxBytes / 1024;

                binding.rx.setText(Long.toString(rxKb)+" KBs");

                if (rxKb >= 1024) {

                    long rxMB = rxKb / 1024;


                    binding.rx.setText(Long.toString(rxMB)+" MBs");

                    if (rxMB >= 1024) {

                        long rxGB = rxMB / 1024;
                        binding.rx.setText(Long.toString(rxGB)+" GBs");


                    }
                }
            }

            mStartRX=resetdownload;

            long resetupload=TrafficStats.getTotalTxBytes();

            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;


            binding.tx.setText(Long.toString(txBytes)+" bytes");

            if (txBytes >= 1024) {

                long txKb = txBytes / 1024;


                binding.tx.setText(Long.toString(txKb)+" KBs");

                if (txKb >= 1024) {

                    long txMB = txKb / 1024;


                    binding.tx.setText(Long.toString(txMB)+" MBs");

                    if (txMB >= 1024) {

                        long txGB = txMB / 1024;


                        binding.tx.setText(Long.toString(txGB)+" GBs");

                    }
                }
            }

            mStartTX=resetupload;

            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    public String intToIp(int i) {

        return  ( i & 0xFF)+ "." +((i >> 8 ) & 0xFF) + "." +((i >> 16 ) & 0xFF)+ "." +((i >> 24 ) & 0xFF ) ;
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

    public static int convertFrequencyToChannel(int freq) {
        if (freq >= 2412 && freq <= 2484) {
            return (freq - 2412) / 5 + 1;
        } else if (freq >= 5170 && freq <= 5825) {
            return (freq - 5170) / 5 + 34;
        } else {
            return -1;
        }
    }
}