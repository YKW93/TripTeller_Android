package com.example.rhkdd.yunal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdd.yunal.adapter.DetailMainImageVPAdapter;
import com.example.rhkdd.yunal.adapter.ReviewImageVPAdapter;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.common.RetrofitTourClient;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommon;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommonItem;
import com.example.rhkdd.yunal.model.detailImage.DetailImage;
import com.example.rhkdd.yunal.model.detailImage.DetailImageItem;
import com.example.rhkdd.yunal.model.detailIntro.CultureData;
import com.example.rhkdd.yunal.model.detailIntro.DetailIntro;
import com.example.rhkdd.yunal.model.detailIntro.FestivalData;
import com.example.rhkdd.yunal.model.detailIntro.FoodData;
import com.example.rhkdd.yunal.model.detailIntro.LeportsData;
import com.example.rhkdd.yunal.model.detailIntro.LodgingData;
import com.example.rhkdd.yunal.model.detailIntro.ShoppingData;
import com.example.rhkdd.yunal.model.detailIntro.TouristData;
import com.example.rhkdd.yunal.dialog.MapOptionBottomSheet;
import com.example.rhkdd.yunal.model.locationBased.LocationBased;
import com.example.rhkdd.yunal.model.locationBased.LocationBasedItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.relex.circleindicator.CircleIndicator;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.ReviewActivity.RATING_NUM;
import static com.example.rhkdd.yunal.ReviewActivity.REVIEW_DATE;
import static com.example.rhkdd.yunal.ReviewActivity.REVIEW_EDIT;
import static com.example.rhkdd.yunal.ReviewActivity.REVIEW_IMAGES;
import static com.example.rhkdd.yunal.SearchActivity.API_key;

/**
 * Created by rhkdd on 2018-02-19.
 */

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    public static final String DETAIL_IMAGES = "DETAIL_IMAGES";
    public static final String MAIN_IMAGE = "MAIN_IMAGE";
    public static final String DETAIL_COMMON = "DETAIL_COMMON";
    public static final String CONTENT_TYPE_ID = " CONTENT_TYPE_ID";
    public static final String LOCATIONBASED_LIST_DATA = "LOCATIONBASED_LIST_DATA";
    public static final String COMMENT_IMAGE = "COMMENT_IMAGE";
    public static final String TOUR_NAME = "TOUR_NAME";
    private static final int ACTIVITY_REVIEW = 1000;



    private DetailCommonItem detailCommonItem;
    private ArrayList<DetailImageItem> detailImageItems;
    private ArrayList<LocationBasedItem> locationBasedItems;
    private DetailImageItem detailFirstImage;
    private ArrayList<DetailImageItem> totalDetailImageItem;

    private DetailMainImageVPAdapter viewPagerAdapter;
    private int contentTyId;

    private TextView toolbarTitle;
    private Boolean checkRun = true;
    private Button findingwayBtn;

    private GoogleMap mMap;

    private TextView locationBasedData_AllView;

//    // 리뷰하기 화면에서 넘어온 데이터들
//    private String reviewEdit;
//    private String ratingNum;
//    private ArrayList<Uri> reviewImages;

    public static Intent newIntent(Context context, int contentTyId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(CONTENT_TYPE_ID, contentTyId);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // intent 받은 데이터 가져오기
        Intent intent = getIntent();
        contentTyId = intent.getIntExtra(CONTENT_TYPE_ID, 0);

        Initialize();
        loadDetailData(contentTyId); // 여행 정보 파싱 하는 함수 호출

    }

    private void Initialize() {
        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바 왼쪽 이전버튼 생기게 하는 구문

        detailFirstImage = new DetailImageItem(); // 메인이미지 객체화시켜서 저장

        // viewapger 셋팅 및 indicator 셋팅 뿌려주기
        ViewPager viewPager = findViewById(R.id.viewPager);
        CircleIndicator indicator = findViewById(R.id.indicator);
        viewPagerAdapter = new DetailMainImageVPAdapter(DetailActivity.this);
        viewPager.setAdapter(viewPagerAdapter);
        indicator.setViewPager(viewPager);
        viewPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver()); // 뷰페이저 인디케이터 적용

        viewPager.setOnClickListener(onClickListener);


        // 길찾기 버튼
        findingwayBtn = findViewById(R.id.finding_way);
        findingwayBtn.setOnClickListener(onClickListener);


        // 툴바 셋팅
        toolbarTitle = findViewById(R.id.toolbar_title);

        final LinearLayout head_layout = findViewById(R.id.headlayout);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    toolbarTitle.setVisibility(View.VISIBLE);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);
                } else {
                    toolbarTitle.setVisibility(View.GONE);
                }
            }
        });

        //댓글 달기 플로팅 버튼
        FloatingActionButton floatingActionButton = findViewById(R.id.reviewBtn);
        floatingActionButton.setOnClickListener(onClickListener);


        // 잘못된 정보 신고하기 버튼
        TextView falseInfoBtn = findViewById(R.id.falseInfoBtn);
        falseInfoBtn.setOnClickListener(onClickListener);

        locationBasedData_AllView = findViewById(R.id.allDataView);
        locationBasedData_AllView.setOnClickListener(onClickListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("rhkddn1657", "onRestart 실행");
    }

    // 파싱 해온 데이터(여행관련) 화면에 적용.
    private void setDetailData() {

        // 메인 이미지가 있으면 해당 이미지 url 넣고 없으면 null
        detailFirstImage.originimgurl = detailCommonItem.firstimage;

        // 뷰페이저에 사진을 넣는 작업.
        totalDetailImageItem = new ArrayList<>();
        if (detailFirstImage.originimgurl == null) { // 메인 이미지가 없을 경우 (메인이미지가 없고 추가이
            // 미지가 있는 경우도 있는데 이건 복잡해져서 생략함.)
            detailFirstImage.originimgurl = String.valueOf(R.drawable.no_image);
            totalDetailImageItem.add(detailFirstImage);
            viewPagerAdapter.setData(totalDetailImageItem);
        } else if (detailFirstImage.originimgurl != null && detailImageItems == null) { // 메인 이미지는 있는데 추가이미지가 없을 경우
            totalDetailImageItem.add(detailFirstImage);
            viewPagerAdapter.setData(totalDetailImageItem);
        } else { // 메인 이미지 있고 추가이미지 있는 경우
            totalDetailImageItem.add(detailFirstImage);
            totalDetailImageItem.addAll(detailImageItems);
            viewPagerAdapter.setData(totalDetailImageItem);
        }

        // 구글맵 부분
        /*
        해당 구글맵 호출부분을 onCreate에 넣으면 안됨 그 이유는 호출되는 onMapReady() 도 스레드를 통해서 불리게 되는데
        내가 원하는 map.x , map.y 의 데이터들이 파싱되기도 전에 onCreate에서 onMapReady()메소드를 부르게되고 onMapReady()에서
        파싱도 안된 데이터를 쓰게되서 오류가 나게된다.
         */
        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // getMapAsync()를 통해 onMapReady()가 자동 호출

        // 툴바 타이틀 제목 셋팅
        toolbarTitle.setText(detailCommonItem.title);

        //소개 정보 뿌려주기
        LinearLayout introduceInfoLayout = findViewById(R.id.introduceinfolayout);
        if (detailCommonItem.overview == null) {
            introduceInfoLayout.setVisibility(View.GONE);
        } else {
            TextView summary_TV = findViewById(R.id.summary);
            summary_TV.setText(Html.fromHtml(detailCommonItem.overview));
        }
        Button introduceDetailBtn = findViewById(R.id.introduceinfoDetailBtn);
        introduceDetailBtn.setOnClickListener(onClickListener);

        // 공통정보 틀 셋팅
        TextView common_title = findViewById(R.id.common_title);
        common_title.setText(detailCommonItem.title);

        TextView common_contenttype = findViewById(R.id.common_contenttype);
        ImageView common_contenttypeimage = findViewById(R.id.common_contenttype_image);
        int contenttypeid = detailCommonItem.contenttypeid;
        if (contenttypeid == 12) {
            common_contenttype.setText("관광지");
            common_contenttypeimage.setImageResource(R.drawable.travel);
        } else if (contenttypeid == 14) {
            common_contenttype.setText("문화시설");
            common_contenttypeimage.setImageResource(R.drawable.culture);
        }  else if (contenttypeid == 15) {
            common_contenttype.setText("행사");
            common_contenttypeimage.setImageResource(R.drawable.festival);
        }  else if (contenttypeid == 28) {
            common_contenttype.setText("레포츠");
            common_contenttypeimage.setImageResource(R.drawable.leisure);
        }  else if (contenttypeid == 32) {
            common_contenttype.setText("숙박");
            common_contenttypeimage.setImageResource(R.drawable.lodgment);
        }  else if (contenttypeid == 38) {
            common_contenttype.setText("쇼핑");
            common_contenttypeimage.setImageResource(R.drawable.shopping);
        }  else if (contenttypeid == 39) {
            common_contenttype.setText("음식점");
            common_contenttypeimage.setImageResource(R.drawable.food);
        }

        // 공통정보 데이터 뿌려주기
        commonBasicInfoSetting(detailCommonItem);

        // 주변 장소 제목 셋팅
        TextView placeName = findViewById(R.id.placeName);
        placeName.setText(detailCommonItem.title);
    }

    // 여행 정보 데이터 요청
    private void loadDetailData(final int contentTyId) {

        Call<DetailImage> call = RetrofitTourClient.getInstance().getService(null).detailImage(API_key, "yunal",
                "AND", "json", contentTyId);
        call.enqueue(new Callback<DetailImage>() {
            @Override
            public void onResponse(Call<DetailImage> call, Response<DetailImage> response) { // 추가이미지 있을 경우
                if (response.isSuccessful() && response.body() != null) {
                    DetailImage detailImage = response.body();
                    if (detailImage != null) {
                        // 1.추가 이미지는 있는데 대표이미지가 있을 경우 없을 경우가 있는데 결국 없을 경우에는 null 값이 보내지기 때문에 glide에서 null값을 ]
                        //   받으면 error(drawable) 설정한 이미지가 보여져서 문제없음.
                        detailImageItems = new ArrayList<>();
                        detailImageItems.addAll(detailImage.response.body.items.item);
                        Call<DetailCommon> call2 = RetrofitTourClient.getInstance().getService(null).detailCommon(API_key, "yunal", "AND", "json",
                                contentTyId, "Y", "Y", "Y", "Y", "Y");
                        call2.enqueue(new Callback<DetailCommon>() {
                            @Override
                            public void onResponse(Call<DetailCommon> call, @NonNull Response<DetailCommon> response) {
                                DetailCommon detailCommon = response.body();
                                detailCommonItem = detailCommon.response.body.items.item;
                                setDetailData(); //파싱이 끝나면 화면 데이터 셋팅하기
                                loadLocationBasedData(); // 해당여행지의 주변 장소 파싱하는 함수 호출(mapx, mapy 값을 사용해야되서 여기서 호출)
                            }

                            @Override
                            public void onFailure(Call<DetailCommon> call, Throwable t) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailImage> call, Throwable t) {
                // 1.이건 추가이미지가 없을 경우이니 인텐트를 설정할때 그냥 null 값을 보내버린뒤 DetailActivity에서 null 값이 오면 예외처리를 해주면됨
                // 2.이때도 위와 같이 대표이미지가 있을 경우 없을 경우로 나뉠수 있다.
                Call<DetailCommon> call2 = RetrofitTourClient.getInstance().getService(null).detailCommon(API_key, "yunal",
                        "AND", "json", contentTyId, "Y", "Y", "Y", "Y", "Y");
                call2.enqueue(new Callback<DetailCommon>() {
                    @Override
                    public void onResponse(Call<DetailCommon> call, Response<DetailCommon> response) {
                        DetailCommon detailCommon = response.body();
                        detailCommonItem = detailCommon.response.body.items.item;
                        setDetailData(); //파싱이 끝나면 화면 데이터 셋팅하기
                        loadLocationBasedData(); // 해당여행지의 주변 장소 파싱하는 함수 호출(mapx, mapy 값을 사용해야되서 여기서 호출)
                    }

                    @Override
                    public void onFailure(Call<DetailCommon> call, Throwable t) {

                    }
                });
            }
        });
    }

    // 해당 여행지 주변장소 데이터 요청
    private void loadLocationBasedData() {

        Call<LocationBased> call = RetrofitTourClient.getInstance().getService(null).locationBased(API_key, "yunal",
                "AND", "json", detailCommonItem.mapx, detailCommonItem.mapy, 2000, "S",1, 1000);
        Log.d("rhkddn1657", "호출 url:"+ call.request().url());
        call.enqueue(new Callback<LocationBased>() {
            @Override
            public void onResponse(Call<LocationBased> call, Response<LocationBased> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LocationBased locationBased = response.body();
                    if (locationBased != null) {
                        locationBasedItems = new ArrayList<>();
                        locationBasedItems.addAll(locationBased.response.body.items.item);
                        locationBasedItems.remove(0); // 첫번째 값은 해당 여행지이기때문에 삭제 해줘야됨.
                        Log.d("rhkddn1657", "토탈 카운트 :  " +  locationBased.response.body.totalCount);

                        for (int i = 0; i < locationBasedItems.size(); i++) {
                            if (i > 3) { // 아이템을 4개까지만 출력하기 위한 조건문.
                                break;
                            } else {
                                setLocationBasedData(locationBasedItems, i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationBased> call, Throwable t) {
                Log.d("rhkddn1657", "실패");
                // 여행지 주변 장소가 없을 경우 그냥 layout 화면에서 삭제
                LinearLayout locationBasedParentLayout = findViewById(R.id.locationBasedParentLayout);
                locationBasedParentLayout.setVisibility(View.GONE);

            }
        });
    }

    // 해당 여행지 주변장소 화면에 뿌려주기
    private void setLocationBasedData(final ArrayList<LocationBasedItem> lists, final int index) {

        // 화면 넓이 구하기
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;


        GridLayout gridLayout = findViewById(R.id.locationBasedLayout);

        /* 아이템뷰 아이템 초기화 */
        View v = LayoutInflater.from(DetailActivity.this).inflate(R.layout.item_recyclerview_locationbased, gridLayout, false);
        // 아이템 뷰 넓이를 휴대폰 화면의 반으로 나누어서 적용.
        v.findViewById(R.id.view_layout).setLayoutParams(new LinearLayout.LayoutParams(width/2, GridLayout.LayoutParams.WRAP_CONTENT));
        ImageView thumbIV = v.findViewById(R.id.thumb);
        TextView titleTV = v.findViewById(R.id.name);
        TextView distTV = v.findViewById(R.id.dist);

        /* 아이템뷰 아이템 데이터 셋팅 */
        GlideApp.with(DetailActivity.this).load(lists.get(index).firstimage).into(thumbIV);
        titleTV.setText(lists.get(index).title);
        distTV.setText(String.valueOf(lists.get(index).dist) + "m");
        gridLayout.addView(v);

        /*아이템뷰 클릭 리스너*/
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailActivity.newIntent(DetailActivity.this, lists.get(index).contentid);
                startActivity(intent);
            }
        });

    }

    // 서버에서 사용자 댓글 데이터를 가져오는 함수
    private void loadCommentData() {

        // 서버에 있는 사용자가 올린 댓글을 전부 가져올수 있도록 작업
    }


    // 서버에서 가져온 사용자 댓글을 화면에 뿌려줌
    private void setCommentData(String reviewEdit, String ratingNum, String date, ArrayList<Uri> reviewImages) {

        LinearLayout reviewLayout = findViewById(R.id.reviewLayout);

        // 댓글 아이템뷰 가져오기
        View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.item_recyclerview_comment, reviewLayout, false);

        // 뷰페이저 셋팅
        ViewPager viewPager = view.findViewById(R.id.commentVP);

        if (reviewImages.isEmpty()) { // 사용자가 댓글에 이미지를 추가 안했을 경우
            viewPager.setVisibility(View.GONE);
        } else { // 댓글에 이미지를 추가했을 경우 -> Viewpager 셋팅
            viewPager.setClipToPadding(false);
            viewPager.setPadding(35,0,35,0);
            viewPager.setPageMargin(17);

            ReviewImageVPAdapter commentImageVP = new ReviewImageVPAdapter(DetailActivity.this);
            commentImageVP.setData(reviewImages);
            viewPager.setAdapter(commentImageVP);

        }

        // 아이템뷰를 이용한 id값 지정
        TextView commentTV = view.findViewById(R.id.commentTV);
        MaterialRatingBar ratingBar = view.findViewById(R.id.ratingbar);
        TextView ratingBarTV = view.findViewById(R.id.ratingbarTV);
        TextView commentDate = view.findViewById(R.id.commentDate);

        // 데이터 셋팅
        commentTV.setText(reviewEdit);
        ratingBar.setRating(Float.parseFloat(ratingNum));
        ratingBarTV.setText(ratingNum);
        commentDate.setText(date);

        reviewLayout.addView(view);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 해당 데이터 위치설정
        LatLng DataLocation = new LatLng(detailCommonItem.mapy, detailCommonItem.mapx);

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DataLocation).title(detailCommonItem.title);

        // 마커 생성
        mMap.addMarker(markerOptions);

        // 카메라를 해당 위치로 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DataLocation,15));

        // map 클릭 막아 놓기 위한 구문
        mMap.getUiSettings().setScrollGesturesEnabled(false);


        Log.d("marker", "마커 값 : " + markerOptions.getPosition());
        Log.d("marker", "카메라 값 : " + mMap.getCameraPosition());


    }


    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.introduceinfoDetailBtn :
                    if (checkRun) {
                        checkRun = false;

                        Gson gson = RetorfitGsonSetting();

                        Call<DetailIntro> call = RetrofitTourClient.getInstance().getService(gson).detailIntro(API_key, "yunal",
                                "AND", "json", detailCommonItem.contentid, detailCommonItem.contenttypeid, "Y");

                        call.enqueue(new Callback<DetailIntro>() {
                            @Override
                            public void onResponse(Call<DetailIntro> call, Response<DetailIntro> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    DetailIntro detailIntro = response.body();
                                    if (detailIntro != null) {

                                        int typeid = detailCommonItem.contenttypeid;
                                        Intent intent = null;
                                        if (typeid == 12) { // 관광지
                                            TouristData touristData = (TouristData) detailIntro.response.body.items.item;
                                            intent = IntroductoryInfoActivity.newIntent(DetailActivity.this, touristData,
                                                    detailCommonItem.overview, detailCommonItem.title, typeid);
                                        } else if (typeid == 14) { // 문화시설
                                            CultureData cultureData = (CultureData) detailIntro.response.body.items.item;
                                            intent = IntroductoryInfoActivity.newIntent(DetailActivity.this, cultureData,
                                                    detailCommonItem.overview, detailCommonItem.title, typeid);
                                        } else if (typeid == 15) { // 행사/공연/축제
                                            FestivalData festivalData = (FestivalData) detailIntro.response.body.items.item;
                                            intent = IntroductoryInfoActivity.newIntent(DetailActivity.this, festivalData,
                                                    detailCommonItem.overview, detailCommonItem.title, typeid);
                                        } else if (typeid == 28) { // 레포츠
                                            LeportsData leportsData = (LeportsData) detailIntro.response.body.items.item;
                                            intent = IntroductoryInfoActivity.newIntent(DetailActivity.this, leportsData,
                                                    detailCommonItem.overview, detailCommonItem.title, typeid);
                                        } else if (typeid == 32) { // 숙박
                                            LodgingData lodgingData = (LodgingData) detailIntro.response.body.items.item;
                                            intent = IntroductoryInfoActivity.newIntent(DetailActivity.this, lodgingData,
                                                    detailCommonItem.overview, detailCommonItem.title, typeid);
                                        } else if (typeid == 38) { // 쇼핑
                                            ShoppingData shoppingData = (ShoppingData) detailIntro.response.body.items.item;
                                            intent = IntroductoryInfoActivity.newIntent(DetailActivity.this, shoppingData,
                                                    detailCommonItem.overview, detailCommonItem.title, typeid);
                                        } else if (typeid == 39) { // 음식점
                                            FoodData foodData = (FoodData) detailIntro.response.body.items.item;
                                            intent = IntroductoryInfoActivity.newIntent(DetailActivity.this, foodData,
                                                    detailCommonItem.overview, detailCommonItem.title, typeid);
                                        }
                                        startActivity(intent);


                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DetailIntro> call, Throwable t) {

                            }
                        });
                        checkRun = true;
                    }
                    break;
//                case R.id.representativeImage :
//                    Intent intent = DetailImageActivity.newIntent(DetailActivity.this, totalDetailImageItem);
//                    startActivity(intent);
//                    break;

                case R.id.finding_way :
                    //위치 정보 퍼미션 체크
                    PermissionListener permissionlistener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
//                            Toast.makeText(DetailActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                            MapOptionBottomSheet searchResultBottomSheet = new MapOptionBottomSheet();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(DETAIL_COMMON, detailCommonItem);
                            bundle.putString(MAIN_IMAGE, detailCommonItem.firstimage);
                            searchResultBottomSheet.setArguments(bundle);
                            FragmentManager fm = getSupportFragmentManager();   // FragmentManager를 통해서 Fragment 관리
                            searchResultBottomSheet.show(fm, "MapOptionBottomSheet");
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                            Toast.makeText(DetailActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

                        }
                    };


                    TedPermission.with(DetailActivity.this)
                            .setPermissionListener(permissionlistener)
                            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                            .check();

                    break;
                case R.id.reviewBtn :
                    // 후기 작성

                    PermissionListener permissionListener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Intent reviewIntent = ReviewActivity.newIntent(DetailActivity.this, detailCommonItem.title);
                            startActivityForResult(reviewIntent, ACTIVITY_REVIEW);
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            Toast.makeText(DetailActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                        }
                    };

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        TedPermission.with(DetailActivity.this)
                                .setPermissionListener(permissionListener)
                                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                                .check();
                    }
                    break;
                case R.id.falseInfoBtn : // 잘못된 정보 신고하기 버튼
                    break;
                case R.id.allDataView : // 주변장소 전체보기 버튼클릭 처리
                    Intent intent1 =LocationBasedListActivity.newIntent(DetailActivity.this, locationBasedItems, detailCommonItem.title);
                    startActivity(intent1);
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REVIEW) {
            if (resultCode == RESULT_OK) {

                TextView textView = findViewById(R.id.reviewMessage);
                textView.setVisibility(View.GONE);


                // 인텐트 값 가져오기
                String reviewEdit = data.getStringExtra(REVIEW_EDIT);
                String ratingNum = data.getStringExtra(RATING_NUM);
                ArrayList<Uri> reviewImages = (ArrayList<Uri>) data.getSerializableExtra(REVIEW_IMAGES);
                String date = data.getStringExtra(REVIEW_DATE);

                setCommentData(reviewEdit, ratingNum, date, reviewImages);

                /*

                서버 완성전이여서 테스트로 임시로 이렇게 만들어놈.

                * 기본 기획 작업 순서도
                * 첫번째 DetailActivity가 처음 켜졌을 경우 , 두번째 ReviewActivity 에서 리뷰를 하고 작성하고 DetailActivity가 켜진 경우

                첫번째 작업 순서도
                - onCreate 함수에서 loadCommentData() 함수를 불러온 뒤 서버에 있는 댓글들을 가져옴.
                - 가져온 댓글을 setCommentData() 함수를 호출해서 화면에 보여줌 이때 loadCommentData() 함수의 맨마지막 부분에서 서언해주면 문제 없을 듯.
                 (setCommentData 매개변수 값은 댓글들이 여러개 일수가 있으므로 리스트가 되어야함.)


                두번째 작업 순서도
                - ReviewActivity 에서 사용자가 댓글을 쓰고 작성 버튼을 누르면 서버에 데이터 전송.
                - onRestart() 함수에서 loadCommentData() 함수를 불러온 뒤 서버에 있는 댓글들을 가져옴.
                - 이렇게 하면 첫번째 작업 순서도와 거의 비슷한 진행 방향으로 흘러 갈 듯.

                * 현재는 테스트를 위해서 onActivityResult를 사용해서 ReviewActivity에서 넘어온 값을 받아줬지만 서버가 있다면
                굳이 이런 작업 필요없이 서버에 데이터를 보내고 서버에서 가져오면 되기때문에 onActivityResult는 필요가 없어짐.

                 */

            }
            if (requestCode == RESULT_CANCELED) {
                // ReviewActivity 에서 아무 값도 넘어 오지 않을 경우.
            }
        }

    }

    // 해당 데이터가 어떤 타입인지 판별하고 데이터 받기 전에 Gson 셋팅 하는 메소드
    public Gson RetorfitGsonSetting() {

        int contentTypeId = detailCommonItem.contenttypeid;
        Gson gson = null;

        if (contentTypeId == 12) { // 관광지
            gson = new GsonBuilder().registerTypeAdapter(DetailIntro.Items.class, new ItemDeserializer(TouristData.class)).create();

        } else if (contentTypeId == 14) { // 문화시설
            gson = new GsonBuilder().registerTypeAdapter(DetailIntro.Items.class, new ItemDeserializer(CultureData.class)).create();

        } else if (contentTypeId == 15) { // 행사/공연/축제
            gson = new GsonBuilder().registerTypeAdapter(DetailIntro.Items.class, new ItemDeserializer(FestivalData.class)).create();

        } else if (contentTypeId == 28) { // 레포츠
            gson = new GsonBuilder().registerTypeAdapter(DetailIntro.Items.class, new ItemDeserializer(LeportsData.class)).create();

        } else if (contentTypeId == 32) { // 숙박
            gson = new GsonBuilder().registerTypeAdapter(DetailIntro.Items.class, new ItemDeserializer(LodgingData.class)).create();

        } else if (contentTypeId == 38) { // 쇼핑
            gson = new GsonBuilder().registerTypeAdapter(DetailIntro.Items.class, new ItemDeserializer(ShoppingData.class)).create();

        } else if (contentTypeId == 39) { // 음식점
            gson = new GsonBuilder().registerTypeAdapter(DetailIntro.Items.class, new ItemDeserializer(FoodData.class)).create();

        }

        return gson;
    }

//    public void setDetailImageLists(ArrayList<DetailImageItem> detailImageItems) {
//        this.detailImageItems = detailImageItems;
//    }


    //서버 데이터 받아오기전 Gson 셋팅
    public class ItemDeserializer<T> implements JsonDeserializer<DetailIntro.Items<T>> {

        private T TypeClass;

        public ItemDeserializer(T data) {
            TypeClass = data;
        }

        @Override
        public DetailIntro.Items<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            JsonElement monumentElement = json.getAsJsonObject().get("item");


            return (DetailIntro.Items<T>) new DetailIntro.Items<>((T)context.deserialize(monumentElement.getAsJsonObject(), (Type) TypeClass));

        }
    }

    // 기본정보 데이터 셋팅 함수
    public void commonBasicInfoSetting(DetailCommonItem detailCommonItem) {

        TextView common_homepage = findViewById(R.id.common_homepage);
        TextView common_address = findViewById(R.id.common_address);
        TextView common_tel = findViewById(R.id.common_tel);
        TextView common_createdtime = findViewById(R.id.common_createdtime);
        TextView common_modifiedtime = findViewById(R.id.common_modifiedtime);

        ImageView common_hompage_image = findViewById(R.id.common_homepage_image);
        ImageView common_address_image = findViewById(R.id.common_address_image);
        ImageView common_tel_image = findViewById(R.id.common_tel_image);


        if (detailCommonItem.homepage != null) {
//            String htmlTag = detailCommonItem.homepage.replace("/[<]br [/][>]/gi", "");
//            String htmlTag = detailCommonItem.homepage.replace("<[^>]*>", "");
            String htmlTag = detailCommonItem.homepage;
            String tempHtmlTag = htmlTag;

            Pattern pattern = Pattern.compile("<(/)?[bB][rR](\\s)*(/)?>"); // 정규식을 이용한 <br /> 태그 제거
            Matcher matcher = pattern.matcher(htmlTag);

            while (matcher.find()) {
                tempHtmlTag = matcher.replaceAll("");
            }

            common_homepage.setText(Html.fromHtml(tempHtmlTag));
            common_hompage_image.setImageResource(R.drawable.homepage);

        } else {
            common_homepage.setText("준비중 입니다.");
            common_hompage_image.setImageResource(R.drawable.homepage);
        }

        if (detailCommonItem.addr1 != null || detailCommonItem.addr2 != null) {
            String addr = detailCommonItem.addr1 + detailCommonItem.addr2;
            common_address.setText(addr);
            common_address_image.setImageResource(R.drawable.address);

        } else {
            common_address.setVisibility(View.GONE);
            common_address_image.setVisibility(View.GONE);
        }

        if (detailCommonItem.tel != null) {
            common_tel.setText(Html.fromHtml(detailCommonItem.tel));
            common_tel_image.setImageResource(R.drawable.tel);

        } else {
            common_tel_image.setImageResource(R.drawable.tel);
            common_tel.setText("준비중 입니다.");

        }

        if (detailCommonItem.createdtime != 0) {
            String createtime = String.valueOf(detailCommonItem.createdtime);
            StringBuilder stringBuilder = new StringBuilder(createtime);
            stringBuilder.delete(8, createtime.length());
            Log.d("test11", "등록날짜:"+stringBuilder);
            stringBuilder.insert(4, "-");
            stringBuilder.insert(7, "-");

            common_createdtime.setText("등록날짜 :  " + stringBuilder);

        } else {
            common_createdtime.setVisibility(View.GONE);
        }

        if (detailCommonItem.modifiedtime != 0) {

            String modifitime = String.valueOf(detailCommonItem.modifiedtime);
            StringBuilder stringBuilder = new StringBuilder(modifitime);
            stringBuilder.delete(8, modifitime.length());
            stringBuilder.insert(4, "-");
            stringBuilder.insert(7, "-");

            common_modifiedtime.setText("수정날짜 :  " + stringBuilder);

        } else {
            common_modifiedtime.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home :
                finish();
                return true;
            case R.id.menu_item_map :
                Intent intent = GoogleMapActivity.newIntent(DetailActivity.this, detailCommonItem, detailCommonItem.firstimage);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
