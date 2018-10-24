package com.example.rhkdd.yunal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rhkdd.yunal.adapter.CurrentLocationVPAdapter;
import com.example.rhkdd.yunal.common.RetrofitTourClient;
import com.example.rhkdd.yunal.model.locationBased.LocationBased;
import com.example.rhkdd.yunal.model.locationBased.LocationBasedItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.SearchActivity.API_key;

public class CurrentLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LinearLayout warningMsg;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LocationManager locationManager;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private ArrayList<LocationBasedItem> locationBasedItems;
    private ArrayList<MarkerOptions> markerOptions; // 저장된 마커들 좌표값

    private ViewPager viewPager;
    private CurrentLocationVPAdapter currentLocationVPAdapter;

    private double latitude;
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        Initialize();
    }

    private void Initialize() {

        // 툴바 초기화 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        // viewpager 셋팅
        viewPager = findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);
        int padding_dp = 15;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dp = Math.round(padding_dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        viewPager.setPadding(dp, 0, dp, 0);


        currentLocationVPAdapter = new CurrentLocationVPAdapter(CurrentLocationActivity.this);
        viewPager.setAdapter(currentLocationVPAdapter);
        viewPager.setVisibility(View.GONE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                moveCamera(markerOptions.get(position)); // 마커 위치로 카메라 이동
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        warningMsg = findViewById(R.id.locationWarnnigMsg);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // 맵이 사용할 준비가 되면 onMapReady 함수를 자동으로 호출된다
        mapFragment.getMapAsync(this);

        // 위치 관리자 객체 가져온다.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 위치 허가 등록
        checkPermission();

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { // gps 켜짐 꺼짐 브로드캐스트 리시버 등록
                GpsOnOffCheck();
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

    }

    private void checkPermission() {
        TedPermission.with(CurrentLocationActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() { // 위치 permission 허용
            buildLocationRequest(); // locationrequest 설정
            buildLocationCallBack(); // 위치 관련 콜백 등록
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(CurrentLocationActivity.this);

            GpsOnOffCheck(); // gps on off 체크
            mMap.setMyLocationEnabled(true); // 현재위치로 가는 버튼 생성
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) { // 위치 permission 불가
//            Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
            mMap.setMyLocationEnabled(false);
        }
    };

    // 현재 위치 값 가져오는 메소드
    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) { // 사용자 현재 위치값을 구했을 경우에 호출되는 메소드(가끔 늦어지는 경우 발생)
                super.onLocationAvailability(locationAvailability);
                loadData(longitude, latitude);

                LatLng dataLocation = new LatLng( latitude ,  longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(dataLocation).zoom(13).build(); // 카메라 현재 위치로 셋팅
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); // 애니메이션 적용, 위치 이동

            }

            @Override
            public void onLocationResult(LocationResult locationResult) { // 사용자 현재 위치값을 반환하는 메소드 (주기적으로 호출됨)
                for (Location location : locationResult.getLocations()) {
                    LatLng dataLocation = new LatLng( location.getLatitude(),  location.getLongitude());

                    longitude = dataLocation.longitude; // 경도
                    latitude = dataLocation.latitude; // 위도
                }
            }
        };
    }

    private void setMarker(ArrayList<LocationBasedItem> lists) {

        markerOptions = new ArrayList<>();

        for (int i = 0; i < lists.size(); i++) {
            LatLng dataLocation = new LatLng(lists.get(i).mapy, lists.get(i).mapx);
            MarkerOptions markers = new MarkerOptions();
            markers.position(dataLocation).title(lists.get(i).title);
            markerOptions.add(i, markers);
            mMap.addMarker((markers));
        }

    }

    private void loadData(final double longitude, final double latitude) {
        locationBasedItems = new ArrayList<>();

        Call<LocationBased> call = RetrofitTourClient.getInstance().getService(null).locationBased(API_key, "yunal", "AND", "json",
                longitude, latitude, 3000, "O", 1, 500);
        call.enqueue(new Callback<LocationBased>() {
            @Override
            public void onResponse(Call<LocationBased> call, Response<LocationBased> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LocationBased locationBased = response.body();
                    if (locationBased != null) {
                        locationBasedItems.addAll(locationBased.response.body.items.item);
                        setMarker(locationBasedItems);
                        currentLocationVPAdapter.setData(locationBasedItems);
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationBased> call, Throwable t) {
//                Toasty.error(CurrentLocationActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // 해당 시간마다 위치 업데이트 단, 위치 변화 이벤트에 따라 이보다 더빠르게 느리게 위치를 수집할 수 있음.
        locationRequest.setFastestInterval(3000); // 해당 시간마다 위치를 빠르게 업데이트 하지 않는다. (전력 소모를 줄일 수 있다.)
        locationRequest.setSmallestDisplacement(10);
    }

    @SuppressLint("MissingPermission")
    public void GpsOnOffCheck() {
        //GPS가 켜져있는지 체크
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){ // gps가 꺼져 있을 경우
            warningMsg.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(CurrentLocationActivity.this);
            builder.setTitle("위치 서비스 사용중지");
            builder.setMessage("기기의 위치 설정이 꺼져있습니다.\n" +
                    "위치 설정을 켜고 다시 시도해 주세요.");
            builder.setPositiveButton("위치 설정 가기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //GPS 설정화면으로 이동
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        } else { // gps 켜져있을 경우
            warningMsg.setVisibility(View.GONE);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() { // 구글 현재위치 찾아주는 버튼 리스너
            @Override
            public boolean onMyLocationButtonClick() {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // gps 꺼짐
                    Toasty.error(CurrentLocationActivity.this, "위치 서비스를 켜주세요.", Toast.LENGTH_LONG).show();
                } else { // gps 켜짐
                    if (longitude != 0 && latitude != 0) {
                        loadData(longitude, latitude); // 위치에 대한 관광 정보 로드

                    } else {
                        Toasty.warning(CurrentLocationActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        // 마커 클릭 이벤트
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < markerOptions.size(); i++) {
                    if (marker.getTitle().equals(markerOptions.get(i).getTitle())) {
                        viewPager.setVisibility(View.VISIBLE);
                        viewPager.setCurrentItem(i); // 해당 마커와 동일한 viewpager 이동
                        break;
                    }

                }
                return false;
            }
        });

        // 디폴트 위치
        LatLng dataLocation = new LatLng( 37.5512645 ,  126.9553756);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(dataLocation).zoom(12).build(); // 카메라 현재 위치로 셋팅
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); // 애니메이션 적용, 위치 이동
   }

   private void moveCamera(MarkerOptions markerOptions) {

       mMap.addMarker(markerOptions).showInfoWindow(); // 위치한 마커 클릭
       CameraPosition cameraPosition = new CameraPosition.Builder().target(markerOptions.getPosition()).zoom(14).build(); // 카메라 셋팅
       mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); // 애니메이션 적용, 위치 이동

   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
