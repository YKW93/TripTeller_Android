package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.model.mainReview.CommentItem;

import java.util.ArrayList;

public class CommentRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CommentItem> commentItems;

    public CommentRVAdapter(Context context) {
        mContext = context;
        commentItems = new ArrayList<>();
    }

    public void setData(ArrayList<CommentItem> data) {
        commentItems.clear();
        commentItems.addAll(data);
        notifyDataSetChanged();
    }

    private class CommentVH extends RecyclerView.ViewHolder {
        private ImageView user_profile;
        private TextView user_name;
        private TextView review_content;
        private TextView review_created;

        public CommentVH(View itemView) {
            super(itemView);
            user_profile = itemView.findViewById(R.id.user_profile);
            user_name = itemView.findViewById(R.id.user_name);
            review_content = itemView.findViewById(R.id.review_content);
            review_created = itemView.findViewById(R.id.review_created);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommentVH commentVH = (CommentVH) holder;

        GlideApp.with(mContext).load(commentItems.get(position).image).into(commentVH.user_profile);
        commentVH.user_name.setText(commentItems.get(position).author);
        commentVH.review_content.setText(commentItems.get(position).content);
        commentVH.review_created.setText(commentItems.get(position).created_at);

    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }
}
