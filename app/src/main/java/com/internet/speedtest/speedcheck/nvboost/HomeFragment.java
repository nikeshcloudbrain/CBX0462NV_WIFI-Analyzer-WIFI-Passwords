package com.internet.speedtest.speedcheck.nvboost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internet.speedtest.speedcheck.nvboost.activi.AutoConnectActivity;
import com.internet.speedtest.speedcheck.nvboost.activi.SameNetworkDeviceActivity;
import com.google.android.material.tabs.TabLayoutMediator;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    FragmentAdapter fragmentAdapter;
FragmentHomeBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater);
        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(getActivity(), binding.frameViewAdsMain);

        fragmentAdapter.addFragment(new WifiFragment(), "WiFi");
        fragmentAdapter.addFragment(new RouterFragment(), "Router");
        fragmentAdapter.addFragment(new NetworkFragment(), "Network");

        binding.wPager.setAdapter(fragmentAdapter);
        binding.wPager.setOffscreenPageLimit(2);

        new TabLayoutMediator(binding.tab, binding.wPager, (tab, position) -> tab.setText(fragmentAdapter.StringArrayList.get(position))).attach();

        binding.AutoConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {
                    startActivity(new Intent(getActivity(), AutoConnectActivity.class));

                });
            }
        });

        binding.whoSame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {
                    startActivity(new Intent(getActivity(), SameNetworkDeviceActivity.class));

                });

            }
        });

        return binding.getRoot();
    }
}