package com.internet.speedtest.speedcheck.nvboost.activi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityStartBinding;
import com.internet.speedtest.speedcheck.nvboost.utils.Constant;


public class StartActivity extends AppCompatActivity {

    ActivityStartBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(this, binding.frameViewAdsMain);


        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StartActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + StartActivity.this.getPackageName())));
                } catch (ActivityNotFoundException unused) {
                    Toast.makeText(StartActivity.this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", StartActivity.this.getString(R.string.app_name));
                    intent.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\nhttps://play.google.com/store/apps/details?id=" + StartActivity.this.getPackageName() + "\n\n");
                    StartActivity.this.startActivity(Intent.createChooser(intent, "choose one"));
                } catch (Exception unused) {
                }
            }
        });

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTCAppLoadAds.getInstance().showInterstitial(StartActivity.this, () -> {
                    StartActivity.this.startActivity(new Intent(StartActivity.this, MainActivity.class));
                });
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Constant.showRateDialog(StartActivity.this, false);
    }
}
