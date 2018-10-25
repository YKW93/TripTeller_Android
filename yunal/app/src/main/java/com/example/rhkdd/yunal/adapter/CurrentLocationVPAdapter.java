package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.locationBased.LocationBasedItem;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CurrentLocationVPAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<LocationBasedItem> locationBasedItems;
    private ArrayList<TourInfoItem> tourInfoItems;
    public CurrentLocationVPAdapter(Context context) {
        this.context = context;
        locationBasedItems = new ArrayList<>();
        tourInfoItems = new ArrayList<>();
    }

    public void setData(ArrayList<LocationBasedItem> lists, ArrayList<TourInfoItem> lists1) {
        locationBasedItems.clear();
        tourInfoItems.clear();
        locationBasedItems.addAll(lists);
        tourInfoItems.addAll(lists1);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager_currentlocation, container, false);

        CardView cardView = view.findViewById(R.id.info_cardview);
        ImageView mapIV = view.findViewById(R.id.map_image);
        TextView mapTV = view.findViewById(R.id.map_title);
        TextView mapLocationTV = view.findViewById(R.id.map_location);
        MaterialRatingBar materialRatingBar = view.findViewById(R.id.ratingbar);
        TextView ratingTV = view.findViewById(R.id.ratingbarTV);
        TextView markTV = view.findViewById(R.id.markTV);
        TextView reviewTV = view.findViewById(R.id.reviewTV);

        if (locationBasedItems.get(position).firstimage != null) {
            GlideApp.with(context).load(locationBasedItems.get(position).firstimage).into(mapIV);
        } else {
            GlideApp.with(context).load(R.drawable.no_image).into(mapIV);
        }

        mapTV.setText(locationBasedItems.get(position).title);

        if (locationBasedItems.get(position).addr1 == null && locationBasedItems.get(position).addr2 == null) {
            mapLocationTV.setText("");
        } else if (locationBasedItems.get(position).addr2 == null) {
            mapLocationTV.setText(locationBasedItems.get(position).addr1);
        } else {
            String addr = locationBasedItems.get(position).addr1 + locationBasedItems.get(position).addr2;
            mapLocationTV.setText(addr);
        }

        materialRatingBar.setRating(tourInfoItems.get(position).star);
        ratingTV.setText(String.valueOf(tourInfoItems.get(position).star));
        markTV.setText(String.valueOf("찜 " + tourInfoItems.get(position).mark_cnt));
        reviewTV.setText(String.valueOf("후기 " + tourInfoItems.get(position).review));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailActivity.newIntent(context, locationBasedItems.get(position).contentid);
                context.startActivity(intent);

            }
        });

        container.addView(view);

        return view;
    }


    @Override
    public int getCount() {
        return locationBasedItems.size();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
