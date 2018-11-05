package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.userResponseResult.TopTellerResponseResult;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopTellerRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TopTellerResponseResult> topTellerResponseResults;

    public TopTellerRVAdapter(Context context) {
        mContext = context;
        topTellerResponseResults = new ArrayList<>();
    }

    public void setData(ArrayList<TopTellerResponseResult> data) {
        topTellerResponseResults.clear();
        topTellerResponseResults.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (topTellerResponseResults.get(position).ranking < 4) { // 해당 유저 랭킹이 4등미만일 경우
            return 0;
        } else {
            return 1;
        }
    }

    private class TopTellerType1VH extends RecyclerView.ViewHolder {
        private ImageView rank_image;
        private CircleImageView user_profile;
        private TextView user_name;

        public TopTellerType1VH(View itemView) {
            super(itemView);
            rank_image = itemView.findViewById(R.id.rankImage);
            user_profile = itemView.findViewById(R.id.user_profile);
            user_name = itemView.findViewById(R.id.user_name);
        }
    }

    private class TopTellerType2VH extends RecyclerView.ViewHolder {
        private TextView rank_num;
        private CircleImageView user_profile;
        private TextView user_name;

        public TopTellerType2VH(View itemView) {
            super(itemView);
            rank_num = itemView.findViewById(R.id.rank_num);
            user_profile = itemView.findViewById(R.id.user_profile);
            user_name = itemView.findViewById(R.id.user_name);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType ==0) {
            return new TopTellerType1VH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_topteller_type1, parent, false));
        } else {
            return new TopTellerType2VH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_topteller_type2, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TopTellerType1VH) {

            TopTellerType1VH topTellerType1VH = (TopTellerType1VH) holder;

            if (topTellerResponseResults.get(position).ranking == 1) {
                topTellerType1VH.rank_image.setImageResource(R.drawable.ic_rank_1);
            } else if (topTellerResponseResults.get(position).ranking == 2) {
                topTellerType1VH.rank_image.setImageResource(R.drawable.ic_rank_2);
            } else {
                topTellerType1VH.rank_image.setImageResource(R.drawable.ic_rank_3);
            }

            GlideApp.with(mContext).load(topTellerResponseResults.get(position).image).into(topTellerType1VH.user_profile);

            topTellerType1VH.user_name.setText(topTellerResponseResults.get(position).nickname);

        } else {

            TopTellerType2VH topTellerType2VH = (TopTellerType2VH) holder;

            topTellerType2VH.rank_num.setText(String.valueOf(topTellerResponseResults.get(position).ranking));
            GlideApp.with(mContext).load(topTellerResponseResults.get(position).image).into(topTellerType2VH.user_profile);
            topTellerType2VH.user_name.setText(topTellerResponseResults.get(position).nickname);

        }
    }

    @Override
    public int getItemCount() {
        return topTellerResponseResults.size();
    }
}
