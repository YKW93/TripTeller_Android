package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SearchResultActivity;
import com.example.rhkdd.yunal.model.searchKeyword.SearchKeywordItem;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;

import java.util.ArrayList;

public class SearchResultsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private ArrayList<SearchKeywordItem> searchResultLists;
    private ArrayList<TourInfoItem> tourInfoItems;

    private boolean checkRun = true;
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;

    public SearchResultsRVAdapter(Context context) {
        this.context = context;
        searchResultLists = new ArrayList<>();
        tourInfoItems = new ArrayList<>();
    }

    public void setData(ArrayList<SearchKeywordItem> data, ArrayList<TourInfoItem> data1) {
        searchResultLists.clear();
        tourInfoItems.clear();
        searchResultLists.addAll(data);
        tourInfoItems.addAll(data1);
        notifyDataSetChanged();
    }

    public void changeData(int position, TourInfoItem tourInfoItem) {
        tourInfoItems.set(position, tourInfoItem);
        notifyItemChanged(position);
    }

    private class ResultVH extends RecyclerView.ViewHolder implements View.OnClickListener {


        private ImageView thumbIV;
        private TextView nameTV;
        private TextView reviewTV;
        private TextView markTV;

        public ResultVH(View itemView) {
            super(itemView);
            thumbIV = itemView.findViewById(R.id.thumb);
            nameTV = itemView.findViewById(R.id.name);
            reviewTV = itemView.findViewById(R.id.reviewTV);
            markTV = itemView.findViewById(R.id.markTV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                default: // 리스트 아이템 클릭했을 경우
                    if (checkRun) {

                        checkRun = false;

                        int contentid = searchResultLists.get(getAdapterPosition()).contentid;
                        Intent intent = DetailActivity.newIntent(context, contentid);
                        context.startActivity(intent);
                        ((SearchResultActivity)context).setPositionData(getAdapterPosition(), contentid);
                        checkRun = true;
                    }
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultVH(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_datatype1, parent,false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)  {

        if (position >= getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        ResultVH resultVH = (ResultVH) holder;
        if (searchResultLists.get(position).firstimage != null) {
            Glide.with(context).load(searchResultLists.get(position).firstimage).into(resultVH.thumbIV);
        } else {
            Glide.with(context).load(R.drawable.no_image).into(resultVH.thumbIV);
        }
        resultVH.nameTV.setText(searchResultLists.get(position).title);

        resultVH.reviewTV.setText(String.valueOf("후기 "+ tourInfoItems.get(position).review));
        resultVH.markTV.setText(String.valueOf("찜 " + tourInfoItems.get(position).mark_cnt));
    }

    @Override
    public int getItemCount() {
        return searchResultLists.size();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
