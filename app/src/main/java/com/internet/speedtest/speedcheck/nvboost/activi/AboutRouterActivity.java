package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;


import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityAboutRouterBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import kotlin.UByte;

public class AboutRouterActivity extends AppCompatActivity {
    ActivityAboutRouterBinding binding;
    DhcpInfo d;
    String ip;
    String broadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutRouterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(this, binding.frameViewAdsMain);

        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.tool.toolText.setText("Router Details");


    }

    @Override
    protected void onResume() {
        super.onResume();
        RouterData();
    }

    public void RouterData(){
        if (ConnectivityReceiver.isConnected()) {
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            WifiInfo info = wm.getConnectionInfo();
            d = wm.getDhcpInfo();

            binding.txtDeviceInternalIP.setText("Internal IP " + ip);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            binding.txtDeviceMACAddress.setText("Device Mac Address " + info.getMacAddress());
            binding.txtWiFiGateway.setText(intToIp(d.gateway));
            binding.txtWiFiSubnetMask.setText(intToIp(d.netmask));
            binding.txtWiFiMACAddress.setText(info.getBSSID());
            binding.txtWiFiDNSAddress1.setText(intToIp(d.dns1));
            binding.txtWiFiDNSAddress2.setText(intToIp(d.dns2));
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        broadCast = getBroadcast();
                        binding.txtWiFiBroadcastAddress.setText(broadCast);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


            binding.txtWiFiFrequency.setText(info.getFrequency() + "MHz");
            binding.txtWiFiChannel.setText(fre(String.valueOf(info.getFrequency())));


            d = wm.getDhcpInfo();
        } else {

            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);

        }
    }

    public String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);


    }


    public static String getBroadcast() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    return interfaceAddress.getBroadcast().toString().substring(1);
                }
            }
        }
        return "/255.255.255.255";
    }

    public static String fre(String str) {
        switch (str.hashCode()) {
            case 1541091:
                return str.equals("2412") ? "ch 1 - 2.4ghz" : str;
            case 1541096:
                return str.equals("2417") ? "ch 2 - 2.4ghz" : str;
            case 1541122:
                return str.equals("2422") ? "ch 3 - 2.4ghz" : str;
            case 1541127:
                return str.equals("2427") ? "ch 4 - 2.4ghz" : str;
            case 1541153:
                return str.equals("2432") ? "ch 5 - 2.4ghz" : str;
            case 1541158:
                return str.equals("2437") ? "ch 6 - 2.4ghz" : str;
            case 1541184:
                return str.equals("2442") ? "ch 7 - 2.4ghz" : str;
            case 1541189:
                return str.equals("2447") ? "ch 8 - 2.4ghz" : str;
            case 1541215:
                return str.equals("2452") ? "ch 9 - 2.4ghz" : str;
            case 1541220:
                return str.equals("2457") ? "ch 10 - 2.4ghz" : str;
            case 1541246:
                return str.equals("2462") ? "ch 11 - 2.4ghz" : str;
            case 1541251:
                return str.equals("2467") ? "ch 12 - 2.4ghz" : str;
            case 1541277:
                return str.equals("2472") ? "ch 13 - 2.4ghz" : str;
            case 1541310:
                return str.equals("2484") ? "ch 14 - 2.4ghz" : str;
            case 1605481:
                return str.equals("4915") ? "ch 183 - 5.0ghz" : str;
            case 1605507:
                return str.equals("4920") ? "ch 184 - 5.0ghz" : str;
            case 1605512:
                return str.equals("4925") ? "ch 185 - 5.0ghz" : str;
            case 1605543:
                return str.equals("4935") ? "ch 187 - 5.0ghz" : str;
            case 1605569:
                return str.equals("4940") ? "ch 188 - 5.0ghz" : str;
            case 1605574:
                return str.equals("4945") ? "ch 189 - 5.0ghz" : str;
            case 1605631:
                return str.equals("4960") ? "ch 192 - 5.0ghz" : str;
            case 1605693:
                return str.equals("4980") ? "ch 196 - 5.0ghz" : str;
            case 1626685:
                return str.equals("5035") ? "ch 7 - 5.0ghz" : str;
            case 1626711:
                return str.equals("5040") ? "ch 8 - 5.0ghz" : str;
            case 1626716:
                return str.equals("5045") ? "ch 9 - 5.0ghz" : str;
            case 1626747:
                return str.equals("5055") ? "ch 11 - 5.0ghz" : str;
            case 1626773:
                return str.equals("5060") ? "ch 12 - 5.0ghz" : str;
            case 1626835:
                return str.equals("5080") ? "ch 16 - 5.0ghz" : str;
            case 1627765:
                return str.equals("5170") ? "ch 34 - 5.0ghz" : str;
            case 1627796:
                return str.equals("5180") ? "ch 36 - 5.0ghz" : str;
            case 1627827:
                return str.equals("5190") ? "ch 38 - 5.0ghz" : str;
            case 1628509:
                return str.equals("5200") ? "ch 40 - 5.0ghz" : str;
            case 1628540:
                return str.equals("5210") ? "ch 42 - 5.0ghz" : str;
            case 1628571:
                return str.equals("5220") ? "ch 44 - 5.0ghz" : str;
            case 1628602:
                return str.equals("5230") ? "ch 46 - 5.0ghz" : str;
            case 1628633:
                return str.equals("5240") ? "ch 48 - 5.0ghz" : str;
            case 1628695:
                return str.equals("5260") ? "ch 52 - 5.0ghz" : str;
            case 1628757:
                return str.equals("5280") ? "ch 56 - 5.0ghz" : str;
            case 1629470:
                return str.equals("5300") ? "ch 60 - 5.0ghz" : str;
            case 1629532:
                return str.equals("5320") ? "ch 64 - 5.0ghz" : str;
            case 1631392:
                return str.equals("5500") ? "ch 100 - 5.0ghz" : str;
            case 1631454:
                return str.equals("5520") ? "ch 104 - 5.0ghz" : str;
            case 1631516:
                return str.equals("5540") ? "ch 108 - 5.0ghz" : str;
            case 1631578:
                return str.equals("5560") ? "ch 112 - 5.0ghz" : str;
            case 1631640:
                return str.equals("5580") ? "ch 116 - 5.0ghz" : str;
            case 1632353:
                return str.equals("5600") ? "ch 120 - 5.0ghz" : str;
            case 1632415:
                return str.equals("5620") ? "ch 124 - 5.0ghz" : str;
            case 1632477:
                return str.equals("5640") ? "ch 128 - 5.0ghz" : str;
            case 1632539:
                return str.equals("5660") ? "ch 132 - 5.0ghz" : str;
            case 1632601:
                return str.equals("5680") ? "ch 136 - 5.0ghz" : str;
            case 1633314:
                return str.equals("5700") ? "ch 140 - 5.0ghz" : str;
            case 1633376:
                return str.equals("5720") ? "ch 144 - 5.0ghz" : str;
            case 1633443:
                return str.equals("5745") ? "ch 149 - 5.0ghz" : str;
            case 1633505:
                return str.equals("5765") ? "ch 153 - 5.0ghz" : str;
            case 1633567:
                return str.equals("5785") ? "ch 157 - 5.0ghz" : str;
            case 1634280:
                return str.equals("5805") ? "ch 161 - 5.0ghz" : str;
            case 1634342:
                return str.equals("5825") ? "ch 165 - 5.0ghz" : str;
            default:
                return "No";
        }
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

    @Override
    public void onBackPressed() {
        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }
}