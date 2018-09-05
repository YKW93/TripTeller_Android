package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * Created by rhkdd on 2018-01-23.
 */

public class SearchActivity extends AppCompatActivity {

    private EditText searchEdit;
    private RecyclerView recentRV;
    public static final String API_key = "pjUTyt2nvkg4O9pu5PyjjblvqVC3JHFvTn0cqEKWVE7O0a%2BMTP68wcnQW0dEEZ6hA%2BHSYV03I1%2BRl0Wl6YhfTw%3D%3D";
    public static final String API_BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";

    private boolean checkRun = true;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        searchEdit = findViewById(R.id.search_key);
        findViewById(R.id.searchBtn).setOnClickListener(onClickListener);

        recentRV = findViewById(R.id.recent);
        recentRV.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recentRV.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL)); // 리사이클러뷰 구분선
        RecentSearchRVAdapter recentSearchRVAdapter = new RecentSearchRVAdapter(SearchActivity.this);
        recentRV.setAdapter(recentSearchRVAdapter);
        recentSearchRVAdapter.setData("서울");
        recentSearchRVAdapter.setData("경기도");
        recentSearchRVAdapter.setData("전주");
        recentSearchRVAdapter.setData("광주");
        recentSearchRVAdapter.setData("포천");
        recentSearchRVAdapter.setData("영동");
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.searchBtn:
                    final String search_key = searchEdit.getText().toString();
                    if (!search_key.isEmpty()) {
                        Intent intent = SearchResultActivity.newIntent(SearchActivity.this, search_key);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SearchActivity.this, "장소명 또는 지역명을 입력해주세요.", Toast.LENGTH_LONG).show();
                    }

            }
        }
    };

    private class RecentSearchRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<String> recentSearchesValues;
        private Context mContext;

        private RecentSearchRVAdapter(Context context) {
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
                searchEdit.setText(recentSearchesValues.get(getAdapterPosition()));
                searchEdit.setSelection(searchEdit.getText().length());
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
    }

}
