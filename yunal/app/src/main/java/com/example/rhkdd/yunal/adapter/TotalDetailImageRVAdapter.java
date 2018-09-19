package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rhkdd.yunal.DetailImageSliderActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.detailImage.DetailImageItem;

import java.util.ArrayList;

public class TotalDetailImageRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<DetailImageItem> detailImageItems;

    public TotalDetailImageRVAdapter(Context context) {
        this.context = context;
        detailImageItems = new ArrayList<>();
    }

    public void setDatas(ArrayList<DetailImageItem> lists) {
        detailImageItems.addAll(lists);
    }

    private class DetailImagesVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView detailImage;

        public DetailImagesVH(View itemView) {
            super(itemView);
            detailImage = itemView.findViewById(R.id.detailImage);
            detailImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailImageSliderActivity.newIntent(context, detailImageItems, getAdapterPosition(), 1);
            context.startActivity(intent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailImagesVH(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_detailimage, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DetailImagesVH detailImagesVH = (DetailImagesVH) holder;
        GlideApp.with(context).load(detailImageItems.get(position).originimgurl).into(detailImagesVH.detailImage);
    }

    @Override
    public int getItemCount() {
        return detailImageItems.size();
    }
}