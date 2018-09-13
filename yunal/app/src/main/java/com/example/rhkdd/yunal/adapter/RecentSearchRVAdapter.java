package com.example.rhkdd.yunal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;

import java.util.ArrayList;

public class RecentSearchRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> recentSearchesValues;
    private Context mContext;
    private onDataReturnListener listener;
    private EditText searchEdit;

    public RecentSearchRVAdapter(Context context) {
        this.mContext = context;
        recentSearchesValues = new ArrayList<>();
    }

    public void setData(String value) {
        recentSearchesValues.add(value);
        notifyDataSetChanged();
    }

    private class RecentSearchVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView recentSearch;
        private TextView searchTime;
        public RecentSearchVH(View itemView) {
            super(itemView);
            recentSearch = itemView.findViewById(R.id.recentSearches);
            searchTime = itemView.findViewById(R.id.recnetTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick();
            if (searchEdit != null) {
                searchEdit.setText(recentSearchesValues.get(getAdapterPosition()));
                searchEdit.setSelection(searchEdit.getText().length());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentSearchVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_recentsearches, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecentSearchVH  recentSearchVH = (RecentSearchVH) holder;
        recentSearchVH.recentSearch.setText(recentSearchesValues.get(position));
        recentSearchVH.searchTime.setText("01-25");
    }

    @Override
    public int getItemCount() {
        return recentSearchesValues.size();
    }


    public interface onDataReturnListener {
        void onClick();
    }

    public void setOnDataReturnListener(onDataReturnListener onDataReturnListener) {
        listener = onDataReturnListener;
    }

    public void setEditText(EditText editText) {
        searchEdit = editText;
    }
}
