package com.example.rhkdd.yunal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.rhkdd.yunal.adapter.TopTellerRVAdapter;
import com.example.rhkdd.yunal.adapter.TotalReviewRVAdapter;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.RetrofitTourClient;
import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.model.userResponseResult.TopTellerResponseResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopTellerActivity extends AppCompatActivity {

    private TopTellerRVAdapter topTellerRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_teller);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(TopTellerActivity.this, getResources().getColor(R.color.status_color));

        Initialize();
        loadTopTellerData();
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
        topTellerRVAdapter = new TopTellerRVAdapter(this);
        recyclerView.setAdapter(topTellerRVAdapter);
    }

    public void loadTopTellerData() {

        Call<ArrayList<TopTellerResponseResult>> call = RetrofitServerClient.getInstance().getService().topTellerResponseResult();
        call.enqueue(new Callback<ArrayList<TopTellerResponseResult>>() {
            @Override
            public void onResponse(Call<ArrayList<TopTellerResponseResult>> call, Response<ArrayList<TopTellerResponseResult>> response) {
                if (response.isSuccessful()) {
                    ArrayList<TopTellerResponseResult> topTellerResponseResults = response.body();
                    if (!topTellerResponseResults.isEmpty()) {
                        topTellerRVAdapter.setData(topTellerResponseResults);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TopTellerResponseResult>> call, Throwable t) {

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
