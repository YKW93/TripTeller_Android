package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SelectAreaActivity;
import com.example.rhkdd.yunal.data.areaCode.AreaCodeItem;

import java.util.ArrayList;

public class AreaRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AreaCodeItem> lists;
    private int selectPosition;
    private Boolean onBind; //
    private Boolean isStart; // 처음 실행 했는지 판별 변수
    private OnLoadAreaCodeListener listener;

    public AreaRVAdapter(Context context) {
        this.context = context;
        lists = new ArrayList<>();
        selectPosition = -1;
        isStart = true;

    }

    public void setData(ArrayList<AreaCodeItem> lists) {
        this.lists.clear();
        this.lists.addAll(lists);
        notifyDataSetChanged();

    }

    public String getAreaCode() {
        return this.lists.get(selectPosition).code;
    }

    public String getAreaName() {
        return this.lists.get(selectPosition).name;
    }

    public class AreaVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView localName;
        public RelativeLayout backgournd_layout;

        AreaVH(View itemView) {
            super(itemView);
            localName = itemView.findViewById(R.id.localName);
            backgournd_layout = itemView.findViewById(R.id.background_layout);

            backgournd_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                default:
                    selectPosition = getAdapterPosition();
                    if (!onBind) {
                        notifyDataSetChanged();
                    }
                    listener.onLoad(lists.get(selectPosition).code);
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AreaVH(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_area, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AreaVH areaVH = (AreaVH) holder;

        areaVH.localName.setText(lists.get(position).name);

        if (position == 0 && isStart) {
            onBind = true;
            ((AreaVH) holder).backgournd_layout.performClick();
            isStart = false;
        }
        onBind = false;


        if (selectPosition == position) {
            areaVH.localName.setTextColor(Color.parseColor("#000000"));
            areaVH.backgournd_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        } else {
            areaVH.localName.setTextColor(Color.parseColor("#c5c5c7"));
            areaVH.backgournd_layout.setBackgroundColor(Color.parseColor("#F8F8FA"));
        }

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public interface OnLoadAreaCodeListener {
        void onLoad(String areaCode);
    }

    public void setOnLoadAreaCodeListener(OnLoadAreaCodeListener onLoadAreaCodeListener) {
        listener = onLoadAreaCodeListener;
    }
}