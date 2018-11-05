package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.rhkdd.yunal.adapter.TotalReviewRVAdapter;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.UserInfoReturn;
import com.example.rhkdd.yunal.model.tourDetail.TourReviewItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.DetailActivity.CONTENT_ID;

public class TotalReviewActivity extends AppCompatActivity {

    private static final String TAG = TotalReviewActivity.class.getSimpleName();
    private ArrayList<TourReviewItem> tourReviewItems;
    private int contentId;
    private String email_id;
    private TotalReviewRVAdapter totalReviewRVAdapter;

    public static Intent newIntent(Context context, int contentid) {
        Intent intent = new Intent(context, TotalReviewActivity.class);
        intent.putExtra(CONTENT_ID, contentid);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_review);

        Initialize();
        loadTourReviewListData(); // 리뷰 전체 불러오기

    }

    private void Initialize() {

        email_id = UserInfoReturn.getInstance().getUserNicname(TotalReviewActivity.this);

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        // 인텐트값 가져오기
        Intent intent = getIntent();
        contentId = intent.getIntExtra(CONTENT_ID, 0);

        //recyclerview 셋팅
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalReviewRVAdapter = new TotalReviewRVAdapter(TotalReviewActivity.this);
        recyclerView.setAdapter(totalReviewRVAdapter);

    }

    // 해당 관광지 리뷰 리스트 가져오기
    private void loadTourReviewListData() {

        Call<ArrayList<TourReviewItem>> call = RetrofitServerClient.getInstance().getService().TourReviewResponseResult(contentId, email_id);
        call.enqueue(new Callback<ArrayList<TourReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<TourReviewItem>> call, Response<ArrayList<TourReviewItem>> response) {
                if (response.isSuccessful()) {
                    tourReviewItems = response.body();
                    if (tourReviewItems != null && !tourReviewItems.isEmpty()) { // 관광지 리뷰데이터가 있을때
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
