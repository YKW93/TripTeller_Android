package com.example.rhkdd.yunal.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.RetrofitClient;
import com.example.rhkdd.yunal.data.searchKeyword.SearchKeyword;
import com.google.android.gms.location.FusedLocationProviderClient;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.SearchActivity.API_key;

/**
 * Created by rhkdd on 2018-05-10.
 */

public class ThirdTabFragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    LocationManager locationManager;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        // 프래그먼트 안에서 프래그먼트를 가져오는 코드
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 맵이 사용할 준비가 되면 onMapReady 함수를 자동으로 호출된다
        mapFragment.getMapAsync(this);

        // 위치 관리자 객체 가져온다.
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }



    private void checkPermission() {
        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            buildLocationRequest(); // locationrequest 설정
            buildLocationCallBack(); // 위치 찾는 콜백 등록
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

            GpsOnOffCheck(); // gps on off 체크
            mMap.setMyLocationEnabled(true); // 현재위치로 가는 버튼 생성
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
            mMap.setMyLocationEnabled(false);
        }
    };

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng dataLocation = new LatLng( location.getLatitude(),  location.getLongitude());
                    //여기서 위도 경도값 넘겨주고 관광 데이터 받아오기 !

                    Call<SearchKeyword> call = RetrofitClient.getInstance().getService(null).searchKeyword(API_key,"yunal","AND","json",1,10, "서울", "O");
                    call.enqueue(new Callback<SearchKeyword>() {
                        @Override
                        public void onResponse(Call<SearchKeyword> call, Response<SearchKeyword> response) {
                            Log.d("test1122", "성공..");
                        }

                        @Override
                        public void onFailure(Call<SearchKeyword> call, Throwable t) {

                        }
                    });

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dataLocation,15));
                }
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    public void loadData() {


    }

    //MainActivity에서 뷰페이저를 3개까지 미리 로드를 해놨기 때문에 실제로 화면이 보일때와 안보일때의 구분이 필요
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { //화면에 실제로 보일때
            checkPermission();
        } else { //preload 될때(전페이지에 있을때)
//            Log.d("test", "on");
        }
    }


    @SuppressLint("MissingPermission")
    public void GpsOnOffCheck() {
        //GPS가 켜져있는지 체크
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){ // gps가 꺼져 있을 경우
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 해당 데이터 위치설정
        LatLng dataLocation = new LatLng(37.568477, 126.981106);

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(dataLocation).title("테스트");

        // 마커 생성
        mMap.addMarker(markerOptions);

        // 카메라를 해당 위치로 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dataLocation,15));


    }


}
