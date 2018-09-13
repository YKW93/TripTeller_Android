package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;

public class LocalPopularityTellerVP extends PagerAdapter {

    private Context context;

    public LocalPopularityTellerVP(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_viewpager_localtellerwriting, container, false);
        ImageView imageView = v.findViewById(R.id.mainImage);
        GlideApp.with(context).load(R.drawable.dd).into(imageView);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public float getPageWidth(int position) {
        return (0.968f);
    }
}

