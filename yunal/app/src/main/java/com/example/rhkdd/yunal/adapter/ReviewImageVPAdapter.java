package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.DetailImageSliderActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.tourDetail.PhotoItem;

import java.util.ArrayList;

public class ReviewImageVPAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<PhotoItem> lists;

    public ReviewImageVPAdapter(Context context) {
        this.context = context;
        lists = new ArrayList<>();
    }

    public void setData(ArrayList<PhotoItem> list) {
        lists.clear();
        lists.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_viewpager_commentimage, container, false);

        ImageView imageView = v.findViewById(R.id.commentImageView);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = DetailImageSliderActivity.newIntent(context, lists, position, 2);
                context.startActivity(intent);
            }
        });

        GlideApp.with(container).load(lists.get(position).photo).into(imageView);
        container.addView(v);

        return v;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public float getPageWidth(int position) {
        return (0.48f);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}