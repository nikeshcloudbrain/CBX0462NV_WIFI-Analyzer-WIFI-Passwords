package com.internet.speedtest.speedcheck.nvboost.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.utils.LocalDeviceInfo;

import java.util.ArrayList;

public class WhoAdapter extends RecyclerView.Adapter<WhoAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context con;
    private ArrayList<LocalDeviceInfo> arrayL;

    public WhoAdapter(Context context, ArrayList<LocalDeviceInfo> arrayL) {
        inflater = LayoutInflater.from(context);
        this.con = context;
        this.arrayL = arrayL;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.item_who,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Log.e("wifiList", "onBindViewHolder: " +arrayL.get(i).getIp() );
myViewHolder.ip.setText(arrayL.get(i).getIp());
myViewHolder.mac.setText(arrayL.get(i).getMac());
myViewHolder.ip2.setText(arrayL.get(i).getIp());



    }

    @Override
    public int getItemCount() {
        return arrayL.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ip;
        TextView ip2;
        TextView mac;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ip = itemView.findViewById(R.id.ip);
            ip2 = itemView.findViewById(R.id.ip2);
            mac = itemView.findViewById(R.id.mac);


        }
    }

    public void updateDate(ArrayList<LocalDeviceInfo> arrayList){
        this.arrayL = arrayList;
        notifyDataSetChanged();
    }
}
