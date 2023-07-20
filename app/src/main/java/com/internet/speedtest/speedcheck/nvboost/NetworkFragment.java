package com.internet.speedtest.speedcheck.nvboost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internet.speedtest.speedcheck.nvboost.activi.PingToolsActivity;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.FragmentNetworkBinding;

public class NetworkFragment extends Fragment {


    public NetworkFragment() {
        // Required empty public constructor
    }


   FragmentNetworkBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentNetworkBinding.inflate(inflater);
        // Inflate the layout for this fragment

        binding.pinTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {
                    startActivity(new Intent(getActivity(), PingToolsActivity.class));

                });
            }
        });

        return binding.getRoot();
    }
}