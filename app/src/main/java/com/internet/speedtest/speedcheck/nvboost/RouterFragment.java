package com.internet.speedtest.speedcheck.nvboost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internet.speedtest.speedcheck.nvboost.activi.AboutRouterActivity;
import com.internet.speedtest.speedcheck.nvboost.activi.RouterAdminActivity;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.FragmentRouterBinding;


public class RouterFragment extends Fragment {


    public RouterFragment() {
        // Required empty public constructor
    }

    FragmentRouterBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRouterBinding.inflate(inflater);
        binding.aboutRouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {
                    startActivity(new Intent(getActivity(), AboutRouterActivity.class));

                });
            }
        });

        binding.Routeradmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(getActivity(), () -> {
                    startActivity(new Intent(getActivity(), RouterAdminActivity.class));

                });
            }
        });
        return binding.getRoot();
    }
}