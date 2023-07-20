package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.webkit.WebViewClient;

import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityRouterAdminBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;


public class RouterAdminActivity extends AppCompatActivity {
ActivityRouterAdminBinding binding;
    DhcpInfo d;
    String AdminLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRouterAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CTCAppLoadAds.getInstance().showNativeBottomDynamic(this, binding.frameViewAds);

        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        onBackPressed();
    }
});
binding.tool.toolText.setText("Router Admin");



    }

    @Override
    protected void onResume() {
        super.onResume();
    AdminData();
    }

    public void AdminData(){
        if(ConnectivityReceiver.isConnected()){
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);



            d=wm.getDhcpInfo();

            AdminLink=String.valueOf(Formatter.formatIpAddress(d.gateway));
            binding.webview.loadUrl("http://" + AdminLink);
            binding.webview.getSettings().setJavaScriptEnabled(true);
            binding.webview.setWebViewClient(new WebViewClient());
        }else {

            Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }
}