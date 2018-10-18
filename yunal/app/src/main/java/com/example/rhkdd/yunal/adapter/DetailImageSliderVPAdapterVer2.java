package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.tourDetail.PhotoItem;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class DetailImageSliderVPAdapterVer2 extends PagerAdapter {

    private ArrayList<PhotoItem> uriLists;
    private Context mContext;

    public DetailImageSliderVPAdapterVer2(Context context) {
        mContext = context;
        uriLists = new ArrayList<>();
    }

    public void setData(ArrayList<PhotoItem> lists) {
        uriLists.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return uriLists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_detailimageslider, container, false);
//            ImageView imageView = v.findViewById(R.id.detailImage);
//            photoViewAttacher = new PhotoViewAttacher(imageView);
//            photoViewAttacher.update();
        PhotoView photoView = v.findViewById(R.id.detailImage);

        GlideApp.with(mContext).load(uriLists.get(position).photo).into(photoView);
        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}