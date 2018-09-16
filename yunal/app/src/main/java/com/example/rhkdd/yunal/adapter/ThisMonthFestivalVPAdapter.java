package com.example.rhkdd.yunal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.data.searchFestival.SearchFestivalItem;

import java.util.ArrayList;

public class ThisMonthFestivalVPAdapter extends PagerAdapter{

    private Context context;
    private ArrayList<SearchFestivalItem> lists;

    public ThisMonthFestivalVPAdapter(Context context) {
        this.context = context;
        lists = new ArrayList<>();
    }

    public void setData(ArrayList<SearchFestivalItem> lists) {
        this.lists.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager_thismonthfestival, container, false);

        ImageView mainIV = view.findViewById(R.id.mainImage);
        TextView titleTV = view.findViewById(R.id.title);
        TextView addressTV = view.findViewById(R.id.address);
        TextView eventStartEndTV = view.findViewById(R.id.eventStartEnd);

        if (lists.get(position).firstimage != null) {
            GlideApp.with(context).load(lists.get(position).firstimage).into(mainIV);
        } else {
            GlideApp.with(context).load(R.drawable.no_image).into(mainIV);
        }

        titleTV.setText(lists.get(position).title);

        if (lists.get(position).addr1 == null && lists.get(position).addr2 == null) {
            addressTV.setVisibility(View.GONE);
        } else if (lists.get(position).addr2 == null) {
            addressTV.setVisibility(View.VISIBLE);
            addressTV.setText(lists.get(position).addr1);
        } else {
            addressTV.setVisibility(View.VISIBLE);
            String addr = lists.get(position).addr1 + lists.get(position).addr2;
            addressTV.setText(addr);
        }

        eventStartEndTV.setText(lists.get(position).eventstartdate + " ~ " + lists.get(position).eventenddate);


        mainIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailActivity.newIntent(context, lists.get(position).contentid);
                context.startActivity(intent);

            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {
        return (0.968f);
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }
}
