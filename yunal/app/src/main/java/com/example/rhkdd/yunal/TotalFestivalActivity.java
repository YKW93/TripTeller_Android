package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.rhkdd.yunal.adapter.TotalFestivalRVAdapter;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestivalItem;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalFestivalActivity extends AppCompatActivity {

    private static final String FESTIVAL_DATAS = "FESTIVAL_DATAS";

    private String email_id;

    private TotalFestivalRVAdapter totalFestivalRVAdapter;

    private ArrayList<Integer> contentIdList;

    private int listPositionData = 0;
    private int listPositionContentId;

    public static Intent newIntent(Context context, ArrayList<SearchFestivalItem> lists) {
        Intent intent = new Intent(context, TotalFestivalActivity.class);
        intent.putExtra(FESTIVAL_DATAS, lists);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_festival);

        // 휴대폰 내에 저장된 사용자 email 값 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("TripTeller", MODE_PRIVATE);
        email_id = sharedPreferences.getString("userId", "이메일 정보 없음");

        Initialize();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadSingleData(listPositionContentId);
    }

    public void setPositionData(int position, int contentId) {
        listPositionData = position;
        listPositionContentId = contentId;
    }

    private void Initialize() {

        contentIdList = new ArrayList<>();

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        //recyclerview 셋팅
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(TotalFestivalActivity.this, 2));
        totalFestivalRVAdapter = new TotalFestivalRVAdapter(TotalFestivalActivity.this);
        recyclerView.setAdapter(totalFestivalRVAdapter);

        //intent 받아오기
        Intent intent = getIntent();
        ArrayList<SearchFestivalItem> lists = (ArrayList<SearchFestivalItem>) intent.getSerializableExtra(FESTIVAL_DATAS);
        loadData(lists);
//        for (int i = 0 ; i < lists.size(); i++) {
//            Log.d("testtt1414", lists.get(i).title + " , " + lists.get(i).contentid);
//        }
    }

    private void loadSingleData(int contentId) { // 단일 관광지 데이터를 호출할 경우
        ArrayList<Integer> contentIdList = new ArrayList<>();
        contentIdList.add(contentId);
        Call<ArrayList<TourInfoItem>> call = RetrofitServerClient.getInstance().getService().TourInfoResponseResult(email_id, contentIdList);
        call.enqueue(new Callback<ArrayList<TourInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<TourInfoItem>> call, Response<ArrayList<TourInfoItem>> response) {
                if (response.isSuccessful()) {
                    ArrayList<TourInfoItem> tourInfoItems = response.body();
                    if (tourInfoItems != null && !tourInfoItems.isEmpty()) {
                        totalFestivalRVAdapter.changeData(listPositionData, tourInfoItems.get(0));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TourInfoItem>> call, Throwable t) {
            }
        });
    }

    private void loadData(final ArrayList<SearchFestivalItem> list) {

        for (int i = 0; i < list.size(); i++) {
            contentIdList.add(list.get(i).contentid);
        }

        Call<ArrayList<TourInfoItem>> serverCall = RetrofitServerClient.getInstance().getService().TourInfoResponseResult(email_id, contentIdList);
        serverCall.enqueue(new Callback<ArrayList<TourInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<TourInfoItem>> call, Response<ArrayList<TourInfoItem>> response) {
                if (response.isSuccessful()) {
                    ArrayList<TourInfoItem> tourInfoItems = response.body();
                    if (tourInfoItems != null && !tourInfoItems.isEmpty()) {
                        totalFestivalRVAdapter.setData(list, tourInfoItems);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TourInfoItem>> call, Throwable t) {

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
