package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rhkdd.yunal.DetailImageActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.data.detailImage.DetailImageItem;

import java.util.ArrayList;

public class DetailMainImageVPAdapter extends PagerAdapter implements View.OnClickListener {
    private Context context;
    private ArrayList<DetailImageItem> lists;

    public DetailMainImageVPAdapter(Context context) {
        this.context = context;
        lists = new ArrayList<>();
    }

    public void setData(ArrayList<DetailImageItem> list) {
        lists.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)  {
        View v = LayoutInflater.from(context).inflate(R.layout.item_viewpager_image, container,false);
        ImageView imageView = v.findViewById(R.id.representativeImage);
        if (lists.get(position).originimgurl.equals(String.valueOf(R.drawable.no_image))) { // 메인이미지가 없을 경우 no_image를 string 값으로 넣어놔서 확인후 imageview에 no_image를 뿌려줌
            GlideApp.with(context).load(R.drawable.no_image).into(imageView);
        } else {
            GlideApp.with(context).load(lists.get(position).originimgurl).into(imageView);
        }
        imageView.setOnClickListener(this);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.representativeImage :
                Intent intent = DetailImageActivity.newIntent(context, lists);
                context.startActivity(intent);
                break;
        }
    }
}
