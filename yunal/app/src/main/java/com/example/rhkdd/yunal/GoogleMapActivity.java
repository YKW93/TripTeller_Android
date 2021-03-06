package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommonItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.rhkdd.yunal.DetailActivity.DETAIL_COMMON;
import static com.example.rhkdd.yunal.DetailActivity.MAIN_IMAGE;

/**
 * Created by rhkdd on 2018-04-01.
 */

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DetailCommonItem detailCommonItem;
    private String firstImage;

    private GoogleMap mMap;
    private LatLng DataLocation;

    private CardView info_cardview;
    private ImageView map_image;
    private TextView map_title;
    private TextView map_contentType;
    private TextView map_location;

    private String contentType;

    public static Intent newIntent(Context context, DetailCommonItem detailCommonItem, String mainImage) {

        Intent intent = new Intent(context, GoogleMapActivity.class);
        intent.putExtra(DETAIL_COMMON, detailCommonItem);
        intent.putExtra(MAIN_IMAGE, mainImage);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(GoogleMapActivity.this, getResources().getColor(R.color.status_color));

        Initialize();

    }

    private void Initialize() {

        //툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        info_cardview = findViewById(R.id.info_cardview);
        map_image = findViewById(R.id.map_image);
        map_title = findViewById(R.id.map_title);
        map_contentType = findViewById(R.id.map_contentType);
        map_location = findViewById(R.id.map_location);

        //intent 정보 받아오기
        Intent intent = getIntent();
        detailCommonItem = (DetailCommonItem) intent.getSerializableExtra(DETAIL_COMMON);
        firstImage = intent.getStringExtra(MAIN_IMAGE);



        // 구글맵 부분
        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // getMapAsync()를 통해 onMapReady()가 자동 호출

        // 해당 지역 정보창 데이터 셋팅
        if (firstImage != null) {
            GlideApp.with(GoogleMapActivity.this).load(firstImage).into(map_image);
        } else {
            map_image.setImageResource(R.drawable.no_image);
        }

        map_title.setText(detailCommonItem.title);
        String addr = detailCommonItem.addr1 + detailCommonItem.addr2;
        map_location.setText(addr);

        info_cardview.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LatLng latLng = new LatLng(DataLocation.latitude, DataLocation.longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build(); // 카메라 이동 애니메이션
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 해당 데이터 위치설정
        DataLocation = new LatLng(detailCommonItem.mapy, detailCommonItem.mapx);

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DataLocation).title(detailCommonItem.title);

        //관광지 타입 구분 메소드 호출
        contentTypeSetting(detailCommonItem.contenttypeid, markerOptions);

        // 마커 생성
        mMap.addMarker(markerOptions);

        // 카메라를 해당 위치로 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DataLocation,15));

    }

    public void contentTypeSetting(int typeid, MarkerOptions markerOptions) {

        if (typeid == 12) { // 관광지
            contentType = "(관광지)";
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_travel_mark));

        } else if (typeid == 14) { // 문화시설
            contentType = "(문화시설)";
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_culture_mark));

        } else if (typeid == 15) { // 행사/공연/축제
            contentType = "(행사/공연/축제)";
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_festival_mark));

        } else if (typeid == 28) { // 레포츠
            contentType = "(레포츠)";
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_leisure_mark));

        } else if (typeid == 32) { // 숙박
            contentType = "(숙박)";
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lodgment_mark));

        } else if (typeid == 38) { // 쇼핑
            contentType = "(쇼핑)";
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shopping_mark));

        } else if (typeid == 39) { // 음식점
            contentType = "(음식점)";
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_food_mark));
        }

        map_contentType.setText(contentType);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
