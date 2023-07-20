package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.internet.speedtest.speedcheck.nvboost.adapter.WhoAdapter;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivitySameNetworkDeviceBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;
import com.internet.speedtest.speedcheck.nvboost.utils.LocalDeviceInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SameNetworkDeviceActivity extends AppCompatActivity {
    ActivitySameNetworkDeviceBinding binding;
    public String s_dns1;
    public String s_dns2;
    public String s_gateway;
    public String s_ipAddress;
    public String s_leaseDuration;
    public String s_netmask;
    public String s_serverAddress;
    String DeviceIp, DeviceMac;
    DhcpInfo d;
    WifiManager wifii;
    ArrayList<LocalDeviceInfo> arrayList = new ArrayList<>();
    private WhoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySameNetworkDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(this, binding.frameViewAdsMain);


        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        AllData();
    }

    public void AllData(){
        wifii = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(ConnectivityReceiver.isConnected()){
            d = wifii.getDhcpInfo();

            s_dns1 = "DNS 1: " + String.valueOf(d.dns1);
            s_dns2 = "DNS 2: " + String.valueOf(d.dns2);
            s_gateway = "Default Gateway: " + String.valueOf(d.gateway);
            s_ipAddress = "IP Address: " + String.valueOf(d.ipAddress);
            s_leaseDuration = "Lease Time: " + String.valueOf(d.leaseDuration);
            s_netmask = "Subnet Mask: " + String.valueOf(d.netmask);
            s_serverAddress = "Server IP: " + String.valueOf(d.serverAddress);

            RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvDeviceList.setLayoutManager(lm);

            adapter = new WhoAdapter(SameNetworkDeviceActivity.this, arrayList);
            binding.rvDeviceList.setAdapter(adapter);
            binding.scanhost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.updateDate(arrayList);
                }
            });
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        String connections = "";
                        InetAddress host;
                        try {
                            host = InetAddress.getByName(intToIp(d.dns1));
                            byte[] ip = host.getAddress();

                            for (int i = 1; i <= 254; i++) {
                                ip[3] = (byte) i;
                                InetAddress address = InetAddress.getByAddress(ip);
                                if (address.isReachable(100)) {
                                    System.out.println(address + " machine is turned on and can be pinged");
                                    connections = address + "\n";
                                    connections = connections.trim();
                                    DeviceIp = getRequiredText(String.valueOf(address.getHostName()));
                                    DeviceMac = getMacAddressForIp(DeviceIp);
                                    arrayList.add(new LocalDeviceInfo(DeviceIp, DeviceMac));
                                    Log.e("TAG", "run: " + DeviceIp + " " + DeviceMac);
                                    runOnUiThread(() -> {
                                        adapter.updateDate(arrayList);

                                    });
                                } else if (!address.getHostAddress().equals(address.getHostName())) {
                                    System.out.println(address + " machine is known in a DNS lookup");
                                }

                            }


                        } catch (UnknownHostException e1) {
                            e1.printStackTrace();
                            Log.e("TAG", "run: ", e1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("TAG", "run: ", e);

                        }
                        System.out.println(connections);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }else {
            Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);

        }

    }

    @Override
    public void onBackPressed() {
        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }

    String getRequiredText(String text) {
        int delimiterIndex = text.indexOf('/');
        return text.substring(delimiterIndex + 1);
    }

    public String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }

    /*public static String getMacFromArpCache(String ip) {

    }*/

    public String getMacAddressForIp(final String ipAddress) {
        try (BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(ipAddress)) {
                    final int macStartIndex = line.indexOf(":") - 2;
                    final int macEndPos = macStartIndex + 17;
                    if (macStartIndex >= 0 && macEndPos < line.length()) {
                        return line.substring(macStartIndex, macEndPos);
                    } else {
                        Log.w("MyClass", "Found ip address line, but mac address was invalid.");
                    }
                }
            }
        } catch(Exception e){
            Log.e("MyClass", "Exception reading the arp table.", e);
        }
        return null;
    }


}