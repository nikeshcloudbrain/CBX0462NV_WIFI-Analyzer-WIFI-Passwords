package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.internet.speedtest.speedcheck.nvboost.BaseApplication;
import com.internet.speedtest.speedcheck.nvboost.HomeFragment;
import com.internet.speedtest.speedcheck.nvboost.MoreFragment;
import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.WebFragment;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityHomeBinding;
import com.internet.speedtest.speedcheck.nvboost.utils.Constant;

public class MainActivity extends AppCompatActivity {
ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    requestPermissions();

        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment, "");
        fragmentTransaction.commit();

        binding.navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_main:
                        HomeFragment fragment = new HomeFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, fragment, "");
                        fragmentTransaction.commit();
                        return true;

                    case R.id.navigation_home:
                        WebFragment fragment1 = new WebFragment();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.nav_host_fragment, fragment1);
                        fragmentTransaction1.commit();
                        return true;

                    case R.id.navigation_more:
                        MoreFragment fragment2 = new MoreFragment();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.nav_host_fragment, fragment2, "");
                        fragmentTransaction2.commit();
                        return true;
                }
                return false;
            }
        });

        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (BaseApplication.getAdModel().getExtraScreen().equalsIgnoreCase("Yes")) {
            CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);

        }else{
            Constant.showRateDialog(MainActivity.this, false);

        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_WIFI_STATE)
                    != PackageManager.PERMISSION_GRANTED  ) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE


                }, 0);
            }
        }






    }


}