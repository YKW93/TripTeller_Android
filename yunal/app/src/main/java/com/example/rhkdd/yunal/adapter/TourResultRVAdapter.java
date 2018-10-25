package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SearchResultActivity;
import com.example.rhkdd.yunal.SelectAreaResultActivity;
import com.example.rhkdd.yunal.model.areaBase.AreaBaseItem;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;

import java.util.ArrayList;

public class TourResultRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AreaBaseItem> areaBaseItems;
    private ArrayList<TourInfoItem> tourInfoItems;
    private Boolean checkRun = true;

    public TourResultRVAdapter(Context context) {
        this.context = context;
        this.areaBaseItems = new ArrayList<>();
        this.tourInfoItems = new ArrayList<>();
    }

    public void setData(ArrayList<AreaBaseItem> data, ArrayList<TourInfoItem> data1) {
        this.areaBaseItems.clear();
        this.tourInfoItems.clear();
        this.areaBaseItems.addAll(data);
        this.tourInfoItems.addAll(data1);
        notifyDataSetChanged();
    }

    public void changeData(int position, TourInfoItem tourInfoItem) {
        tourInfoItems.set(position, tourInfoItem);
        // 리스트에서 아이템을 클릭하고 DetailActivity -> SearchResultActivity로 넘어 왔을 경우
        notifyItemChanged(position);
    }

    class TourResultVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumbIV;
        private TextView nameTV;
        private TextView reviewTV;
        private TextView markTV;

        TourResultVH(View itemView) {
            super(itemView);
            thumbIV = itemView.findViewById(R.id.thumb);
            nameTV = itemView.findViewById(R.id.name);
            reviewTV = itemView.findViewById(R.id.reviewTV);
            markTV = itemView.findViewById(R.id.markTV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (checkRun) {

                checkRun = false;

                int contentid = areaBaseItems.get(getAdapterPosition()).contentid;
                Intent intent = DetailActivity.newIntent(context, contentid);
                context.startActivity(intent);
                ((SelectAreaResultActivity)context).setPositionData(getAdapterPosition(), contentid);
                checkRun = true;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TourResultVH(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_datatype1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TourResultVH tourResultVH = (TourResultVH) holder;

        if (areaBaseItems.get(position).firstimage != null) {
            Glide.with(context).load(areaBaseItems.get(position).firstimage).into(tourResultVH.thumbIV);
        } else {
            Glide.with(context).load(R.drawable.no_image).into(tourResultVH.thumbIV);
        }

        tourResultVH.nameTV.setText(areaBaseItems.get(position).title);

        tourResultVH.reviewTV.setText(String.valueOf("후기 "+ tourInfoItems.get(position).review));
        tourResultVH.markTV.setText(String.valueOf("찜 " + tourInfoItems.get(position).mark_cnt));

    }

    @Override
    public int getItemCount() {
        return this.areaBaseItems.size();
    }

}
