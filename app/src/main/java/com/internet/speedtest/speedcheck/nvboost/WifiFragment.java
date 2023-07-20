package com.internet.speedtest.speedcheck.nvboost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internet.speedtest.speedcheck.nvboost.activi.CurrentWifiDetailsActivity;
import com.internet.speedtest.speedcheck.nvboost.activi.StartActivity;
import com.internet.speedtest.speedcheck.nvboost.activi.WifiListActivity;
import com.internet.speedtest.speedcheck.nvboost.activi.WifiSignalStrengthActivity;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.FragmentWifiBinding;


public class WifiFragment extends Fragment {



    public WifiFragment() {
    }

FragmentWifiBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentWifiBinding.inflate(inflater);
        binding.wifiList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {
                    startActivity(new Intent(getActivity(), WifiListActivity.class));

                });

            }
        });

        binding.aboutWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {
                    startActivity(new Intent(getActivity(), CurrentWifiDetailsActivity.class));

                });
            }
        });
        binding.wifiStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {
                    startActivity(new Intent(getActivity(), WifiSignalStrengthActivity.class));

                });
            }
        });
        return binding.getRoot();
    }
}