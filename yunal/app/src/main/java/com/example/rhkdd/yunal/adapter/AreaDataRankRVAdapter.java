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
import com.example.rhkdd.yunal.data.areaBase.AreaBaseItem;

import java.util.ArrayList;

public class AreaDataRankRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AreaBaseItem> lists;
    private Boolean checkRun = true;

    public AreaDataRankRVAdapter(Context context) {
        this.context = context;
        lists = new ArrayList<>();
    }

    public void setData(ArrayList<AreaBaseItem> datas) {
        lists.clear();
        lists.addAll(datas);
        notifyDataSetChanged();
    }

    private class AreaDataRankVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumbIV;
        private TextView nameTV;

        AreaDataRankVH(View itemView) {
            super(itemView);
            thumbIV = itemView.findViewById(R.id.thumb);
            nameTV = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (checkRun) {

                checkRun = false;

                int contentid = lists.get(getAdapterPosition()).contentid;
                Intent intent = DetailActivity.newIntent(context, contentid);
                context.startActivity(intent);

                checkRun = true;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AreaDataRankVH(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_datatype2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AreaDataRankVH areaDataRankVH = (AreaDataRankVH) holder;

        if (lists.get(position).firstimage != null) {
            Glide.with(context).load(lists.get(position).firstimage).into(areaDataRankVH.thumbIV);
        } else {
            Glide.with(context).load(R.drawable.no_image).into(areaDataRankVH.thumbIV);
        }

        areaDataRankVH.nameTV.setText(lists.get(position).title);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}