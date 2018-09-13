package com.example.rhkdd.yunal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rhkdd.yunal.adapter.LocationBasedRVAdapter;
import com.example.rhkdd.yunal.adapter.SearchResultsRVAdapter;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.data.locationBased.LocationBasedItem;

import java.util.ArrayList;

import static com.example.rhkdd.yunal.DetailActivity.LOCATIONBASED_LIST_DATA;
import static com.example.rhkdd.yunal.DetailActivity.TOUR_NAME;

/**
 * Created by rhkdd on 2018-05-09.
 */

public class LocationBasedListActivity extends AppCompatActivity {


    private ArrayList<LocationBasedItem> locationBasedItems;
    private String tourName;
    private ProgressBar progressBar;

    public static Intent newIntent(Context context, ArrayList<LocationBasedItem> lists, String tourName) {
        Intent intent = new Intent(context, LocationBasedListActivity.class);
        intent.putExtra(LOCATIONBASED_LIST_DATA, lists);
        intent.putExtra(TOUR_NAME, tourName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationbasedlist);

        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        locationBasedItems = (ArrayList<LocationBasedItem>) intent.getSerializableExtra(LOCATIONBASED_LIST_DATA);
        tourName = intent.getStringExtra(TOUR_NAME);

        RecyclerView locationBasedRV = findViewById(R.id.locationBasedRV);
        locationBasedRV.setLayoutManager(new GridLayoutManager(LocationBasedListActivity.this, 2));
        LocationBasedRVAdapter locationBasedRVAdapter = new LocationBasedRVAdapter(LocationBasedListActivity.this);
        locationBasedRVAdapter.setData(locationBasedItems);
        locationBasedRV.setAdapter(locationBasedRVAdapter);

        TextView locationbasedTV = findViewById(R.id.locationbasedTV);
        locationbasedTV.setText(Html.fromHtml("2km 내 " + "<font color='#14b9d6'>" +'\''+ tourName + '\''+ "</font>" + " 주변 장소"));

        findViewById(R.id.back_btn).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_btn :
                    finish();
                    break;
            }
        }
    };
}
