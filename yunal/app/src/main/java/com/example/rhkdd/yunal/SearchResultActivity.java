package com.example.rhkdd.yunal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdd.yunal.adapter.SearchResultsRVAdapter;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.RetrofitTourClient;
import com.example.rhkdd.yunal.common.StatusBarColorChange;
import com.example.rhkdd.yunal.model.searchKeyword.SearchKeyword;
import com.example.rhkdd.yunal.model.searchKeyword.SearchKeywordItem;
import com.example.rhkdd.yunal.dialog.SearchResultBottomSheet;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.common.RetrofitTourClient.API_key;


/**
 * Created by rhkdd on 2018-01-23.
 */

public class SearchResultActivity extends AppCompatActivity {

    public static final String SEARCH_NAME = "SEARCH_NAME";
    private String email_id;
    private RecyclerView resultRV;
    private SearchResultsRVAdapter resultRVAdapter;
    private GridLayoutManager gridLayoutManager;
    private TextView searchNameTV;
    private String search_Name;
    private String arrange;
    private int currentPage = 1;
    private int currentItems;
    private ArrayList<SearchKeywordItem> searchResultLists;
    private ArrayList<Integer> contentIdList;

    private int listPositionData = 0;
    private int listPositionContentId;

    public static Intent newIntent(Context context, String search_key) {
        Intent intent = new Intent(context, SearchResultActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(SEARCH_NAME, search_key);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        //상태바 색상 변경
        StatusBarColorChange.setStatusBarColor(SearchResultActivity.this, getResources().getColor(R.color.status_color));

        Initialize();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 리스트에서 아이템을 클릭하고 DetailActivity -> SearchResultActivity로 넘어 왔을 경우
        loadSingleData(listPositionContentId);
    }

    private void Initialize() {

        // 휴대폰 내에 저장된 사용자 email 값 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("TripTeller", MODE_PRIVATE);
        email_id = sharedPreferences.getString("userId", "이메일 정보 없음");

        searchNameTV = findViewById(R.id.searchname);

        searchResultLists = new ArrayList<>();
        contentIdList = new ArrayList<>();

        arrange = "O"; // 기본 정렬값 제목순으로 지정

        Intent intent = getIntent();
        search_Name = intent.getStringExtra(SEARCH_NAME);

        resultRV = findViewById(R.id.result_recycler);
        gridLayoutManager = new GridLayoutManager(SearchResultActivity.this, 2);
        resultRVAdapter = new SearchResultsRVAdapter(SearchResultActivity.this);
        resultRV.setLayoutManager(gridLayoutManager);

        resultRVAdapter.setLoadMoreListener(new SearchResultsRVAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                resultRV.post(new Runnable() {
                    @Override
                    public void run() {
                        currentItems = gridLayoutManager.getChildCount();
                        currentPage++;
                        loadData(currentPage, search_Name, arrange);
                    }
                });
            }
        });
        resultRV.setAdapter(resultRVAdapter);


        findViewById(R.id.back_btn).setOnClickListener(onClickListener);
        findViewById(R.id.filter_btn).setOnClickListener(onClickListener);
        searchNameTV.setText(Html.fromHtml("<font color='#55c7c3'>" + search_Name + "</font>" + "에 대한 검색 결과"));

        loadData(currentPage, search_Name, arrange);
    }

    public void setPositionData(int position, int contentId) {
        listPositionData = position;
        listPositionContentId = contentId;
    }



    public void resetData(String arrange) {
        currentPage = 1;
        searchResultLists.clear();
        if (arrange != null) {
            this.arrange = arrange;
            loadData(currentPage, search_Name, this.arrange);

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
                        resultRVAdapter.changeData(listPositionData, tourInfoItems.get(0));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TourInfoItem>> call, Throwable t) {

            }
        });
    }

    private void loadData(int page, String keyWord, String arrange) {
        contentIdList.clear();
        //서버에 보내기 작업
        // -------------------------------------------------v------//
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        /*
         http 통신 하기전에 로깅해주는 구문 (즉 http 통신하면서 작동정보 시스템상태등을 기록하는것)
         리스트 항목끝까지 내리게되면 onFailue로 들어가서 문제가 발생하는데 로깅을 해본 결과 "" 빈 문자열이 넘어와서 해당 문제 발생(IllegalStateException)
         ItemDeserializer() 클래스에 try catch 문을 추가해서 해당 오류 제거
         HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
         interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
         OkHttpClient loggingclient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
         */

        //gson이 결국 json을 파싱을 해주는데 도움을 주는건데, 문제는 json item 데이터가 array 올 때도 있고 object 올때도 있어서
        //이것을 각각 구분하여 처리해줘야되기 때문에 해당 ItemDeserializer() 클래스를 gson에 셋팅 해준다.
        // 그럼 레트로핏이 json을 완전히 파싱하기전 (call.enqueue에 들어가기전) 에 해당 클래스의 내용대로 데이터를 처리한다.
        // 그리고 나서 레트로핏이 서버랑 통신을해서 데이터를 가져오게됨!
        SearchResultActivity.ItemDeserializer itemDeserializer = new SearchResultActivity.ItemDeserializer();
        Gson gson = new GsonBuilder().registerTypeAdapter(SearchKeyword.Item.class, itemDeserializer).create();


        // 데이터셋팅을 하고 서버를 보내준다.
        Call<SearchKeyword> call = RetrofitTourClient.getInstance().getService(gson).searchKeyword(API_key,"yunal",
                "AND","json",page,20, keyWord, arrange);
            Log.d("test1414",call.request().url().toString()); //실제 호출하는 url을 볼수 있다
        call.enqueue(new Callback<SearchKeyword>() { // 서버에 데이터를 호출하는 부분
            @Override
            public void onResponse(Call<SearchKeyword> call, Response<SearchKeyword> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchKeyword searchResult = response.body();
                    //searchResult.response.body.items != null 이 경우는 20개씩 파싱해서 데이터를 가져오는데 데이터가 없을 경우 json에서 "" 공백데이터를 주는데
                    //그걸 ItemDeserializer 에서 try~catch로 잡아서 공백데이터일 경우 items에 null 값을 줬기 때문에 그 구문을 체크해서 화면에 안뿌려주게 하기 위한 조건
                    if (searchResult != null && searchResult.response.body.items != null) {
                        if (searchResult.response.body.items.item != null) {


                            int j = 0; // remove 경우 해당 아이템이 앞으로 정렬이 되서 일부 아이템을 건너뛰는 문제를 해결하기 위한 변수
                            for (int i = 0; i < searchResult.response.body.items.item.size(); i++) {
                                if (searchResult.response.body.items.item.get(i-j).contenttypeid == 25) { // 여행코스(관광 타입) 데이터 제외시킴
                                    searchResult.response.body.items.item.remove(i-j);
                                    j++;
                                }
                            }

                            searchResultLists.addAll(searchResult.response.body.items.item);

                            // 위에 for문이랑 합칠경우 remove(리스트에서 제거) 때문에 throwIndexOutOfBoundsException 오류가 발생
                            for (int i = 0; i < searchResultLists.size(); i++) { // 검색한 관광지 contentid 값 저장
                                contentIdList.add(searchResultLists.get(i).contentid);
                            }


                            Call<ArrayList<TourInfoItem>> serverCall = RetrofitServerClient.getInstance().getService().TourInfoResponseResult(email_id, contentIdList);
                            serverCall.enqueue(new Callback<ArrayList<TourInfoItem>>() {
                                @Override
                                public void onResponse(Call<ArrayList<TourInfoItem>> call, Response<ArrayList<TourInfoItem>> response) {
                                    if (response.isSuccessful()) {
                                        ArrayList<TourInfoItem> tourInfoItems = response.body();
                                        if (tourInfoItems != null && !tourInfoItems.isEmpty()) { // 관광지 리뷰 정보 등이 있을 때
                                            resultRVAdapter.setData(searchResultLists, tourInfoItems);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<TourInfoItem>> call, Throwable t) {

                                }
                            });

                        }
                    } else { // 관광지 데이터 없음
                        resultRVAdapter.setMoreDataAvailable(false);
                        // 데이터 없다는 다이얼로그 뿌려주기~!
                        Log.d("test1414", "관광데이터 없음");
                    }
                    resultRVAdapter.notifyDataChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchKeyword> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SearchResultActivity.this, "서버와 연결이 끊어졌습니다.", Toast.LENGTH_LONG).show();

            }
        });
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_btn :
                    finish();
                    break;
                case R.id.filter_btn :
                    SearchResultBottomSheet searchResultBottomSheet = new SearchResultBottomSheet();
                    FragmentManager fm = getSupportFragmentManager();   // FragmentManager를 통해서 Fragment 관리
                    searchResultBottomSheet.show(fm, "searchResultBottomSheet");
                    break;
            }
        }
    };



    // 서버 데이터 요청후 데이터가 array가 들어오는 경우도 있고 object가 들어오는 경우가 있다.(여기서 object가 들어오는 경우는 한개의 데이터만 있을 경우!)
    // 이때 object 들어올 경우 우리는 파싱 셋팅을 arraylist로 해놨기 때문에 object -> arraylist로 변경 해줘야된다.
    // 해당 클래스는 서버가 데이터를 파싱하기전 gson에 해당 클래스를 셋팅 해주고 object가 들어올 경우 arraylist로 변경 해준다.
    // 만약 array 들어올 경우도 arraylist로 변경 해줘야된다!.
    // json에서 array는 arraylist가 아니다. 근데 왜 이전에는 해당 클래스를 안써줘도 array 를 파싱 할수 있느냐 ? 그건 retrofit2 에서
    // array를 arraylist로 자동으로 파싱 해주기 때문이다.( object는 arraylist 파싱을 자동으로 해줄수가 없기때문에 해당 클래스를 써서 gson에 셋팅해줌)
    public  class ItemDeserializer implements JsonDeserializer<SearchKeyword.Item> {

        @Override
        public SearchKeyword.Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            try {
                JsonElement monumentElement = json.getAsJsonObject().get("item");
                if (monumentElement.isJsonArray()) { // array 일 경우
                    return new SearchKeyword.Item((SearchKeywordItem[]) context.deserialize(monumentElement.getAsJsonArray(), SearchKeywordItem[].class));
                } else if (monumentElement.isJsonObject()) { // object 일 경우
                    return new SearchKeyword.Item((SearchKeywordItem) context.deserialize(monumentElement.getAsJsonObject(), SearchKeywordItem.class));
                } else {
                    throw new JsonParseException("Unsupported type of monument element");
                }
            } catch (IllegalStateException e) {
                return null;
            }

        }
    }
}
