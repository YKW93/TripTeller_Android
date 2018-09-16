package com.example.rhkdd.yunal.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rhkdd.yunal.CurrentLocationActivity;
import com.example.rhkdd.yunal.DetailActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SelectAreaActivity;
import com.example.rhkdd.yunal.TotalFestivalActivity;
import com.example.rhkdd.yunal.adapter.AreaDataRankRVAdapter;
import com.example.rhkdd.yunal.adapter.ThisMonthFestivalVPAdapter;
import com.example.rhkdd.yunal.common.RetrofitClient;
import com.example.rhkdd.yunal.common.TourApiService;
import com.example.rhkdd.yunal.data.areaBase.AreaBase;
import com.example.rhkdd.yunal.data.areaBase.AreaBaseItem;
import com.example.rhkdd.yunal.data.searchFestival.SearchFestival;
import com.example.rhkdd.yunal.data.searchFestival.SearchFestivalItem;
import com.example.rhkdd.yunal.data.searchKeyword.SearchKeyword;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rhkdd.yunal.SearchActivity.API_BASE_URL;
import static com.example.rhkdd.yunal.SearchActivity.API_key;

/**
 * Created by rhkdd on 2018-01-10.
 */

public class InfoTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private AreaDataRankRVAdapter areaDataRankRVAdapter;
    private ThisMonthFestivalVPAdapter thisMonthFestivalVPAdapter;

    private static final String TAG = "InfoFragement";

    private ArrayList<SearchFestivalItem> lists;

    private String monthStartDay;
    private String monthEndDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        Button areaBtn = view.findViewById(R.id.areaBtn);
        areaBtn.setOnClickListener(onClickListener);
        Button currentLocationBtn = view.findViewById(R.id.currentLocationBtn);
        currentLocationBtn.setOnClickListener(onClickListener);
        TextView allDataViewTV = view.findViewById(R.id.allDataView);
        allDataViewTV.setOnClickListener(onClickListener);

        // recyclerview 셋팅
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false); // NestScrollView 안에 RecyclerView 설정 시, Scroll이 부드럽게 되지 않는 경우가 발생해서 해당 구문 필요
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        areaDataRankRVAdapter = new AreaDataRankRVAdapter(getActivity());
        recyclerView.setAdapter(areaDataRankRVAdapter);

        // viewpage 셋팅
        ViewPager viewPager = view.findViewById(R.id.viewPage);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(6,0,6,0);
        viewPager.setPageMargin(-5);
        thisMonthFestivalVPAdapter = new ThisMonthFestivalVPAdapter(getActivity());
        viewPager.setAdapter(thisMonthFestivalVPAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMonthStartEndDate();
        loadRankAreaData();
        loadFestivalData();


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.areaBtn :
                    Intent intent = new Intent(getActivity(), SelectAreaActivity.class);
                    startActivity(intent);
                    break;
                case  R.id.currentLocationBtn :
                    GpsOnOffCheck();
                    break;
                case R.id.allDataView :
                    Intent intent1 = TotalFestivalActivity.newIntent(getActivity(), lists);
                    startActivity(intent1);
                    break;
            }
        }
    };

    public void GpsOnOffCheck() {
        //GPS가 켜져있는지 체크
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
            Intent intent1 = new Intent(getActivity(), CurrentLocationActivity.class);
            startActivity(intent1);
        }
    }

    private void loadRankAreaData() {

        final ArrayList<AreaBaseItem> lists = new ArrayList<>();

        Call<AreaBase> call = RetrofitClient.getInstance().getService(null).AreaBase(API_key, "yunal", "AND", "json",
                1, 100, null, null, "P", null);
        call.enqueue(new Callback<AreaBase>() {
            @Override
            public void onResponse(Call<AreaBase> call, Response<AreaBase> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AreaBase areaBaseData = response.body();
                    if (areaBaseData != null && areaBaseData.response.body.items.item != null) {
                        ArrayList<AreaBaseItem> areaBaseItemLists = areaBaseData.response.body.items.item;
                        for (int i = 0; i < areaBaseItemLists.size(); i++) {
                            if (areaBaseItemLists.get(i).contenttypeid != 15) {
                                lists.add(areaBaseItemLists.get(i));
                            }
                            if (lists.size() == 10) {
                                areaDataRankRVAdapter.setData(lists);
                                break;
                            }
                        }
                    }
                }            }

            @Override
            public void onFailure(Call<AreaBase> call, Throwable t) {

            }
        });
    }

    private void loadFestivalData() {
        lists = new ArrayList<>();

        Call<SearchFestival> call = RetrofitClient.getInstance().getService(null).SearchFestival(API_key, "yunal", "AND" , "json",
                1, 500, "P", monthStartDay, monthEndDay);
        Log.d("test1414", String.valueOf(call.request().url()));
        call.enqueue(new Callback<SearchFestival>() {
            @Override
            public void onResponse(Call<SearchFestival> call, Response<SearchFestival> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchFestival searchFestival = response.body();
                    if (searchFestival != null && searchFestival.response.body.items.item != null) {
                        Log.d("test1414", String.valueOf(searchFestival.response.body.items.item.size()));
                        lists.addAll(searchFestival.response.body.items.item);
                        thisMonthFestivalVPAdapter.setData(lists);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchFestival> call, Throwable t) {
                Log.d("test1414", t.getMessage());
            }
        });
    }

    private void getMonthStartEndDate() {

        String monthData;

        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        int month = cal.get(Calendar.MONTH) + 1;
        if (month < 10) {
            monthData = "0" + String.valueOf(month);
        } else {
            monthData = String.valueOf(month);
        }
        String startday = "0" + String.valueOf(cal.getMinimum(Calendar.DAY_OF_MONTH));
        String endDay = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        monthStartDay = year + monthData + startday;
        monthEndDay = year + monthData + endDay;
    }

}
