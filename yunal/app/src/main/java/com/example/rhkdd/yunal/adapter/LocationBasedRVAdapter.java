package com.example.rhkdd.yunal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.locationBased.LocationBasedItem;

import java.util.ArrayList;

public class LocationBasedRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<LocationBasedItem> locationBasedItems;
    private Boolean checkRun = true;

    public LocationBasedRVAdapter(Context context) {
        this.context = context;
        locationBasedItems = new ArrayList<>();
    }

    public void setData(ArrayList<LocationBasedItem> lists) {
        locationBasedItems.clear();
        locationBasedItems.addAll(lists);
        notifyDataSetChanged();
    }

    private class LocationBasedVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumbIV;
        private TextView nameTV;
        private TextView distTV;
        public LocationBasedVH(View itemView) {
            super(itemView);
            thumbIV = itemView.findViewById(R.id.thumb);
            nameTV = itemView.findViewById(R.id.name);
            distTV = itemView.findViewById(R.id.dist);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                default: // 리스트 아이템 클릭했을 경우
                    if (checkRun) {

                        checkRun = false;

                        int contentid = locationBasedItems.get(getAdapterPosition()).contentid;
                        Intent intent = DetailActivity.newIntent(context, contentid);
                        context.startActivity(intent);
                        checkRun = true;
                    }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocationBasedVH(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_locationbased, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LocationBasedVH locationBasedVH = (LocationBasedVH) holder;

        if (locationBasedItems.get(position).firstimage != null) {
            GlideApp.with(context).load(locationBasedItems.get(position).firstimage).into(locationBasedVH.thumbIV);
        } else {
            GlideApp.with(context).load(R.drawable.no_image).into(locationBasedVH.thumbIV);
        }
        locationBasedVH.nameTV.setText(locationBasedItems.get(position).title);
        locationBasedVH.distTV.setText(String.valueOf(locationBasedItems.get(position).dist) + "m");
    }

    @Override
    public int getItemCount() {
        return locationBasedItems.size();
    }
}