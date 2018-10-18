package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.rhkdd.yunal.adapter.TotalFestivalRVAdapter;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestivalItem;

import java.util.ArrayList;

public class TotalFestivalActivity extends AppCompatActivity {

    private static final String FESTIVAL_DATAS = "FESTIVAL_DATAS";

    TotalFestivalRVAdapter totalFestivalRVAdapter;

    public static Intent newIntent(Context context, ArrayList<SearchFestivalItem> lists) {
        Intent intent = new Intent(context, TotalFestivalActivity.class);
        intent.putExtra(FESTIVAL_DATAS, lists);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_festival);

        Initialize();


    }

    private void Initialize() {

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        //recyclerview 셋팅
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(TotalFestivalActivity.this, 2));
        totalFestivalRVAdapter = new TotalFestivalRVAdapter(TotalFestivalActivity.this);
        recyclerView.setAdapter(totalFestivalRVAdapter);

        //intent 받아오기
        Intent intent = getIntent();
        ArrayList<SearchFestivalItem> lists = (ArrayList<SearchFestivalItem>) intent.getSerializableExtra(FESTIVAL_DATAS);
        totalFestivalRVAdapter.setData(lists);
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
