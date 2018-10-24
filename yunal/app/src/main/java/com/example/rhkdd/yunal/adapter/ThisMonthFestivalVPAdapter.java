package com.example.rhkdd.yunal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestivalItem;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;

import java.util.ArrayList;

public class ThisMonthFestivalVPAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<SearchFestivalItem> searchFestivalItems;
    private ArrayList<TourInfoItem> tourInfoItems;

    public ThisMonthFestivalVPAdapter(Context context) {
        this.context = context;
        searchFestivalItems = new ArrayList<>();
        tourInfoItems = new ArrayList<>();
    }

    public void setData(ArrayList<SearchFestivalItem> lists, ArrayList<TourInfoItem> lists1) {
        this.searchFestivalItems.clear();
        this.tourInfoItems.clear();
        this.searchFestivalItems.addAll(lists);
        this.tourInfoItems.addAll(lists1);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return searchFestivalItems.size();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager_thismonthfestival, container, false);

        ImageView mainIV = view.findViewById(R.id.mainImage);
        TextView titleTV = view.findViewById(R.id.title);
        TextView addressTV = view.findViewById(R.id.address);
        TextView markCountTV = view.findViewById(R.id.markCount);
        TextView reviewCountTV = view.findViewById(R.id.reviewCount);

        if (searchFestivalItems.get(position).firstimage != null) {
            GlideApp.with(context).load(searchFestivalItems.get(position).firstimage).into(mainIV);
        } else {
            GlideApp.with(context).load(R.drawable.no_image).into(mainIV);
        }

        titleTV.setText(searchFestivalItems.get(position).title);

        if (searchFestivalItems.get(position).addr1 == null && searchFestivalItems.get(position).addr2 == null) {
            addressTV.setVisibility(View.GONE);
        } else if (searchFestivalItems.get(position).addr2 == null) {
            addressTV.setVisibility(View.VISIBLE);
            addressTV.setText(searchFestivalItems.get(position).addr1);
        } else {
            addressTV.setVisibility(View.VISIBLE);
            String addr = searchFestivalItems.get(position).addr1 + searchFestivalItems.get(position).addr2;
            addressTV.setText(addr);
        }


        mainIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailActivity.newIntent(context, searchFestivalItems.get(position).contentid);
                context.startActivity(intent);

            }
        });


        markCountTV.setText(String.valueOf(tourInfoItems.get(position).mark_cnt));
        reviewCountTV.setText(String.valueOf(tourInfoItems.get(position).review));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }
}
