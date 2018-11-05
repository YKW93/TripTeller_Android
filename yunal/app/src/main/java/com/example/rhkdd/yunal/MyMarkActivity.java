package com.example.rhkdd.yunal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.rhkdd.yunal.adapter.MyMarkDataRVAdapter;
import com.example.rhkdd.yunal.common.LoadingScreenHelper;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.RetrofitTourClient;
import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.common.UserInfoReturn;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommon;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommonItem;
import com.example.rhkdd.yunal.model.userResponseResult.MyMarkResponseResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.common.RetrofitTourClient.API_key;

public class MyMarkActivity extends AppCompatActivity {

    private String email_id;

    private MyMarkDataRVAdapter myMarkDataRVAdapter;
    private ArrayList<DetailCommonItem> detailCommonItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mark);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(MyMarkActivity.this, getResources().getColor(R.color.status_color));


        // 휴대폰 내에 저장된 사용자 email 값 가져오기
        UserInfoReturn.getInstance().getUserNicname(MyMarkActivity.this);

        Initialize();
        loadMyMarkData();
    }

    private void Initialize() {

        email_id = UserInfoReturn.getInstance().getUserNicname(MyMarkActivity.this);

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        // recyclerview 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myMarkDataRVAdapter = new MyMarkDataRVAdapter(this);
        recyclerView.setAdapter(myMarkDataRVAdapter);

    }

    private void loadMyMarkData() {

        Call<ArrayList<MyMarkResponseResult>> call = RetrofitServerClient.getInstance().getService().MyMarkResponseResult(email_id);
        Log.d("test167", String.valueOf(call.request().url()));
        call.enqueue(new Callback<ArrayList<MyMarkResponseResult>>() {
            @Override
            public void onResponse(Call<ArrayList<MyMarkResponseResult>> call, Response<ArrayList<MyMarkResponseResult>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MyMarkResponseResult> myMarkResponseResults = response.body();
                    if (!myMarkResponseResults.isEmpty()) {
                        loadTourData(myMarkResponseResults);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyMarkResponseResult>> call, Throwable t) {

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void loadTourData(final ArrayList<MyMarkResponseResult> lists) {
        LoadingScreenHelper.getInstance().progressON(MyMarkActivity.this);

        detailCommonItems = new ArrayList<>();

        for (int i = 0; i < lists.size(); i++) {
            final Call<DetailCommon> call = RetrofitTourClient.getInstance().getService(null).detailCommon(API_key, "yunal", "AND", "json", lists.get(i).content_id,
                    "Y", "N", "N", "N","Y", "N");
            final int finalI = i;
            call.enqueue(new Callback<DetailCommon>() {
                @Override
                public void onResponse(Call<DetailCommon> call, Response<DetailCommon> response) {
                    if (response.isSuccessful()) {
                        DetailCommon detailCommon = response.body();
                        if (detailCommon != null) {
                            detailCommonItems.add(detailCommon.response.body.items.item);
                            if (finalI == lists.size() -1) { // 반복문이 전부 돌았을 때 어댑터에 데이터 셋팅
                                // 데이터를 받아오는 시간을 주기 위해 2초 기다림
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        myMarkDataRVAdapter.setData(detailCommonItems);
                                        LoadingScreenHelper.getInstance().progressOFF();

                                    }
                                }, 2000);

                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<DetailCommon> call, Throwable t) {

                }
            });

        }
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
