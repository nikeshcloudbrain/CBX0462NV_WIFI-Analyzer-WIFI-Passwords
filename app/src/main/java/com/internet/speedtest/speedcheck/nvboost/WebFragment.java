package com.internet.speedtest.speedcheck.nvboost;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.FragmentWebBinding;
import com.internet.speedtest.speedcheck.nvboost.ipget.IPINFO;
import com.internet.speedtest.speedcheck.nvboost.ipget.RetroClient;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;
import com.internet.speedtest.speedcheck.nvboost.utils.GetIPAddressFromHostname;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;


public class WebFragment extends Fragment {

    String ip;
    JsoupAsyncTask jsoupAsyncTask;
    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");

    public WebFragment() {
        // Required empty public constructor
    }


    FragmentWebBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWebBinding.inflate(inflater);
        CTCAppLoadAds.getInstance().showNativeMediaMatch(getActivity(), binding.frameViewAdsMain);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        get_data();
    }

    public void get_data(){
        if(ConnectivityReceiver.isConnected()) {
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            this.jsoupAsyncTask = jsoupAsyncTask;
            jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);

            binding.btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(binding.getIp.getText().toString())) {


                        Toast.makeText(getActivity(), "Please Enter Ip Address", Toast.LENGTH_SHORT).show();

                        return;


                    } else {
                        Matcher matcher = IP_ADDRESS.matcher(binding.getIp.getText().toString());
                        if (matcher.matches()) {
                            ip = binding.getIp.getText().toString();
                        }else {
                            Thread gfgThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try  {
                                        //   GetIPAddressFromHostname.main("boostapps.space");
                                        ip = GetIPAddressFromHostname.main( binding.getIp.getText().toString());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            gfgThread.start();
                        }



                    }

                    getIpDetails(ip);
                }
            });


            binding.ownIp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                    jsoupAsyncTask = jsoupAsyncTask;
                    jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);

                }
            });
        }else {
            Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);

        }
    }

    public void getIpDetails(String mIp) {
        RetroClient.getInstance().getApi2().getIPINFO(mIp)
                .enqueue(new Callback<IPINFO>() {

                    @Override
                    public void onResponse(Call<IPINFO> call, retrofit2.Response<IPINFO> response) {

                        if (response.body() != null) {
                            Log.e("ipD", "onResponse: " + response.body().isMobile());

                            binding.iCountry.setText(response.body().getCountry());
                            binding.iCountryCode.setText(response.body().getCountryCode());
                            binding.iRegion.setText(response.body().getRegion());
                            binding.iRegionName.setText(response.body().getRegionName());
                            binding.iCity.setText(response.body().getCity());
                            binding.iZipCode.setText(response.body().getZip());
                            binding.ilat.setText(response.body().getLat());
                            binding.ilon.setText(response.body().getLon());
                            binding.iTimeZ.setText(response.body().getTimezone());
                            binding.iIsp.setText(response.body().getIsp());
                            binding.iOrg.setText(response.body().getOrg());
                            binding.iAs.setText(response.body().getAs());
                            binding.iAsName.setText(response.body().getAs());
                            if(response.body().isMobile()){
                                binding.iConnctionType.setText("Cellular");

                            }else{
                                binding.iConnctionType.setText("Wifi");

                            }
                            if(response.body().isHosting()){
                                binding.iIsHost.setText("Yes");

                            }else{
                                binding.iIsHost.setText("No");

                            }
                            if(response.body().isProxy()){
                                binding.iPro.setText("Yes");

                            }else{
                                binding.iPro.setText("No");

                            }
                              binding.iPubIp.setText(ip);


                        }

                    }

                    @Override
                    public void onFailure(Call<IPINFO> call
                            , Throwable t) {
                        Log.e("ipD", "onResponse: ", t);

                    }
                });
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
                getIpDetails(this.ip);
                binding.getIp.setText(ip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}