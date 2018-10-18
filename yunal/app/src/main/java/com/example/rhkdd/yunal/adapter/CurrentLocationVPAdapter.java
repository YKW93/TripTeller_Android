package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.locationBased.LocationBasedItem;

import java.util.ArrayList;

public class CurrentLocationVPAdapter extends PagerAdapter {

    Context context;
    ArrayList<LocationBasedItem> lists;

    public CurrentLocationVPAdapter(Context context) {
        this.context = context;
        lists = new ArrayList<>();
    }

    public void setData(ArrayList<LocationBasedItem> lists) {
        this.lists.addAll(lists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager_currentlocation, container, false);

        ImageView mapIV = view.findViewById(R.id.map_image);
        TextView mapTV = view.findViewById(R.id.map_title);
        TextView mapLocationTV = view.findViewById(R.id.map_location);

        if (lists.get(position).firstimage != null) {
            GlideApp.with(context).load(lists.get(position).firstimage).into(mapIV);
        } else {
            GlideApp.with(context).load(R.drawable.no_image).into(mapIV);
        }

        mapTV.setText(lists.get(position).title);

        contentTypeSetting(lists.get(position).contenttypeid, view);

        if (lists.get(position).addr1 == null && lists.get(position).addr2 == null) {
            mapLocationTV.setText("");
        } else if (lists.get(position).addr2 == null) {
            mapLocationTV.setText(lists.get(position).addr1);
        } else {
            String addr = lists.get(position).addr1 + lists.get(position).addr2;
            mapLocationTV.setText(addr);
        }

        container.addView(view);

        return view;
    }

    public void contentTypeSetting(int typeid, View view) {
        ImageView mapContentTypeIV = view.findViewById(R.id.map_contentType_image);
        TextView mapContentTypeTV = view.findViewById(R.id.map_contentType);

        if (typeid == 12) { // 관광지
            mapContentTypeIV.setImageResource(R.drawable.travel);
            mapContentTypeTV.setText("관광지");

        } else if (typeid == 14) { // 문화시설
            mapContentTypeIV.setImageResource(R.drawable.culture);
            mapContentTypeTV.setText("문화시설");

        } else if (typeid == 15) { // 행사/공연/축제
            mapContentTypeIV.setImageResource(R.drawable.festival);
            mapContentTypeTV.setText("행사/공연/축제");

        } else if (typeid == 28) { // 레포츠
            mapContentTypeIV.setImageResource(R.drawable.leisure);
            mapContentTypeTV.setText("레포츠");

        } else if (typeid == 32) { // 숙박
            mapContentTypeIV.setImageResource(R.drawable.lodgment);
            mapContentTypeTV.setText("숙박");
        } else if (typeid == 38) { // 쇼핑
            mapContentTypeIV.setImageResource(R.drawable.shopping);
            mapContentTypeTV.setText("쇼핑");

        } else if (typeid == 39) { // 음식점
            mapContentTypeIV.setImageResource(R.drawable.food);
            mapContentTypeTV.setText("음식점");

        }
    }

    @Override
    public int getCount() {
        return lists.size();
    }

//    @Override
//    public float getPageWidth(int position) {
//        return (0.945f);
//    }
//0.945f
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
