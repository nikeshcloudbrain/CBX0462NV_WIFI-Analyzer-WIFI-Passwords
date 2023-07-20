package com.internet.speedtest.speedcheck.nvboost.adsp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.gson.Gson;
import com.internet.speedtest.speedcheck.nvboost.BaseApplication;
import com.internet.speedtest.speedcheck.nvboost.BuildConfig;
import com.internet.speedtest.speedcheck.nvboost.R;

import java.util.ArrayList;
import java.util.Collections;

public class CTCAppLoadAds {
    public final static int ARRAY_SIZE = 3;
    public static CTCAppLoadAds instance;

    //Interstitial
    private boolean isInterstitialAdLoading = false;
    private InterstitialAd interstitialGoogle = null;



    private NativeAd nativeOneGoogle = null;
    private NativeAd nativeTwoGoogle = null;

    //Native List
    private final ArrayList<com.google.android.gms.ads.nativead.NativeAd> nativeAdsGoogle = new ArrayList<>();

    public static CTCAppLoadAds getInstance() {
        if (instance == null) {
            instance = new CTCAppLoadAds();
        }
        return instance;
    }

    public void setApplicationId(Activity activity) {
        try {
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", BaseApplication.getAdModel().getAdsAppId());
        } catch (Exception e) {
            Log.e("Failed to load", e.getMessage());
        }
    }

    public void showOpenAdIfAvailable(Activity activity, AdCompleteListener listener) {
        if (BaseApplication.getAdModel().getAdsSplash().equalsIgnoreCase("AppOpen")) {
            BaseApplication.getInstance().showAdIfAvailable(activity, () -> {
                BaseApplication.booleanSplashAds = false;
                listener.onAdCompleted();
            });
        } else {
            BaseApplication.booleanSplashAds = false;
            listener.onAdCompleted();
        }
    }

    public interface AdCompleteListener {
        void onAdCompleted();
    }

    //Interstitial
    public void loadInterstitial(final Activity context) {
         if (BaseApplication.getAdModel().getAdsInterstitial().equalsIgnoreCase("Google")) {
            loadInterstitialGoogle(context);
        }
    }


    private void loadInterstitialGoogle(final Activity context) {
        if (interstitialGoogle == null && !isInterstitialAdLoading) {
            isInterstitialAdLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            com.google.android.gms.ads.interstitial.InterstitialAd.load(context, BaseApplication.getAdModel().getAdsInterstitialId(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                    interstitialGoogle = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    interstitialGoogle = null;
                    isInterstitialAdLoading = false;

                }
            });
        }
    }

    public void showInterstitial(Activity context, AdCompleteListener listener) {
        this.listener = listener;

            if (getInt(BaseApplication.ADS_COUNT_SHOW, 0) == (getBoolean("isFirstTime", false) ? BaseApplication.getAdModel().getAdsInterstitialCountShow() : BaseApplication.getAdModel().getAdsInterstitialCount())) {
                if (interstitialGoogle != null) {
                    interstitialGoogle.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            interstitialGoogle = null;
                            isInterstitialAdLoading = false;
                            putBoolean("isFirstTime", false);
                            putInt(BaseApplication.ADS_COUNT_SHOW, 0);
                            if (listener != null) {
                                listener.onAdCompleted();
                            }
                            if (BaseApplication.getAdModel().getAdsInterstitial().equalsIgnoreCase("Google")) {
                                loadInterstitialGoogle(context);
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                            interstitialGoogle = null;
                            isInterstitialAdLoading = false;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            interstitialGoogle = null;
                        }
                    });
                    interstitialGoogle.show(context);
                } else {
                    loadInterstitial(context);
                    if (listener != null) {
                        listener.onAdCompleted();
                    }
                }
            } else {
                putInt(BaseApplication.ADS_COUNT_SHOW, getInt(BaseApplication.ADS_COUNT_SHOW, 0) + 1);
                if (listener != null) {
                    listener.onAdCompleted();
                }
            }

    }

    AdCompleteListener listener;

    public void showInterstitialBack(Activity context, AdCompleteListener listener) {
        this.listener = listener;

            if (getInt(BaseApplication.ADS_COUNT_BACK_SHOW, 0) == (getBoolean("isBackFirstTime", false) ? BaseApplication.getAdModel().getAdsInterstitialBackCountShow() : BaseApplication.getAdModel().getAdsInterstitialBackCount())) {
                putBoolean("isBackFirstTime", false);
                if (interstitialGoogle != null) {
                    putInt(BaseApplication.ADS_COUNT_BACK_SHOW, 0);
                    interstitialGoogle.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            interstitialGoogle = null;
                            isInterstitialAdLoading = false;
                            putBoolean("isBackFirstTime", false);
                            putInt(BaseApplication.ADS_COUNT_BACK_SHOW, 0);
                            if (listener != null) {
                                listener.onAdCompleted();
                            }
                            if (BaseApplication.getAdModel().getAdsInterstitial().equalsIgnoreCase("Google")) {
                                loadInterstitialGoogle(context);
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                            interstitialGoogle = null;
                            isInterstitialAdLoading = false;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            interstitialGoogle = null;
                        }
                    });
                    interstitialGoogle.show(context);
                } else {
                    loadInterstitial(context);
                    if (listener != null) {
                        listener.onAdCompleted();
                    }
                }
            } else {
                putInt(BaseApplication.ADS_COUNT_BACK_SHOW, getInt(BaseApplication.ADS_COUNT_BACK_SHOW, 0) + 1);
                if (listener != null) {
                    listener.onAdCompleted();
                }
            }

    }

    //Banner
    public void showBanner(final Activity activity, final LinearLayout linearLayout) {

            loadBannerGoogle(activity, linearLayout);

    }

    //Banner Google
    private void loadBannerGoogle(final Activity activity, final LinearLayout linearLayout) {
        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(activity);
        adView.setAdUnitId(BaseApplication.getAdModel().getAdsBannerId());
        com.google.android.gms.ads.AdSize adSize = getAdSize(activity);
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                linearLayout.removeAllViews();
                linearLayout.addView(adView);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                loadBannerGoogle(activity, linearLayout);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

            }
        });
    }



    private com.google.android.gms.ads.AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    //Native
    public void showNativeMediaFix(Activity activity, FrameLayout frameViewAds) {
        showNative(activity, frameViewAds, "Fix");
    }

    public void showNativeBottomDynamic(Activity activity, FrameLayout frameViewAds) {
        showNative(activity, frameViewAds, "No");
    }

    public void showNativeMediaMatch(Activity activity, FrameLayout frameViewAds) {
        showNative(activity, frameViewAds, "Yes");
    }


    public void displayListNativeAds(Activity activity, FrameLayout frameViewAds) {
       if (BaseApplication.getAdModel().getAdsNative().equalsIgnoreCase("Google")) {

                showAdmobList(activity, frameViewAds);

        }
    }



    private void showAdmobList(Activity activity, FrameLayout frameViewAds) {
        if (nativeAdsGoogle.size() > 0) {
            Collections.shuffle(nativeAdsGoogle);
            frameViewAds.setVisibility(View.VISIBLE);
            frameViewAds.removeAllViews();
            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_fix, null);
            inflateNativeGoogle(activity, nativeAdsGoogle.get(0), adView);
            frameViewAds.addView(adView);
        }
    }

    private void showNative(Activity activity, FrameLayout frameViewAds, String showMedia) {
        if (BaseApplication.getAdModel().getAdsNative().equalsIgnoreCase("Google")) {
            showNativeGoogle(activity, frameViewAds, showMedia);
        }
    }

    //Native Google
    public void preloadNativeGoogleOne(Activity activity) {
        if (BaseApplication.getAdModel().getAdsNativePreload().equalsIgnoreCase("Yes")) {
            loadNativeGoogleOne(activity);
        }
    }

    private void loadNativeGoogleOne(Activity activity) {
        nativeTwoGoogle = null;
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        AdLoader appLoaderNativeOne = new AdLoader.Builder(activity, BaseApplication.getAdModel().getAdsNativeId()).forNativeAd(nativeAd -> {
            nativeOneGoogle = nativeAd;
            if (nativeAdsGoogle.size() < ARRAY_SIZE) {
                nativeAdsGoogle.add(nativeOneGoogle);
            }
        }).withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        }).withNativeAdOptions(adOptions).build();

        AdRequest adRequest = new AdRequest.Builder().build();
        appLoaderNativeOne.loadAd(adRequest);

    }

    private void loadNativeGoogleTwo(Activity activity) {
        nativeOneGoogle = null;
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        AdLoader appLoaderNativeTwo = new AdLoader.Builder(activity, BaseApplication.getAdModel().getAdsNativeId()).forNativeAd(nativeAd -> {
            nativeTwoGoogle = nativeAd;
            if (nativeAdsGoogle.size() < ARRAY_SIZE) {
                nativeAdsGoogle.add(nativeTwoGoogle);
            }
        }).withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        }).withNativeAdOptions(adOptions).build();
        AdRequest adRequest = new AdRequest.Builder().build();
        appLoaderNativeTwo.loadAd(adRequest);
    }

    @SuppressLint("InflateParams")
    private void showNativeGoogle(Activity activity, FrameLayout frameViewAds, String showMedia) {
        if (nativeOneGoogle != null) {
            frameViewAds.setVisibility(View.VISIBLE);
            frameViewAds.removeAllViews();
            NativeAdView adView = null;

            if (showMedia.equalsIgnoreCase("Yes")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native, null);
            } else if (showMedia.equalsIgnoreCase("No")) {
                if (BaseApplication.getAdModel().getAdsNativeViewId() == 1) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small, null);
                } else if (BaseApplication.getAdModel().getAdsNativeViewId() == 2) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small_2, null);
                }
            } else if (showMedia.equalsIgnoreCase("Fix")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_fix, null);
            }

            inflateNativeGoogle(activity, nativeOneGoogle, adView);
            frameViewAds.addView(adView);

            if (!BaseApplication.IS_NATIVE_AD_LAST) {
                nativeTwoGoogle = null;
                loadNativeGoogleTwo(activity);
            }

        } else if (nativeTwoGoogle != null) {
            frameViewAds.setVisibility(View.VISIBLE);
            frameViewAds.removeAllViews();

            NativeAdView adView = null;

            if (showMedia.equalsIgnoreCase("Yes")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native, null);
            } else if (showMedia.equalsIgnoreCase("No")) {
                if (BaseApplication.getAdModel().getAdsNativeViewId() == 1) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small, null);
                } else if (BaseApplication.getAdModel().getAdsNativeViewId() == 2) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small_2, null);
                }
            } else if (showMedia.equalsIgnoreCase("Fix")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_fix, null);
            }

            inflateNativeGoogle(activity, nativeTwoGoogle, adView);
            frameViewAds.addView(adView);

            if (!BaseApplication.IS_NATIVE_AD_LAST) {
                nativeOneGoogle = null;
                loadNativeGoogleOne(activity);
            }
        } else {
            showNativeGoogleDefault(activity, frameViewAds, showMedia);
        }
    }

    private void showNativeGoogleDefault(Activity activity, FrameLayout frameViewAds, String showMedia) {
        VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        @SuppressLint("InflateParams") AdLoader adLoaderNativeOne = new AdLoader.Builder(activity, BaseApplication.getAdModel().getAdsNativeId()).forNativeAd(nativeAd -> {
            frameViewAds.setVisibility(View.VISIBLE);
            frameViewAds.removeAllViews();

            if (nativeAdsGoogle.size() < ARRAY_SIZE) {
                nativeAdsGoogle.add(nativeAd);
            }

            NativeAdView adView = null;
            if (showMedia.equalsIgnoreCase("Yes")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native, null);
            } else if (showMedia.equalsIgnoreCase("No")) {
                if (BaseApplication.getAdModel().getAdsNativeViewId() == 1) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small, null);
                } else if (BaseApplication.getAdModel().getAdsNativeViewId() == 2) {
                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_small_2, null);
                }
            } else if (showMedia.equalsIgnoreCase("Fix")) {
                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.google_native_fix, null);
            }

            inflateNativeGoogle(activity, nativeAd, adView);
            frameViewAds.addView(adView);

        }).withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                showNative(activity, frameViewAds, showMedia);
            }
        }).withNativeAdOptions(adOptions).build();

        AdRequest adRequest = new AdRequest.Builder().build();
        adLoaderNativeOne.loadAd(adRequest);
    }

    private void inflateNativeGoogle(Activity activity, com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        //adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        AppCompatButton install = adView.findViewById(R.id.ad_call_to_action);
        install.setText(nativeAd.getCallToAction());
        if (BaseApplication.getAdModel().getAdsNativeColor().equalsIgnoreCase("Yes")) {
            install.getBackground().setColorFilter(activity.getColor(R.color.color_1), PorterDuff.Mode.SRC_ATOP);
        } else if (BaseApplication.getAdModel().getAdsNativeColor().equalsIgnoreCase("No")) {
            install.getBackground().setColorFilter(activity.getColor(R.color.color_2), PorterDuff.Mode.SRC_ATOP);
        } else {
            install.getBackground().setColorFilter(activity.getColor(R.color.color_1), PorterDuff.Mode.SRC_ATOP);
        }

        adView.setCallToActionView(install);

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
            CardView ad_app_icon_cards = adView.findViewById(R.id.ad_app_icon_cards);
            ad_app_icon_cards.setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
            CardView ad_app_icon_cards = adView.findViewById(R.id.ad_app_icon_cards);
            ad_app_icon_cards.setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        VideoController vc = nativeAd.getMediaContent().getVideoController();
        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }








    //Shared preference added at 24-06-2023
    private static SharedPreferences getPreference() {
        return BaseApplication.getInstance().getSharedPreferences("DEFAULT_PREFERENCE" + BuildConfig.VERSION_CODE, Context.MODE_PRIVATE);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = getPreference();
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        try {
            SharedPreferences sharedPreferences = getPreference();
            return sharedPreferences.getBoolean(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = getPreference();
        sharedPreferences.edit().putInt(key, value).apply();
    }

    private int getInt(String key, int defaultValue) {
        try {
            SharedPreferences sharedPreferences = getPreference();
            return sharedPreferences.getInt(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void setModelAd(CTCModelAd modelAd) {
        if (modelAd != null) {
            SharedPreferences sharedPreferences = getPreference();
            sharedPreferences.edit().putString(BaseApplication.TAG, new Gson().toJson(modelAd)).apply();
        }
    }

    public static CTCModelAd getModelAd(CTCModelAd defaultModelAd) {
        try {
            SharedPreferences sharedPreferences = getPreference();
            String data = sharedPreferences.getString(BaseApplication.TAG, null);
            if (data == null || data.trim().equals("")) {
                return defaultModelAd;
            }
            return new Gson().fromJson(data, CTCModelAd.class);
        } catch (Exception e) {
            return defaultModelAd;
        }
    }
}
