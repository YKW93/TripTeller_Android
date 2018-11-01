package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommonItem;

import java.util.ArrayList;

public class MyMarkDataRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DetailCommonItem> detailCommonItem;

    public MyMarkDataRVAdapter(Context context) {
        mContext = context;
        detailCommonItem = new ArrayList<>();
    }

    public void setData(ArrayList<DetailCommonItem> data) {
        detailCommonItem.clear();
        detailCommonItem.addAll(data);
        notifyDataSetChanged();
    }

    private class MyMarkDataVH extends RecyclerView.ViewHolder {

        private ImageView thumb;
        private TextView name;

        public MyMarkDataVH(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            name = itemView.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyMarkDataVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_tourtype2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyMarkDataVH myMarkDataVH = (MyMarkDataVH) holder;

        GlideApp.with(mContext).load(detailCommonItem.get(position).firstimage).into(myMarkDataVH.thumb);
        myMarkDataVH.name.setText(detailCommonItem.get(position).title);
    }

    @Override
    public int getItemCount() {
        return detailCommonItem.size();
    }
}
