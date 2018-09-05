package com.example.rhkdd.yunal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.common.RetrofitClient;
import com.example.rhkdd.yunal.common.TourApiService;
import com.example.rhkdd.yunal.data.areaBase.AreaBase;
import com.example.rhkdd.yunal.data.areaBase.AreaBaseItem;
import com.example.rhkdd.yunal.dialog.SelectAreaMainBottomSheet;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rhkdd.yunal.SearchActivity.API_BASE_URL;
import static com.example.rhkdd.yunal.SearchActivity.API_key;
import static com.example.rhkdd.yunal.SelectAreaActivity.AREA_CODE;
import static com.example.rhkdd.yunal.SelectAreaActivity.AREA_NAME;
import static com.example.rhkdd.yunal.SelectAreaActivity.SIGUNGU_CODE;
import static com.example.rhkdd.yunal.SelectAreaActivity.SIGUNGU_NAME;

public class SelectAreaMainActivity extends AppCompatActivity {

    final static String TAG = "SelectAreaMainActivity";


    private String areaCode;
    private String sigunguCode;
    private String areaName;
    private String sigunguName;

    private ArrayList<AreaBaseItem> areaBaseItems;

    private ProgressBar progressBar;

    private int currentPage = 1;

    private TourResultRVAdapter tourResultRVAdapter;
    private GridLayoutManager gridLayoutManager;

    private LocalTellerWritingVP localTellerWritingVP;

    private String arrange;
    private String contentTypeId;

    private Boolean checkRun = true;

    public static Intent newIntent(Context context, String areaCode, String sigunguCode, String areaName, String sigunguName) {

        Intent intent = new Intent(context, SelectAreaMainActivity.class);
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

        areaBaseItems = new ArrayList<>();

        // default 값 설정
        arrange = "O"; // (초기 여행지) 데이터 정렬 제목순으로 셋팅
        contentTypeId = null;

        progressBar = findViewById(R.id.progressBar);


        // 툴바 셋팅
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        // intent 정보 받아오기
        Intent intent = getIntent();
        areaCode = intent.getStringExtra(AREA_CODE);
        sigunguCode = intent.getStringExtra(SIGUNGU_CODE);
        areaName = intent.getStringExtra(AREA_NAME);
        sigunguName = intent.getStringExtra(SIGUNGU_NAME);

        // 정보 처리
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        if (sigunguCode != null & sigunguName != null) { // 광역시 , 시군구 모든 정보
            toolbar_title.setText(areaName + "/" + sigunguName);
        } else { // 광역시 정보
            toolbar_title.setText(areaName + " 전체");
        }

        // 리사이클러뷰 셋팅
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false); // ScrollView 안에 RecyclerView 설정 시, Scroll이 부드럽게 되지 않는 경우가 발생해서 해당 구문 필요
        gridLayoutManager = new GridLayoutManager(SelectAreaMainActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        tourResultRVAdapter = new TourResultRVAdapter(SelectAreaMainActivity.this);
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
        viewPager.setPadding(6,0,6,0);
        viewPager.setPageMargin(-5);
        localTellerWritingVP = new LocalTellerWritingVP(SelectAreaMainActivity.this);
        viewPager.setAdapter(localTellerWritingVP);

        // 여행정보 가져오기
        loadAreaData(currentPage, arrange, contentTypeId);

        // 클릭 리스너
        toolbar.findViewById(R.id.toolbar_title).setOnClickListener(onClickListener);
        findViewById(R.id.filter_btn).setOnClickListener(onClickListener);

    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.toolbar_title :
                    Intent intent1 = new Intent(SelectAreaMainActivity.this, SelectAreaActivity.class);
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

    public void loadAreaData(int page, String arrange, String contentTypeId) {
        progressBar.setVisibility(View.VISIBLE);


        SelectAreaMainActivity.ItemDeserializer itemDeserializer = new SelectAreaMainActivity.ItemDeserializer();
        Gson gson = new GsonBuilder().registerTypeAdapter(AreaBase.Items.class, itemDeserializer).create();



        Call<AreaBase> call = RetrofitClient.getInstance().getService(gson).AreaBase(API_key, "yunal",
                "AND", "json", page, 10, areaCode, sigunguCode, arrange, contentTypeId);
        Log.e(TAG, call.request().url().toString());
        call.enqueue(new Callback<AreaBase>() {
            @Override
            public void onResponse(Call<AreaBase> call, Response<AreaBase> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AreaBase areaBaseData = response.body();
                    if (areaBaseData != null && areaBaseData.response.body.items != null) {
                        areaBaseItems.addAll(areaBaseData.response.body.items.item);
                        tourResultRVAdapter.setData(areaBaseItems);
                    } else { // 관광지 데이터 없음
                        Toasty.error(SelectAreaMainActivity.this, "데이터 없음", Toast.LENGTH_SHORT).show();
                        // 데이터 없다는 다이얼로그 뿌려주기~!
                    }
                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<AreaBase> call, Throwable t) {

                Log.e(TAG,"요청 메시지 :"+call.request());
                Log.e(TAG,"지역정보 불러오기 실패 :" + t.getMessage() );
                Toasty.error(SelectAreaMainActivity.this, "지역정보를 가져오지 못했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class TourResultRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private ArrayList<AreaBaseItem> list;

        public TourResultRVAdapter(Context context) {
            this.mContext = context;
            this.list = new ArrayList<>();
        }

        public void setData(ArrayList<AreaBaseItem> data) {
            this.list.clear();
            this.list.addAll(data);
            notifyDataSetChanged();
        }

        class TourResultVH extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView thumbIV;
            private TextView nameTV;

            TourResultVH(View itemView) {
                super(itemView);
                thumbIV = itemView.findViewById(R.id.thumb);
                nameTV = itemView.findViewById(R.id.name);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (checkRun) {

                    checkRun = false;

                    int contentid = list.get(getAdapterPosition()).contentid;
                    Intent intent = DetailActivity.newIntent(SelectAreaMainActivity.this, contentid);
                    startActivity(intent);

                    checkRun = true;
                }
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TourResultVH(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_datatype1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TourResultVH tourResultVH = (TourResultVH) holder;

            if (list.get(position).firstimage != null) {
                Glide.with(mContext).load(list.get(position).firstimage).into(tourResultVH.thumbIV);
            } else {
                Glide.with(mContext).load(R.drawable.no_image).into(tourResultVH.thumbIV);
            }

            tourResultVH.nameTV.setText(list.get(position).title);

        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }

    }

    private class LocalTellerWritingVP extends PagerAdapter {

        private Context context;

        LocalTellerWritingVP(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_viewpager_localtellerwriting, container, false);
            ImageView imageView = v.findViewById(R.id.mainImage);
            GlideApp.with(context).load(R.drawable.dd).into(imageView);

            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        @Override
        public float getPageWidth(int position) {
            return (0.968f);
        }
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
