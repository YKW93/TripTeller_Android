package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SelectAreaResultActivity;
import com.example.rhkdd.yunal.TotalFestivalActivity;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestivalItem;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;

import java.util.ArrayList;

public class TotalFestivalRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<SearchFestivalItem> searchFestivalItems;
    private ArrayList<TourInfoItem> tourInfoItems;


    public TotalFestivalRVAdapter(Context context) {
        this.context = context;
        searchFestivalItems = new ArrayList<>();
        tourInfoItems = new ArrayList<>();
    }

    public void setData(ArrayList<SearchFestivalItem> data, ArrayList<TourInfoItem> data1) {
        searchFestivalItems.clear();
        tourInfoItems.clear();
        searchFestivalItems.addAll(data);
        tourInfoItems.addAll(data1);
        notifyDataSetChanged();
    }


    public void changeData(int position, TourInfoItem tourInfoItem) {
        tourInfoItems.set(position, tourInfoItem);
        // 리스트에서 아이템을 클릭하고 DetailActivity -> SearchResultActivity로 넘어 왔을 경우
        notifyItemChanged(position);
    }

    private class TotalFestivalVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumbIV;
        private TextView titleTV;
        private TextView ratingTV;
        private TextView reviewTV;
        private TextView markTV;

        public TotalFestivalVH(View itemView) {
            super(itemView);
            thumbIV = itemView.findViewById(R.id.thumb);
            titleTV = itemView.findViewById(R.id.name);
            ratingTV = itemView.findViewById(R.id.ratingbarTV);
            reviewTV = itemView.findViewById(R.id.reviewTV);
            markTV = itemView.findViewById(R.id.markTV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(context, searchFestivalItems.get(getAdapterPosition()).contentid);
            context.startActivity(intent);
            ((TotalFestivalActivity)context).setPositionData(getAdapterPosition(), searchFestivalItems.get(getAdapterPosition()).contentid);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TotalFestivalVH(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_datatype1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TotalFestivalVH totalFestivalVH = (TotalFestivalVH) holder;

        if (searchFestivalItems.get(position).firstimage != null) {
            GlideApp.with(context).load(searchFestivalItems.get(position).firstimage).into(totalFestivalVH.thumbIV);
        } else {
            GlideApp.with(context).load(R.drawable.no_image).into(totalFestivalVH.thumbIV);
        }

        totalFestivalVH.titleTV.setText(searchFestivalItems.get(position).title);

        totalFestivalVH.ratingTV.setText(String.valueOf(tourInfoItems.get(position).star));
        totalFestivalVH.reviewTV.setText(String.valueOf("(후기 "+ tourInfoItems.get(position).review + ")"));
        totalFestivalVH.markTV.setText(String.valueOf(tourInfoItems.get(position).mark_cnt));
    }

    @Override
    public int getItemCount() {
        return searchFestivalItems.size();
    }
}
