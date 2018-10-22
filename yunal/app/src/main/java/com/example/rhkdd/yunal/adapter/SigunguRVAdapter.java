package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SelectAreaResultActivity;
import com.example.rhkdd.yunal.model.areaCode.AreaCodeItem;

import java.util.ArrayList;

public class SigunguRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AreaCodeItem> lists;


    public SigunguRVAdapter(Context context) {
        this.context = context;
        this.lists = new ArrayList<>();
    }

    void setData(ArrayList<AreaCodeItem> lists) {
        this.lists.clear();
        this.lists.addAll(lists);
        notifyDataSetChanged();
    }


    class SigunguVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView sigunguName;

        SigunguVH(View itemView) {
            super(itemView);

            sigunguName = itemView.findViewById(R.id.sigunguName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AreaRVAdapter areaRVAdapter = new AreaRVAdapter(context);

            String areaCode = areaRVAdapter.getAreaCode();
            String areaName = areaRVAdapter.getAreaName();
            String sigunguCode = lists.get(getAdapterPosition()).code;
            String sigunguName = lists.get(getAdapterPosition()).name;

            if (areaCode != null && areaName != null & sigunguCode != null && sigunguName != null) {
                Intent intent = SelectAreaResultActivity.newIntent(context, areaCode, sigunguCode, areaName, sigunguName);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SigunguVH(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_sigungu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SigunguVH sigunguVH = (SigunguVH) holder;

        sigunguVH.sigunguName.setText(lists.get(position).name);

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
