package com.example.rhkdd.yunal.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rhkdd.yunal.GoogleMapActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommonItem;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;

import static com.example.rhkdd.yunal.DetailActivity.DETAIL_COMMON;
import static com.example.rhkdd.yunal.DetailActivity.MAIN_IMAGE;

/**
 * Created by rhkdd on 2018-04-02.
 */

public class MapOptionBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetDialogFragment bottomSheetDialogFragment;

    private DetailCommonItem detailCommonItem;
    private String mainImage;

    private Button goolgeMapView;
    private Button googleFindWay;
    private Button kakaoFindWay;
    private Button kakaoNavi;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //스마트폰 gps 켜져있는지 꺼져있는지 확인
//        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return inflater.inflate(R.layout.bottomsheet_map_option, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        프래그먼트를 종료하기 위한 방법
//        -findFragmentByTag안의 문자열 값은 상위에서 썻던 태그값과 동일시 해야됨.
//         */
        bottomSheetDialogFragment = (MapOptionBottomSheet) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag("MapOptionBottomSheet");

        // 액티비티에서 넘겨준 객체를 받아옴
        Bundle bundle = getArguments();
        detailCommonItem = (DetailCommonItem) bundle.getSerializable(DETAIL_COMMON);
        mainImage = bundle.getString(MAIN_IMAGE);

        // 초기화
        goolgeMapView = view.findViewById(R.id.googleMapView);
        googleFindWay = view.findViewById(R.id.googleFindWay);
        kakaoFindWay = view.findViewById(R.id.kakaoFindWay);
        kakaoNavi = view.findViewById(R.id.kakaoNavi);

        goolgeMapView.setOnClickListener(onClickListener);
        googleFindWay.setOnClickListener(onClickListener);
        kakaoFindWay.setOnClickListener(onClickListener);
        kakaoNavi.setOnClickListener(onClickListener);

    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.googleMapView : // 구글 지도 열기
                    Intent googleMapViewIntent = GoogleMapActivity.newIntent(getActivity(), detailCommonItem, mainImage);
                    startActivity(googleMapViewIntent);
                    bottomSheetDialogFragment.dismiss();
                    break;

                case R.id.googleFindWay : // 구글 길찾기
                    String googleUri ="http://maps.google.com/maps?saddr="+
                            "&daddr="+ detailCommonItem.mapy+","+ detailCommonItem.mapx;
                    Intent googleFindWayintent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(googleUri));
                    googleFindWayintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    googleFindWayintent.addCategory(Intent.CATEGORY_LAUNCHER );
                    googleFindWayintent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(googleFindWayintent);
                    bottomSheetDialogFragment.dismiss();
                    break;

                case R.id.kakaoFindWay :
                    /* 카카오맵 길찾기 */
                    try { // 카카오맵 열기
                        String kakaoUri = "daummaps://route?sp="+"&ep="
                                + detailCommonItem.mapy+","+ detailCommonItem.mapx+"&by=PUBLICTRANSIT";
                        Intent aa = new Intent(Intent.ACTION_VIEW);
                        aa.addCategory(Intent.CATEGORY_DEFAULT);
                        aa.addCategory(Intent.CATEGORY_BROWSABLE);
                        aa.setData(Uri.parse(kakaoUri));
                        startActivity(aa);
                        bottomSheetDialogFragment.dismiss();
                    } catch (Exception e) { // 카카오맵이 없을 경우 설치로 유도~
                        String kakaoUri2 = "https://play.google.com/store/apps/details?id=net.daum.android.map";
                        Intent aa = new Intent(Intent.ACTION_VIEW);
                        aa.setData(Uri.parse(kakaoUri2));
                        startActivity(aa);
                        bottomSheetDialogFragment.dismiss();

                    }
                    break;

                case R.id.kakaoNavi :
                    final KakaoNaviParams.Builder builder = KakaoNaviParams.newBuilder(com.kakao.kakaonavi.Location.newBuilder(detailCommonItem.title, detailCommonItem.mapx, detailCommonItem.mapy)
                            .build()).setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setStartX(126.5).setStartY(35.2).build());
                    KakaoNaviService.navigate(getActivity(), builder.build());
                    bottomSheetDialogFragment.dismiss();
                    break;
            }
        }
    };

}
