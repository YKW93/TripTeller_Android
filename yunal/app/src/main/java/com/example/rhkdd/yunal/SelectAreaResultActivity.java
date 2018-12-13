package com.example.rhkdd.yunal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdd.yunal.adapter.LocalPopularityTellerVPAdapter;
import com.example.rhkdd.yunal.adapter.TourResultRVAdapter;
import com.example.rhkdd.yunal.common.LoadingScreenHelper;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.RetrofitTourClient;
import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.common.UserInfoReturn;
import com.example.rhkdd.yunal.model.areaBase.AreaBase;
import com.example.rhkdd.yunal.model.areaBase.AreaBaseItem;
import com.example.rhkdd.yunal.dialog.SelectAreaMainBottomSheet;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;
import com.example.rhkdd.yunal.model.tourDetail.TourReviewItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.SelectAreaActivity.AREA_CODE;
import static com.example.rhkdd.yunal.SelectAreaActivity.AREA_NAME;
import static com.example.rhkdd.yunal.SelectAreaActivity.SIGUNGU_CODE;
import static com.example.rhkdd.yunal.SelectAreaActivity.SIGUNGU_NAME;
import static com.example.rhkdd.yunal.common.RetrofitTourClient.API_key;

public class SelectAreaResultActivity extends AppCompatActivity {

    final static String TAG = "SelectAreaResult";

    private LinearLayout tellerReviewLayout;
    private LinearLayout emptyLayout;

    private String areaCode;
    private String sigunguCode;
    private String areaName;
    private String sigunguName;

    private String email_id;

    private ArrayList<AreaBaseItem> areaBaseItems;
    private ArrayList<Integer> contentIdList;

    private int currentPage = 1;

    private TourResultRVAdapter tourResultRVAdapter;
    private GridLayoutManager gridLayoutManager;

    private LocalPopularityTellerVPAdapter localTellerWritingVP;

    private String arrange;
    private String contentTypeId;

    private Boolean checkRun = true;

    private int listPositionData = 0;
    private int listPositionContentId;

    public static Intent newIntent(Context context, String areaCode, String sigunguCode, String areaName, String sigunguName) {

        Intent intent = new Intent(context, SelectAreaResultActivity.class);
        intent.putExtra(AREA_CODE, areaCode);
        intent.putExtra(SIGUNGU_CODE, sigunguCode);
        intent.putExtra(AREA_NAME, areaName);
        intent.putExtra(SIGUNGU_NAME, sigunguName);

        return intent;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectareamain);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(SelectAreaResultActivity.this, getResources().getColor(R.color.status_color));

        Initialize();
        loadAreaData(currentPage, arrange, contentTypeId); // 여행정보 가져오기
        loadPopularityTellerReviewData(); // 지역인기 텔러 정보 가져오기
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadSingleData(listPositionContentId);
        loadPopularityTellerReviewData();
    }

    private void Initialize() {

        // 휴대폰 내에 저장된 사용자 email 값 가져오기
        email_id = UserInfoReturn.getInstance().getUserNicname(SelectAreaResultActivity.this);

        areaBaseItems = new ArrayList<>();
        contentIdList = new ArrayList<>();

        // default 값 설정
        arrange = "O"; // (초기 여행지) 데이터 정렬 제목순으로 셋팅
        contentTypeId = null;

        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);

        // intent 정보 받아오기
        Intent intent = getIntent();
        areaCode = intent.getStringExtra(AREA_CODE);
        sigunguCode = intent.getStringExtra(SIGUNGU_CODE);
        areaName = intent.getStringExtra(AREA_NAME);
        sigunguName = intent.getStringExtra(SIGUNGU_NAME);

        tellerReviewLayout = findViewById(R.id.tellerReviewLayout);
        emptyLayout = findViewById(R.id.emptylayout);

        // 정보 처리
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        if (sigunguCode != null & sigunguName != null) { // 광역시 , 시군구 모든 정보
            toolbar_title.setText(String.valueOf(areaName + "/" + sigunguName));
        } else { // 광역시 정보
            toolbar_title.setText(String.valueOf(areaName + " 전체"));
        }

        // 리사이클러뷰 셋팅
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false); // ScrollView 안에 RecyclerView 설정 시, Scroll이 부드럽게 되지 않는 경우가 발생해서 해당 구문 필요
        gridLayoutManager = new GridLayoutManager(SelectAreaResultActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        tourResultRVAdapter = new TourResultRVAdapter(SelectAreaResultActivity.this);
        recyclerView.setAdapter(tourResultRVAdapter);

        // 사용자가 스크롤 끝에 도달했을 경우를 처리하기 위한 구문
        final NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                if (diff == 0) {
                    //code to fetch more data for endless scrolling
                    currentPage++;
                    loadAreaData(currentPage, arrange, contentTypeId);
                }
            }
        });

        // 뷰페이저 셋팅
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setClipToPadding(false);
        int padding_dp = 30;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dp = Math.round(padding_dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        viewPager.setPadding(0,0, dp,0);
        localTellerWritingVP = new LocalPopularityTellerVPAdapter(SelectAreaResultActivity.this);
        viewPager.setAdapter(localTellerWritingVP);


        // 클릭 리스너
        toolbar.findViewById(R.id.toolbar_title).setOnClickListener(onClickListener);
        findViewById(R.id.filter_btn).setOnClickListener(onClickListener);

    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.toolbar_title :
                    Intent intent1 = new Intent(SelectAreaResultActivity.this, SelectAreaActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.filter_btn :
                    SelectAreaMainBottomSheet selectAreaMainBottomSheet = new SelectAreaMainBottomSheet();
                    FragmentManager fm = getSupportFragmentManager();
                    selectAreaMainBottomSheet.show(fm, "selectAreaMainBottomSheet");
                    break;
            }
        }
    };


    public void setPositionData(int position, int contentId) {
        listPositionData = position;
        listPositionContentId = contentId;
    }

    public void resetData(String arrange, String contentTypeId) {
        currentPage = 1;
        areaBaseItems.clear();
        if (arrange != null || contentTypeId != null) {
            this.arrange = arrange;
            this.contentTypeId = contentTypeId;
            loadAreaData(currentPage, this.arrange, this.contentTypeId);
        } else {
            loadAreaData(currentPage, this.arrange, this.contentTypeId);
        }
    }

    private void loadPopularityTellerReviewData() {

        if (sigunguCode == null) { // 지역 전체보기
            Call<ArrayList<TourReviewItem>> call = RetrofitServerClient.getInstance().getService().AreaTotalReviewResponseResult(Integer.valueOf(areaCode));
            call.enqueue(new Callback<ArrayList<TourReviewItem>>() {
                @Override
                public void onResponse(Call<ArrayList<TourReviewItem>> call, Response<ArrayList<TourReviewItem>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<TourReviewItem> tourReviewItems = response.body();
                        if (tourReviewItems != null && tourReviewItems.size() != 0) { // 지역 인기 텔러 데이터가 있을 경우
                            localTellerWritingVP.setData(tourReviewItems);
                        } else { // 지역 인기 텔러 데이터가 없을 경우
                            tellerReviewLayout.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TourReviewItem>> call, Throwable t) {

                }
            });
        } else { // 지역 시군구 값 모두 존재할때
            Call<ArrayList<TourReviewItem>> call = RetrofitServerClient.getInstance().getService().AreaSigunguReviewResponseResult(Integer.valueOf(areaCode), Integer.valueOf(sigunguCode));
            call.enqueue(new Callback<ArrayList<TourReviewItem>>() {
                @Override
                public void onResponse(Call<ArrayList<TourReviewItem>> call, Response<ArrayList<TourReviewItem>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<TourReviewItem> tourReviewItems = response.body();
                        if (tourReviewItems != null && tourReviewItems.size() != 0) { // 지역 인기 텔러 데이터가 있을 경우
                            localTellerWritingVP.setData(tourReviewItems);
                        } else { // 지역 인기 텔러 데이터가 없을 경우
                            tellerReviewLayout.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TourReviewItem>> call, Throwable t) {

                }
            });
        }
    }

    private void loadSingleData(int contentId) { // 단일 관광지 데이터를 호출할 경우
        ArrayList<Integer> contentIdList = new ArrayList<>();
        contentIdList.add(contentId);
        Call<ArrayList<TourInfoItem>> call = RetrofitServerClient.getInstance().getService().TourInfoResponseResult(email_id, contentIdList);
        call.enqueue(new Callback<ArrayList<TourInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<TourInfoItem>> call, Response<ArrayList<TourInfoItem>> response) {
                if (response.isSuccessful()) {
                    ArrayList<TourInfoItem> tourInfoItems = response.body();
                    if (tourInfoItems != null && !tourInfoItems.isEmpty()) {
                        tourResultRVAdapter.changeData(listPositionData, tourInfoItems.get(0));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TourInfoItem>> call, Throwable t) {
            }
        });
    }

    public void loadAreaData(int page, String arrange, final String contentTypeId) {
        LoadingScreenHelper.getInstance().progressON(SelectAreaResultActivity.this);

        contentIdList.clear();

        SelectAreaResultActivity.ItemDeserializer itemDeserializer = new SelectAreaResultActivity.ItemDeserializer();
        Gson gson = new GsonBuilder().registerTypeAdapter(AreaBase.Items.class, itemDeserializer).create();

        Call<AreaBase> call = RetrofitTourClient.getInstance().getService(gson).AreaBase(API_key, "yunal",
                "AND", "json", page, 20, areaCode, sigunguCode, arrange, contentTypeId);
        Log.e(TAG, call.request().url().toString());
        call.enqueue(new Callback<AreaBase>() {
            @Override
            public void onResponse(Call<AreaBase> call, Response<AreaBase> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AreaBase areaBaseData = response.body();
                    if (areaBaseData != null && areaBaseData.response.body.items != null) {

                        int j = 0;
                        for (int i = 0; i < areaBaseData.response.body.items.item.size(); i++) {
                            if (areaBaseData.response.body.items.item.get(i-j).contenttypeid == 25) { // 여행코스(관광 타입) 데이터 제외시킴
                                areaBaseData.response.body.items.item.remove(i-j);
                                j++;
                            }
                        }

                        areaBaseItems.addAll(areaBaseData.response.body.items.item);

                        for (int i = 0; i < areaBaseItems.size(); i++) {
                            contentIdList.add(areaBaseItems.get(i).contentid);
                        }


                        Call<ArrayList<TourInfoItem>> serverCall = RetrofitServerClient.getInstance().getService().TourInfoResponseResult(email_id, contentIdList);
                        serverCall.enqueue(new Callback<ArrayList<TourInfoItem>>() {
                            @Override
                            public void onResponse(Call<ArrayList<TourInfoItem>> call, Response<ArrayList<TourInfoItem>> response) {
                                if (response.isSuccessful()) {
                                    ArrayList<TourInfoItem> tourInfoItems = response.body();
                                    if (tourInfoItems != null && !tourInfoItems.isEmpty()) {
                                        tourResultRVAdapter.setData(areaBaseItems, tourInfoItems);
                                        LoadingScreenHelper.getInstance().progressOFF();

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<TourInfoItem>> call, Throwable t) {
                                LoadingScreenHelper.getInstance().progressOFF();

                            }
                        });

                    }
                }
                LoadingScreenHelper.getInstance().progressOFF();
            }

            @Override
            public void onFailure(Call<AreaBase> call, Throwable t) {

                Log.e(TAG,"요청 메시지 :"+call.request());
                Log.e(TAG,"지역정보 불러오기 실패 :" + t.getMessage() );
                LoadingScreenHelper.getInstance().progressOFF();
                Toasty.error(SelectAreaResultActivity.this, "지역정보를 가져오지 못했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public  class ItemDeserializer implements JsonDeserializer<AreaBase.Items> {

        @Override
        public AreaBase.Items deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            try {
                JsonElement monumentElement = json.getAsJsonObject().get("item");
                if (monumentElement.isJsonArray()) { // array 일 경우
                    return new AreaBase.Items((AreaBaseItem[]) context.deserialize(monumentElement.getAsJsonArray(), AreaBaseItem[].class));
                } else if (monumentElement.isJsonObject()) { // object 일 경우
                    return new AreaBase.Items((AreaBaseItem) context.deserialize(monumentElement.getAsJsonObject(), AreaBaseItem.class));
                } else {
                    throw new JsonParseException("Unsupported type of monument element");
                }
            } catch (IllegalStateException e) {
                return null;
            }

        }
    }


}
