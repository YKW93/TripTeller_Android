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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.data.detailImage.DetailImageItem;

import java.util.ArrayList;

import static com.example.rhkdd.yunal.DetailActivity.DETAIL_IMAGES;


/**
 * Created by rhkdd on 2018-03-27.
 */

public class DetailImageActivity extends AppCompatActivity {

    RecyclerView DetailImageRV;
    DetailImageAdapter detailImageAdapter;

    public static Intent newIntent(Context context, ArrayList<DetailImageItem> detailImageItems) {
        Intent intent = new Intent(context, DetailImageActivity.class);
        intent.putExtra(DETAIL_IMAGES, detailImageItems);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailimage);

        TextView textView = findViewById(R.id.noImageTV);
        TextView totalImageCount = findViewById(R.id.image_total_count);


        //툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        // intent 값 가져오기
        ArrayList<DetailImageItem> detailImageItems = (ArrayList<DetailImageItem>) getIntent().getSerializableExtra(DETAIL_IMAGES);

        // recycler 셋팅 및 초기화
        if (detailImageItems != null && !detailImageItems.get(0).originimgurl.equals(String.valueOf(2131165339))) { // 2131165339 => R.drawable.no_image 값
            Log.d("test14", detailImageItems.get(0).originimgurl);
            DetailImageRV = findViewById(R.id.imageRV);
            DetailImageRV.setLayoutManager(new GridLayoutManager(DetailImageActivity.this, 3));
            DetailImageAdapter detailImageAdapter = new DetailImageAdapter(DetailImageActivity.this);
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

    private class DetailImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;
        private ArrayList<DetailImageItem> detailImageItems;

        public DetailImageAdapter(Context context) {
            mContext = context;
            detailImageItems = new ArrayList<>();
        }

        public void setDatas(ArrayList<DetailImageItem> lists) {
            detailImageItems.addAll(lists);
        }

        private class DetailImagesVH extends RecyclerView.ViewHolder implements View.OnClickListener{

            private ImageView detailImage;

            public DetailImagesVH(View itemView) {
                super(itemView);
                detailImage = itemView.findViewById(R.id.detailImage);
                detailImage.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = DetailImageSliderActivity.newIntent(DetailImageActivity.this, detailImageItems, getAdapterPosition(), 1);
                startActivity(intent);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DetailImagesVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_detailimage, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            DetailImagesVH detailImagesVH = (DetailImagesVH) holder;
            GlideApp.with(mContext).load(detailImageItems.get(position).originimgurl).into(detailImagesVH.detailImage);
        }

        @Override
        public int getItemCount() {
            return detailImageItems.size();
        }
    }
}
