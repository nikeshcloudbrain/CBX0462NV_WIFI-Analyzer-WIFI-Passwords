package com.internet.speedtest.speedcheck.nvboost.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.internet.speedtest.speedcheck.nvboost.BaseApplication;
import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;


public class Constant {





    public static void Log(String message) {
        Log.e("errorLog", message);
    }







    public static native String getMainApi();

    public static native String getKey1();

    public static native String getKey2();


    public static void showRateDialog(Activity activity, boolean isAds) {
        try {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_exit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setOnShowListener(dialogInterface -> {
                if (BaseApplication.getAdModel().getAdsExit().equalsIgnoreCase("YES")) {
                    dialog.findViewById(R.id.cardViewAdsMain).setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> CTCAppLoadAds.getInstance().showNativeMediaMatch(activity, dialog.findViewById(R.id.frameViewAdsMain)), 500);
                } else {
                    dialog.findViewById(R.id.cardViewAdsMain).setVisibility(View.GONE);
                }
            });
            dialog.findViewById(R.id.btnYes).setOnClickListener(view -> {
                dialog.dismiss();
                activity.finishAffinity();
            });

            dialog.findViewById(R.id.btnNo).setOnClickListener(view -> {
                dialog.dismiss();
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopSer(Activity activity) {
        Intent serviceIntent = new Intent(activity, MyIntentService.class);
        activity.stopService(serviceIntent);
    }


}
