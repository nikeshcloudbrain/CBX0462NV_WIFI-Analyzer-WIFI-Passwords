package com.internet.speedtest.speedcheck.nvboost.activi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.adsp.CTCAppLoadAds;
import com.internet.speedtest.speedcheck.nvboost.databinding.ActivityPingBinding;
import com.internet.speedtest.speedcheck.nvboost.pingTools.ConnectivityReceiver;
import com.internet.speedtest.speedcheck.nvboost.pingTools.CustomPingAdapter;
import com.internet.speedtest.speedcheck.nvboost.pingTools.InputFilterMinMax;
import com.internet.speedtest.speedcheck.nvboost.pingTools.MyUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Pattern;


/* loaded from: classes2.dex */
public class PingToolsActivity extends AppCompatActivity {
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    ArrayAdapter<String> adapter;
    ArrayList<Float> arrayListPingList;
    AsyncTask asyncTask;
    EditText cntedt;
    Context context;
    int count;
    int delay;
    ArrayList<String> f158ms;
    Boolean flag = Boolean.FALSE;
    String host;
    TextView linNext;
    RecyclerView recyclerViewPingList;
    EditText timeedt;
    Toolbar toolbar;
    String txtHost;
    AutoCompleteTextView txtHostUrl;
ActivityPingBinding binding;
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding=ActivityPingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CTCAppLoadAds.getInstance().showNativeMediaMatch(this, binding.frameViewAdsMain);

        this.context = this;

binding.tool.icBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        onBackPressed();
    }
});
binding.tool.toolText.setText("Ping Tools");
        this.arrayListPingList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPingList);
        this.recyclerViewPingList = recyclerView;
        recyclerView.setHasFixedSize(true);
        this.recyclerViewPingList.setNestedScrollingEnabled(true);
        this.recyclerViewPingList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.linNext = (TextView) findViewById(R.id.linNext);
        this.txtHostUrl = (AutoCompleteTextView) findViewById(R.id.txtHostUrl);
        String[] strArr = MyUtility.getping((Activity) this.context);
        if (strArr != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.context, 17367043, strArr);
            this.adapter = arrayAdapter;
            this.txtHostUrl.setAdapter(arrayAdapter);
        }
        this.timeedt = (EditText) findViewById(R.id.edtTime);
        EditText editText = (EditText) findViewById(R.id.edTCount);
        this.cntedt = editText;
        editText.setFilters(new InputFilter[]{new InputFilterMinMax("0", "500", this.context, "Enter Valid Range From 0 - 500")});
        this.timeedt.setFilters(new InputFilter[]{new InputFilterMinMax("0", "10000", this.context, "Enter Valid Range From 0 - 1000")});
        this.txtHost = this.txtHostUrl.getText().toString();
        this.linNext.setOnClickListener(new View.OnClickListener() { // from class: wifi.analyzer.ip.tools.PingToolsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PingToolsActivity pingToolsActivity;
                String str;
                try {
                    PingToolsActivity.this.f158ms = new ArrayList<>();
                    if (ConnectivityReceiver.isConnected()) {
                        Object systemService = PingToolsActivity.this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        systemService.getClass();
                        ((InputMethodManager) systemService).hideSoftInputFromWindow(PingToolsActivity.this.txtHostUrl.getWindowToken(), 0);
                        PingToolsActivity pingToolsActivity2 = PingToolsActivity.this;
                        pingToolsActivity2.host = pingToolsActivity2.txtHostUrl.getText().toString().trim();
                        if (!PingToolsActivity.this.host.isEmpty()) {
                            PingToolsActivity.this.host.length();
                            PingToolsActivity pingToolsActivity22 = PingToolsActivity.this;
                            pingToolsActivity22.count = Integer.parseInt(pingToolsActivity22.cntedt.getText().toString());
                            PingToolsActivity pingToolsActivity3 = PingToolsActivity.this;
                            pingToolsActivity3.delay = Integer.parseInt(pingToolsActivity3.timeedt.getText().toString());
                            if (PingToolsActivity.this.f158ms.size() > 1 && PingToolsActivity.this.arrayListPingList.size() > 1) {
                                PingToolsActivity.this.f158ms.clear();
                                PingToolsActivity.this.arrayListPingList.clear();
                            }
                            PingToolsActivity.this.asyncTask = new initDb();
                            PingToolsActivity pingToolsActivity4 = PingToolsActivity.this;
                            String str2 = pingToolsActivity4.host;
                            if (str2 == null || !MyUtility.addping((Activity) pingToolsActivity4.context, str2) || (str = (pingToolsActivity = PingToolsActivity.this).host) == null) {
                                PingToolsActivity.this.asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
                                return;
                            }
                            ArrayAdapter<String> arrayAdapter2 = pingToolsActivity.adapter;
                            if (arrayAdapter2 != null) {
                                try {
                                    arrayAdapter2.add(str);
                                    PingToolsActivity.this.adapter.notifyDataSetChanged();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            } else {
                                try {
                                    String[] strArr2 = MyUtility.getping((Activity) pingToolsActivity.context);
                                    if (strArr2 != null) {
                                        PingToolsActivity.this.adapter = new ArrayAdapter<>(PingToolsActivity.this.context, 17367043, strArr2);
                                        PingToolsActivity pingToolsActivity5 = PingToolsActivity.this;
                                        pingToolsActivity5.txtHostUrl.setAdapter(pingToolsActivity5.adapter);
                                    }
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }
                            }
                        } else {
                            Toast.makeText(pingToolsActivity2, "Please Enter Valid Host", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PingToolsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        CTCAppLoadAds.getInstance().showInterstitialBack(this, this::finish);
    }

    public synchronized boolean getflag() {
        return this.flag.booleanValue();
    }

    public void doPing(int i, int i2, String str) throws IOException, InterruptedException {
        for (int i3 = 0; i3 < i && !getflag(); i3++) {
            try {
                Thread.sleep(i2);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            ping(str, i2);
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        try {
            AsyncTask asyncTask = this.asyncTask;
            if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                this.asyncTask.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isIPv6HexCompressedAddress(String str) {
        return str != null && IPV6_HEX_COMPRESSED_PATTERN.matcher(str).matches();
    }

    public static boolean isIPv6Address(String str) {
        return isIPv6StdAddress(str) || isIPv6HexCompressedAddress(str);
    }

    public static boolean isIPv6StdAddress(String str) {
        return str != null && IPV6_STD_PATTERN.matcher(str).matches();
    }

    public int ping(String str, int i) throws IOException, InterruptedException {
        String[] split;
        Runtime runtime = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();
        int max = Math.max(i / 1000, 1);
        int max2 = Math.max(128, 1);
        InetAddress byName = InetAddress.getByName(str);
        String hostAddress = byName.getHostAddress();
        String str2 = "ping";
        if (hostAddress == null) {
            hostAddress = byName.getHostName();
        } else if (isIPv6Address(hostAddress)) {
            str2 = "ping6";
        }
        Process exec = runtime.exec(str2 + " -c 1 -W " + max + " -t " + max2 + " " + hostAddress);
        exec.waitFor();
        int exitValue = exec.exitValue();
        if (exitValue == 0) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    split = sb.toString().split("\\n");
                    if (split.length > 1) {
                        break;
                    }
                } else {
                    sb.append(readLine);
                    sb.append("\n");
                }
            }
            String[] finalSplit = split;
            runOnUiThread(new Runnable() { // from class: wifi.analyzer.ip.tools.PingToolsActivity.2
                @Override // java.lang.Runnable
                public void run() {
                    PingToolsActivity.this.f158ms.add(0, finalSplit[1].toString());
                    Log.i("ContentValues", "run: " + PingToolsActivity.this.f158ms);
                    PingToolsActivity pingToolsActivity = PingToolsActivity.this;
                    CustomPingAdapter customPingAdapter = new CustomPingAdapter(pingToolsActivity.context, pingToolsActivity.f158ms);
                    PingToolsActivity.this.recyclerViewPingList.setAdapter(customPingAdapter);
                    customPingAdapter.notifyDataSetChanged();
                }
            });
        }
        return exitValue;
    }

    /* loaded from: classes2.dex */
    public class initDb extends AsyncTask<Object, Object, Void> {
        ProgressDialog mDialog;

        @Override // android.os.AsyncTask
        public void onPreExecute() {
        }

        initDb() {
            this.mDialog = new ProgressDialog(PingToolsActivity.this.context);
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Object... objArr) {
            try {
                isCancelled();
                try {
                    PingToolsActivity pingToolsActivity = PingToolsActivity.this;
                    pingToolsActivity.doPing(pingToolsActivity.count, pingToolsActivity.delay, pingToolsActivity.host);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Void r1) {
            try {
                this.mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
