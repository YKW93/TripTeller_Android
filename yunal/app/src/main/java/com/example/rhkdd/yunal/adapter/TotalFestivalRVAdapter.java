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
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestivalItem;

import java.util.ArrayList;

public class TotalFestivalRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<SearchFestivalItem> searchFestivalItems;

    public TotalFestivalRVAdapter(Context context) {
        this.context = context;
        searchFestivalItems = new ArrayList<>();
    }

    public void setData(ArrayList<SearchFestivalItem> lists) {
        searchFestivalItems.clear();
        searchFestivalItems.addAll(lists);
        notifyDataSetChanged();
    }

    private class TotalFestivalVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumbIV;
        private TextView titleTV;

        public TotalFestivalVH(View itemView) {
            super(itemView);
            thumbIV = itemView.findViewById(R.id.thumb);
            titleTV = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailActivity.newIntent(context, searchFestivalItems.get(getAdapterPosition()).contentid);
            context.startActivity(intent);
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
    }

    @Override
    public int getItemCount() {
        return searchFestivalItems.size();
    }
}
