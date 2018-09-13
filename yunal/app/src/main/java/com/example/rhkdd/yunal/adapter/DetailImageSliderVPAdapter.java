package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.data.detailImage.DetailImageItem;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class DetailImageSliderVPAdapter extends PagerAdapter {

    private ArrayList<DetailImageItem> detailImageItems;
    private Context context;

    public DetailImageSliderVPAdapter(Context context) {
        this.context = context;
        detailImageItems = new ArrayList<>();
    }

    public void setData(ArrayList<DetailImageItem> lists) {
        detailImageItems.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return detailImageItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_viewpager_detailimageslider, container, false);
//            ImageView imageView = v.findViewById(R.id.detailImage);
//            photoViewAttacher = new PhotoViewAttacher(imageView);
//            photoViewAttacher.update();
        PhotoView photoView = v.findViewById(R.id.detailImage);

        GlideApp.with(context).load(detailImageItems.get(position).originimgurl).into(photoView);
        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}