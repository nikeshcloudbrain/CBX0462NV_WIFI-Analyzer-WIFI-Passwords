package com.internet.speedtest.speedcheck.nvboost.pingTools;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;


import com.internet.speedtest.speedcheck.nvboost.R;

import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CustomPingAdapter extends RecyclerView.Adapter<CustomPingAdapter.Holder> {
    Context context;
    LayoutInflater inflter;
    ArrayList<String> time;

    /* loaded from: classes2.dex */
    public class Holder extends RecyclerView.ViewHolder {
        AppCompatTextView tv1;
        AppCompatTextView tv2;

        public Holder(View view) {
            super(view);
            this.tv1 = (AppCompatTextView) view.findViewById(R.id.tv1);
            this.tv2 = (AppCompatTextView) view.findViewById(R.id.tv2);
        }
    }

    public CustomPingAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.time = arrayList;
        Log.i("ContentValues", "CustomPingAdapter: " + arrayList.size());
        this.inflter = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(this.context).inflate(R.layout.ping_list_layout_item, viewGroup, false));
    }

    public void onBindViewHolder(Holder holder, int i) {
        holder.tv2.setVisibility(8);
        AppCompatTextView appCompatTextView = holder.tv1;
        appCompatTextView.setText("#" + (getItemCount() - i) + ": " + String.valueOf(this.time.get(i)));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return this.time.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        ArrayList<String> arrayList = this.time;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }
}
