package com.internet.speedtest.speedcheck.nvboost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.internet.speedtest.speedcheck.nvboost.R;
import com.internet.speedtest.speedcheck.nvboost.model.NextScreen;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    Context mContext;
    List<NextScreen> mListScreen;

    public IntroViewPagerAdapter(Context mCOntext, List<NextScreen> mListScreen){
        this.mContext = mCOntext;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_slider_design, null);
        ImageView imgSlide = layoutScreen.findViewById(R.id.InImg);
//        ImageView imgSlideImg = layoutScreen.findViewById(R.id.nextdw);

        TextView title1 = layoutScreen.findViewById(R.id.nextTitle1);
        TextView title2 = layoutScreen.findViewById(R.id.nextTitle2);

        TextView description = layoutScreen.findViewById(R.id.nextDes);

        title1.setText(mListScreen.get(position).getTitle1());
        title2.setText(mListScreen.get(position).getTitle2());
        description.setText(mListScreen.get(position).getDescription());
        imgSlide.setImageResource(mListScreen.get(position).getScreenImg());
//        imgSlideImg.setImageResource(mListScreen.get(position).getNextSlideImg());

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount(){
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o){
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((View) object);
    }
}
