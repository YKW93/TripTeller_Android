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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rhkdd.yunal.adapter.CurrentLocationVPAdapter;
import com.example.rhkdd.yunal.common.RetrofitClient;
import com.example.rhkdd.yunal.data.locationBased.LocationBased;
import com.example.rhkdd.yunal.data.locationBased.LocationBasedItem;
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
import com.google.android.gms.maps.model.LatLng;
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

    LinearLayout warningMsg;

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    LocationManager locationManager;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    ArrayList<LocationBasedItem> locationBasedItems;

    CurrentLocationVPAdapter currentLocationVPAdapter;

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

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(6,0,6,0);
        viewPager.setPageMargin(-5);
        currentLocationVPAdapter = new CurrentLocationVPAdapter(CurrentLocationActivity.this);
        viewPager.setAdapter(currentLocationVPAdapter);

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
            buildLocationCallBack(); // 위치 찾는 콜백 등록
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dataLocation,13)); // 카메라 현재 위치로 이동
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

        Log.d("test12341234", "사이즈 : " + String.valueOf(lists.size()));
        for (int i = 0; i < lists.size(); i++) {
            LatLng dataLocation = new LatLng(lists.get(i).mapy, lists.get(i).mapx);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(dataLocation).title(lists.get(i).title);
            mMap.addMarker((markerOptions));
        }

    }

    private void loadData(final double longitude, final double latitude) {
        Call<LocationBased> call = RetrofitClient.getInstance().getService(null).locationBased(API_key, "yunal", "AND", "json",
                longitude, latitude, 3000, "O", 1, 10);
        Log.d("test15", String.valueOf(call.request().url()));
        call.enqueue(new Callback<LocationBased>() {
            @Override
            public void onResponse(Call<LocationBased> call, Response<LocationBased> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LocationBased locationBased = response.body();
                    if (locationBased != null) {
                        locationBasedItems = new ArrayList<>();
                        int totalCount = locationBased.response.body.totalCount;
                        loadMoreData(totalCount, longitude, latitude);
                    }

                }
            }

            @Override
            public void onFailure(Call<LocationBased> call, Throwable t) {
//                Toasty.error(CurrentLocationActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadMoreData(final int totalCount, double longitude, double latitude) {

        int count = totalCount / 10;

        for (int page = 1; page <= count + 1; page++) {

            Call<LocationBased> call = RetrofitClient.getInstance().getService(null).locationBased(API_key, "yunal", "AND", "json",
                    longitude, latitude, 3000, "O", page, 10);
            Log.d("test15", String.valueOf(call.request().url()));
            call.enqueue(new Callback<LocationBased>() {
                @Override
                public void onResponse(Call<LocationBased> call, Response<LocationBased> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LocationBased locationBased = response.body();
                        if (locationBased != null) {
//                            locationBasedItems.clear();
                            locationBasedItems.addAll(locationBased.response.body.items.item);
                            Log.d("test15", locationBasedItems.get(0).title);
                            currentLocationVPAdapter.setData(locationBasedItems);
                            setMarker(locationBasedItems);

                            if (totalCount == locationBasedItems.size()) {
                                setMarker(locationBasedItems);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LocationBased> call, Throwable t) {
                }
            });
        }

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
                        loadData(longitude, latitude);

                    } else {
                        Toasty.warning(CurrentLocationActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });


        // 해당 디폴트 위치설정
        LatLng dataLocation = new LatLng(37.568477, 126.981106);

        // 카메라를 해당 위치로 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dataLocation,15));

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
