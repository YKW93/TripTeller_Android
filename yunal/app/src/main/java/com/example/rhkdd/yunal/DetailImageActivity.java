package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.rhkdd.yunal.adapter.TotalDetailImageRVAdapter;
import com.example.rhkdd.yunal.model.detailImage.DetailImageItem;

import java.util.ArrayList;

import static com.example.rhkdd.yunal.DetailActivity.DETAIL_IMAGES;


/**
 * Created by rhkdd on 2018-03-27.
 */

public class DetailImageActivity extends AppCompatActivity {

    RecyclerView DetailImageRV;
    TotalDetailImageRVAdapter detailImageAdapter;

    public static Intent newIntent(Context context, ArrayList<DetailImageItem> detailImageItems) {
        Intent intent = new Intent(context, DetailImageActivity.class);
        intent.putExtra(DETAIL_IMAGES, detailImageItems);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailimage);

        Initialize();
    }

    private void Initialize() {

        // intent 값 가져오기
        ArrayList<DetailImageItem> detailImageItems = (ArrayList<DetailImageItem>) getIntent().getSerializableExtra(DETAIL_IMAGES);

        TextView textView = findViewById(R.id.noImageTV);
        TextView totalImageCount = findViewById(R.id.image_total_count);


        //툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);


        // recycler 셋팅 및 초기화
        if (detailImageItems != null && !detailImageItems.get(0).originimgurl.equals(String.valueOf(2131165339))) { // 2131165339 => R.drawable.no_image 값
            DetailImageRV = findViewById(R.id.imageRV);
            DetailImageRV.setLayoutManager(new GridLayoutManager(DetailImageActivity.this, 3));
            TotalDetailImageRVAdapter detailImageAdapter = new TotalDetailImageRVAdapter(DetailImageActivity.this);
            detailImageAdapter.setDatas(detailImageItems);
            DetailImageRV.setAdapter(detailImageAdapter);

            totalImageCount.setVisibility(View.VISIBLE);

            totalImageCount.setText(String.valueOf(detailImageItems.size()));
            textView.setVisibility(View.GONE);

        } else {
            totalImageCount.setVisibility(View.GONE);

            textView.setVisibility(View.VISIBLE);
            textView.setText("이미지가 없습니다.");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
