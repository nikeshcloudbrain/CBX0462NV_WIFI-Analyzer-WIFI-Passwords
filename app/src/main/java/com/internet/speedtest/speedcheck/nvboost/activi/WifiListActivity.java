package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.adapter.MyAdapter;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityWifiListBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

public class WifiListActivity extends AppCompatActivity {
    ActivityWifiListBinding binding;
    private WifiManager wifiManager;
    private RecyclerView rv;
    private Button btnYenile;
    private ArrayList<ScanResult> arrayList = new ArrayList<ScanResult>();
    private MyAdapter adapter;
    private int signalLevel;
    List<ScanResult> mlist = new ArrayList<>();
    private String[] necessaryPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };
    int wifiCount;

    private static final int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWifiListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tool.toolText.setText("List Of Wifi");

    }

    @Override
    protected void onResume() {
        super.onResume();
        ListWifi();
    }

    public void ListWifi() {
        if (ConnectivityReceiver.isConnected()) {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            rv = findViewById(R.id.rvWifiList);
            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(getApplicationContext(), "Wifi is disabled..Making it enabled.", Toast.LENGTH_LONG).show();
                wifiManager.setWifiEnabled(true);
            }
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rv.setLayoutManager(lm);


            if (hasPermissions(necessaryPermissions)) {
                if (checkGPSStatus()) {
                    scanWifi();

                } else {
                    startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                }
            } else {
                ActivityCompat.requestPermissions(this, necessaryPermissions, PERMISSION_ALL);
            }
            btnYenile = findViewById(R.id.btnYenile);
            btnYenile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanWifi();
                }
            });
        } else {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }

    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String p : permissions) {
                if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanWifi();
        } else {
            showMessage("Permissions is not granted");
        }
    }

    public void scanWifi() {
        arrayList.clear();

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        Log.e("wifiList", "scanWifi: " + scanResults);
        showMessage("Scanning...");
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> mScanResults = wifiManager.getScanResults();

                for (int i = 0; i < mScanResults.size(); i++) {
                    arrayList.add(wifiManager.getScanResults().get(i));
                    wifiCount = arrayList.size();
                    binding.totalW.setText(String.valueOf(wifiCount));
//                    adapter.notifyDataSetChanged();
                }
                if (wifiCount == 0) {
                    CTCAppLoadAds.getInstance().showNativeMediaMatch(WifiListActivity.this, binding.frameViewAdsMain);

                }
                adapter = new MyAdapter(WifiListActivity.this, arrayList);
                rv.setAdapter(adapter);
            }
        }
    };

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }*/

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                result = "Very High";
                break;
        }
        return result;
    }


    private Boolean checkGPSStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network");
    }

}