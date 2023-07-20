package com.internet.speedtest.speedcheck.nvboost.activi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivitySpeedGraphBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;
import com.internet.speedtest.speedcheck.nvboost.speedGraph.SpeedDataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SpeedGraphActivity extends AppCompatActivity {
ActivitySpeedGraphBinding binding;
    float downloadSpeed = 0.0f;
    float uploadSpeed = 0.0f;
    List<SpeedDataPoint> speedEntries = new ArrayList<SpeedDataPoint>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySpeedGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        speedEntries = new ArrayList<>();


        CTCAppLoadAds.getInstance().showNativeBottomDynamic(this, binding.frameViewAds);





        if(ConnectivityReceiver.isConnected()){
            mStartRX = TrafficStats.getTotalRxBytes();
            mStartTX = TrafficStats.getTotalTxBytes();


            if (mStartRX == TrafficStats.UNSUPPORTED
                    || mStartTX == TrafficStats.UNSUPPORTED) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SpeedGraphActivity.this);
                alert.setTitle("Uh Oh!");
                alert.setMessage("Your device does not support traffic stat monitoring.");
                alert.show();

            } else {
                mHandler.postDelayed(mRunnable, 1000);

            }

// Step 4: Customize the chart


        }else{
            Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
            finish();
        }



        binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.tool.toolText.setText("Wi-Fi Speed Graph");


    }


    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            long resetdownload=TrafficStats.getTotalRxBytes();

            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            downloadSpeed=rxBytes;

            if (rxBytes >= 1024) {

                long rxKb = rxBytes / 1024;

                downloadSpeed=rxKb;


                if (rxKb >= 1024) {

                    long rxMB = rxKb / 1024;


                    downloadSpeed=rxMB;
                    if (rxMB >= 1024) {

                        long rxGB = rxMB / 1024;
                        downloadSpeed=rxMB;

                    }
                }
            }

            mStartRX=resetdownload;

            long resetupload=TrafficStats.getTotalTxBytes();

            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;

            uploadSpeed=txBytes;

//            binding.tx.setText(Long.toString(txBytes)+" bytes");

            if (txBytes >= 1024) {

                long txKb = txBytes / 1024;

                uploadSpeed=txKb;

//                binding.tx.setText(Long.toString(txKb)+" KBs");

                if (txKb >= 1024) {

                    long txMB = txKb / 1024;

                    uploadSpeed=txMB;

//                    binding.tx.setText(Long.toString(txMB)+" MBs");

                    if (txMB >= 1024) {

                        long txGB = txMB / 1024;

                        uploadSpeed=txGB;

//                        binding.tx.setText(Long.toString(txGB)+" GBs");

                    }
                }
            }

            Log.e("TAG", "run: "+downloadSpeed+" "+uploadSpeed );
            speedEntries.add(new SpeedDataPoint(System.currentTimeMillis() , downloadSpeed, uploadSpeed));
            // Create an ArrayList for download speeds
            ArrayList<Entry> downloadEntries = new ArrayList<>();
            for (SpeedDataPoint speedEntry : speedEntries) {
                downloadEntries.add(new Entry(speedEntry.getTimestamp(), speedEntry.getDownloadSpeed()));
            }

// Create an ArrayList for upload speeds
            ArrayList<Entry> uploadEntries = new ArrayList<>();
            for (SpeedDataPoint speedEntry : speedEntries) {
                uploadEntries.add(new Entry(speedEntry.getTimestamp(), speedEntry.getUploadSpeed()));
            }
            // Create a dataset for download speeds
            LineDataSet downloadDataSet = new LineDataSet(downloadEntries, "Download Speed");
            downloadDataSet.setColor(Color.BLUE);
            downloadDataSet.setCircleColor(Color.BLUE);
            downloadDataSet.setValueTextColor(Color.BLACK);

// Create a dataset for upload speeds
            LineDataSet uploadDataSet = new LineDataSet(uploadEntries, "Upload Speed");
            uploadDataSet.setColor(Color.RED);
            uploadDataSet.setCircleColor(Color.RED);
            uploadDataSet.setValueTextColor(Color.BLACK);

            LineData lineData = new LineData(downloadDataSet, uploadDataSet);
            LineChart chart = findViewById(R.id.downChart);
            chart.setData(lineData);
            chart.getDescription().setEnabled(false);
            chart.getLegend().setTextColor(Color.BLACK);
            chart.getXAxis().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date((long) value));
                }
            });
            chart.getAxisLeft().setTextColor(Color.BLACK);
            chart.getAxisRight().setEnabled(false);
            chart.setTouchEnabled(true);
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            chart.setPinchZoom(true);
            chart.setBackgroundColor(Color.TRANSPARENT);

            chart.invalidate();
            mStartTX=resetupload;


            mHandler.postDelayed(mRunnable, 500);
        }
    };


    @Override
    public void onBackPressed() {
        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}