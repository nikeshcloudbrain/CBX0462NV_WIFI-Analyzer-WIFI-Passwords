package com.internet.speedtest.speedcheck.nvboost.activi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.adapter.IntroViewPagerAdapter;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityIntroBinding;
import com.internet.speedtest.speedcheck.nvboost.model.NextScreen;
import com.internet.speedtest.speedcheck.nvboost.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;

    IntroViewPagerAdapter introViewPagerAdapter;
    private ViewPager screenPager;
    int next;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(this, binding.frameViewAdsMain);

        final List<NextScreen> mList = new ArrayList<>();
        mList.add(new NextScreen("WIFI", " Scanner", "Wifi scanner app is used to Optimize and Analyze WiFi Networks by testing WiFi Signal, Crowded signal, Scanning signal strength and Channel rating.", R.drawable.next1_v));
        mList.add(new NextScreen("Network", " Availability", "Network Analyzer and Wifi Scanner supports you to Optimize your WiFi network by examining nearby WiFi networks, calculating their signal strength as well as identifying crowded channels. ", R.drawable.next2_v));

        screenPager = findViewById(R.id.idViewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);
        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        screenPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                Log.e("vv", "onPageSelected: " + arg0);
                if (arg0 == 1) {
                    binding.btnNext1.setTextColor(ContextCompat.getColor(IntroActivity.this, R.color.colorMain));

                    binding.tabLayout.setImageResource(R.drawable.shape2);

                    next = arg0;

                } else if (arg0 == 0) {
                    next = 0;
                    binding.btnNext1.setTextColor(ContextCompat.getColor(IntroActivity.this, R.color.colorMainLight));
                    binding.tabLayout.setImageResource(R.drawable.shape);

                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int num) {


            }
        });


        binding.btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (next == 1) {
                    sharedPreferences = getSharedPreferences("intro", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    myEdit.putBoolean("iCheck", true);
                    myEdit.commit();
                    CTCAppLoadAds.getInstance().showInterstitial(IntroActivity.this, () -> {

                        startActivity(new Intent(IntroActivity.this, StartActivity.class));

                    });
                } else {

                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        Constant.showRateDialog(IntroActivity.this, false);

    }
}