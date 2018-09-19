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

import com.bumptech.glide.Glide;
import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.model.areaBase.AreaBaseItem;

import java.util.ArrayList;

public class TourResultRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AreaBaseItem> list;
    private Boolean checkRun = true;

    public TourResultRVAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setData(ArrayList<AreaBaseItem> data) {
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    class TourResultVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumbIV;
        private TextView nameTV;

        TourResultVH(View itemView) {
            super(itemView);
            thumbIV = itemView.findViewById(R.id.thumb);
            nameTV = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (checkRun) {

                checkRun = false;

                int contentid = list.get(getAdapterPosition()).contentid;
                Intent intent = DetailActivity.newIntent(context, contentid);
                context.startActivity(intent);

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

        if (list.get(position).firstimage != null) {
            Glide.with(context).load(list.get(position).firstimage).into(tourResultVH.thumbIV);
        } else {
            Glide.with(context).load(R.drawable.no_image).into(tourResultVH.thumbIV);
        }

        tourResultVH.nameTV.setText(list.get(position).title);

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

}
