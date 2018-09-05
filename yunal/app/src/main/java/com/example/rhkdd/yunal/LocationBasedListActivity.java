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

    private Boolean checkRun = true;

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

    private class LocationBasedRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mcontext;
        private ArrayList<LocationBasedItem> locationBasedItems;

        LocationBasedRVAdapter(Context context) {
            mcontext = context;
            locationBasedItems = new ArrayList<>();
        }

        public void setData(ArrayList<LocationBasedItem> lists) {
            locationBasedItems.clear();
            locationBasedItems.addAll(lists);
            notifyDataSetChanged();
        }

        private class LocationBasedVH extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView thumbIV;
            private TextView nameTV;
            private TextView distTV;
            public LocationBasedVH(View itemView) {
                super(itemView);
                thumbIV = itemView.findViewById(R.id.thumb);
                nameTV = itemView.findViewById(R.id.name);
                distTV = itemView.findViewById(R.id.dist);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {

                    default: // 리스트 아이템 클릭했을 경우
                        if (checkRun) {

                            checkRun = false;

                            int contentid = locationBasedItems.get(getAdapterPosition()).contentid;
                            Intent intent = DetailActivity.newIntent(LocationBasedListActivity.this, contentid);
                            startActivity(intent);
                            checkRun = true;
                        }
                }
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LocationBasedVH(LayoutInflater.from(mcontext).inflate(R.layout.item_recyclerview_locationbased, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LocationBasedVH locationBasedVH = (LocationBasedVH) holder;

            if (locationBasedItems.get(position).firstimage != null) {
                GlideApp.with(mcontext).load(locationBasedItems.get(position).firstimage).into(locationBasedVH.thumbIV);
            } else {
                GlideApp.with(mcontext).load(R.drawable.no_image).into(locationBasedVH.thumbIV);
            }
            locationBasedVH.nameTV.setText(locationBasedItems.get(position).title);
            locationBasedVH.distTV.setText(String.valueOf(locationBasedItems.get(position).dist) + "m");
        }

        @Override
        public int getItemCount() {
            return locationBasedItems.size();
        }
    }
}
