package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.tourDetail.TourReviewItem;

import java.util.ArrayList;

public class LocalPopularityTellerVPAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<TourReviewItem> tourReviewItems;

    public LocalPopularityTellerVPAdapter(Context context) {
        this.context = context;
        tourReviewItems = new ArrayList<>();
    }

    public void setData(ArrayList<TourReviewItem> list) {
        tourReviewItems.clear();
        tourReviewItems.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tourReviewItems.size();
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
        TextView contentTV = v.findViewById(R.id.contentTV);
        TextView likeCount = v.findViewById(R.id.likeCount);
        TextView userName = v.findViewById(R.id.userName);

        if (!tourReviewItems.get(position).photo.isEmpty()) { // 사용자 리뷰 이미지가 있을 경우
            GlideApp.with(context).load(tourReviewItems.get(position).photo.get(0).photo).into(imageView);
        } else { // 없을 경우
            GlideApp.with(context).load(R.drawable.temp_image).into(imageView);
        }

        contentTV.setText(tourReviewItems.get(position).content);
        likeCount.setText(String.valueOf(tourReviewItems.get(position).like));
        userName.setText(String.valueOf("by Teller " + tourReviewItems.get(position).author));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

//    @Override
//    public float getPageWidth(int position) {
//        return (0.968f);
//    }
}

