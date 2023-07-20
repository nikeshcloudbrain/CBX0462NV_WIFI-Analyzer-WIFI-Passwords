package com.internet.speedtest.speedcheck.nvboost.activi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.internet.speedtest.speedcheck.nvboost.BaseApplication;
import com.internet.speedtest.speedcheck.nvboost.BuildConfig;
import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCModelAd;
import com.internet.speedtest.speedcheck.nvboost.api.RenClient;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivitySplashBinding;
import com.internet.speedtest.speedcheck.nvboost.encrypt.EasyAES;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    SharedPreferences sh;
    boolean InCheck;
    ActivitySplashBinding binding;
    int VERSION = 0;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivitySplashBinding inflate = ActivitySplashBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());

        sh = getSharedPreferences("intro", Context.MODE_PRIVATE);


        InCheck = sh.getBoolean("iCheck", false);
        CTCAppLoadAds.putBoolean("isFirstTime", true);
        CTCAppLoadAds.putBoolean("isBackFirstTime", true);

        startSplash();


    }


    public void startSplash() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            VERSION = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            VERSION = BuildConfig.VERSION_CODE;
        }
        callUpdateApi();
    }

    private void callUpdateApi() {

        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        JsonObject object = new JsonObject();
        try {
            object.addProperty("AndroidId", android_id);
            object.addProperty("VersionCode", 1);
            object.addProperty("PkgName", BuildConfig.APPLICATION_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RenClient.getInstance().getApi().getUpdatesResponse(EasyAES.encryptString(object.toString()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            BaseApplication.adModel = null;
                            JSONObject jsonObject = new JSONObject(EasyAES.decryptString(response.body().string()));
                            CTCModelAd model = new Gson().fromJson(jsonObject.toString(), CTCModelAd.class);
                            CTCAppLoadAds.setModelAd(model);
                            CTCAppLoadAds.putInt(BaseApplication.ADS_COUNT_SHOW, 0);
                            CTCAppLoadAds.putInt(BaseApplication.ADS_COUNT_BACK_SHOW, 0);
                            if (BaseApplication.getAdModel().getAdsAppUpDate().equalsIgnoreCase("Yes")) {
                                showUpdateDialog();
                            } else {
                                gotoSkip();
                            }
                        } catch (Exception e) {
                            Log.e("TAG", e.toString());
                            e.printStackTrace();
                            gotoSkip();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("TAG", t.getMessage());
                        gotoSkip();
                    }
                });
    }


    public void gotoSkip() {

        if (BaseApplication.getAdModel().getAdsNative().equalsIgnoreCase("Google")) {
            CTCAppLoadAds.getInstance().setApplicationId(this);

            MobileAds.initialize(getApplicationContext(), initializationStatus -> {
            });

            CTCAppLoadAds.getInstance().preloadNativeGoogleOne(SplashActivity.this);

            CTCAppLoadAds.getInstance().loadInterstitial(SplashActivity.this);

            CTCAppLoadAds.getInstance().showOpenAdIfAvailable(this, this::goNext);
        } else {

            CTCAppLoadAds.getInstance().loadInterstitial(SplashActivity.this);

            new Handler().postDelayed(this::goNext, 2000);
        }
    }


    public void showUpdateDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.findViewById(R.id.btnUpdate)
                .setOnClickListener(v -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BaseApplication.getAdModel().getAdsAppUpDateLink())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void goNext() {
        if (BaseApplication.getAdModel().getExtraScreen().equalsIgnoreCase("Yes")) {
            if (InCheck) {
                startActivity(new Intent(SplashActivity.this, StartActivity.class));

            } else {
                startActivity(new Intent(SplashActivity.this, IntroActivity.class));

            }

        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));

        }

    }




}
