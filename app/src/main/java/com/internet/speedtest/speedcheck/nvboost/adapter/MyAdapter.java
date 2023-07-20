package com.internet.speedtest.speedcheck.nvboost.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ItemNativeListAdBinding;
import com.internet.speedtest.speedcheck.nvboost.databinding.RowBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity con;
    private ArrayList<ScanResult> arrayL;
    public final int VIEW_AD = 100, VIEW_NORMAL = 101;

    public MyAdapter(Activity context, ArrayList<ScanResult> arrayL) {
        this.con = context;
        this.arrayL = arrayL;
        setAds(true);

    }

    public void setAds(boolean isCheck) {
        arrayL.removeAll(Collections.singleton(null));
        int PARTICLE_AD_DISPLAY_COUNT = 5;

        ArrayList<ScanResult> tempArr = new ArrayList<>();
        for (int i = 0; i < arrayL.size(); i++) {
            if (arrayL.size() > PARTICLE_AD_DISPLAY_COUNT) {
                if (i != 0 && i % PARTICLE_AD_DISPLAY_COUNT == 0) {
                    tempArr.add(null);
                }
                tempArr.add(arrayL.get(i));
            } else {
                tempArr.add(arrayL.get(i));
            }
        }
        if (arrayL.size() > 0) {
            if (arrayL.size() % PARTICLE_AD_DISPLAY_COUNT == 0) {
                tempArr.add(null);
            }
        }

        this.arrayL = tempArr;
        if (isCheck) notifyDataSetChanged();
    }

    public static class AdHolder extends RecyclerView.ViewHolder {
        ItemNativeListAdBinding binding;

        public AdHolder(@NonNull ItemNativeListAdBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowBinding binding;

        public ViewHolder(@NonNull RowBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == VIEW_AD)
            return new AdHolder(ItemNativeListAdBinding.inflate(LayoutInflater.from(con), viewGroup, false));
        else
            return new ViewHolder(RowBinding.inflate(LayoutInflater.from(con), viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof AdHolder) {
            CTCAppLoadAds.getInstance().displayListNativeAds(con, ((AdHolder) holder).binding.frameViewAds);
        }else {

            ViewHolder myViewHolder = (ViewHolder) holder;
            Log.e("wifiList", "onBindViewHolder: " + arrayL.get(i).SSID);

            myViewHolder.binding.tvRow.setText(arrayL.get(i).SSID + " (" + arrayL.get(i).BSSID + ")");
            myViewHolder.binding.wFre.setText("Frequency :" + arrayL.get(i).frequency + " MHz");
            myViewHolder.binding.wSecurity.setText("Security : WPA2");
            myViewHolder.binding.wChannel.setText("Channels : " + arrayL.get(i).channelWidth);
            myViewHolder.binding.wdbm.setText(arrayL.get(i).level + "dbM");
            try {
                int rssi = arrayL.get(i).level;
                int percentage = rssi <= -100 ? 0 : rssi >= -50 ? 100 : (int) (((rssi - (-100)) * 100) / 50.0f);
                myViewHolder.binding.pWiFiSignalStrength.setProgress(percentage);
                myViewHolder.binding.tWiFiStrength.setText(String.valueOf(percentage) + " % ");
            } catch (Exception e) {
            }
        }




    }


    @Override
    public int getItemCount() {
        return arrayL.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayL.get(position) == null)
            return VIEW_AD;
        else return VIEW_NORMAL;
    }




}
