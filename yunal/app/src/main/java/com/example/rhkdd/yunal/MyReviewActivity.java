package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.rhkdd.yunal.adapter.TotalReviewRVAdapter;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;
import com.example.rhkdd.yunal.model.tourDetail.TourReviewItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReviewActivity extends AppCompatActivity {

    private String email_id;
    private TotalReviewRVAdapter totalReviewRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        // 휴대폰 내에 저장된 사용자 email 값 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("TripTeller", Context.MODE_PRIVATE);
        email_id = sharedPreferences.getString("userId", "이메일 정보 없음");

        Initialize();
        loadMyReviewData();
    }

    private void Initialize() {

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        // recyclerview 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalReviewRVAdapter = new TotalReviewRVAdapter(MyReviewActivity.this);
        recyclerView.setAdapter(totalReviewRVAdapter);

    }

    private void loadMyReviewData() {

        Call<ArrayList<TourReviewItem>> call = RetrofitServerClient.getInstance().getService().MyReviewResponseResult(email_id);

        call.enqueue(new Callback<ArrayList<TourReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<TourReviewItem>> call, Response<ArrayList<TourReviewItem>> response) {
                if (response.isSuccessful()) {
                    ArrayList<TourReviewItem> tourReviewItems = response.body();
                    if (!tourReviewItems.isEmpty()) {
                        totalReviewRVAdapter.setData(tourReviewItems);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TourReviewItem>> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
