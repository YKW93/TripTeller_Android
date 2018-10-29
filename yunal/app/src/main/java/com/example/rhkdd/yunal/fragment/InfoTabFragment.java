package com.example.rhkdd.yunal.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.rhkdd.yunal.CurrentLocationActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SelectAreaActivity;
import com.example.rhkdd.yunal.TotalFestivalActivity;
import com.example.rhkdd.yunal.adapter.AreaDataRankRVAdapter;
import com.example.rhkdd.yunal.adapter.ThisMonthFestivalVPAdapter;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.common.RetrofitTourClient;
import com.example.rhkdd.yunal.model.areaBase.AreaBase;
import com.example.rhkdd.yunal.model.areaBase.AreaBaseItem;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestival;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestivalItem;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rhkdd.yunal.common.RetrofitTourClient.API_key;


/**
 * Created by rhkdd on 2018-01-10.
 */

public class InfoTabFragment extends Fragment {

    private NestedScrollView nestedScrollView;
    private String email_id;
    private RecyclerView recyclerView;
    private AreaDataRankRVAdapter areaDataRankRVAdapter;
    private ThisMonthFestivalVPAdapter thisMonthFestivalVPAdapter;

    private static final String TAG = "InfoFragement";

    private ArrayList<SearchFestivalItem> searchFestivalItems;

    private String monthStartDay;
    private String monthEndDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        LinearLayout areaSearch = view.findViewById(R.id.areaSearch);
        areaSearch.setOnClickListener(onClickListener);
        LinearLayout currentLocation = view.findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(onClickListener);
        LinearLayout allDataView = view.findViewById(R.id.allDataView);
        allDataView.setOnClickListener(onClickListener);

        // recyclerview 셋팅
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false); // NestScrollView 안에 RecyclerView 설정 시, Scroll이 부드럽게 되지 않는 경우가 발생해서 해당 구문 필요
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        areaDataRankRVAdapter = new AreaDataRankRVAdapter(getActivity());
        recyclerView.setAdapter(areaDataRankRVAdapter);


        // viewpage 셋팅
        ViewPager viewPager = view.findViewById(R.id.viewPage);
        viewPager.setClipToPadding(false);
        int padding_dp = 30;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dp = Math.round(padding_dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        viewPager.setPadding(0,0, dp,0);
        thisMonthFestivalVPAdapter = new ThisMonthFestivalVPAdapter(getActivity());
        viewPager.setAdapter(thisMonthFestivalVPAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 휴대폰 내에 저장된 사용자 email 값 가져오기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TripTeller", Context.MODE_PRIVATE);
        email_id = sharedPreferences.getString("userId", "이메일 정보 없음");

        nestedScrollView.scrollTo(0,0); // 액티비티가 열렸을때 맨위 화면으로 보이기위한 작업

        getMonthStartEndDate();
        loadRankAreaData();
        loadFestivalData();


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.areaSearch :
                    Intent intent = new Intent(getActivity(), SelectAreaActivity.class);
                    startActivity(intent);
                    break;
                case  R.id.currentLocation :
                    GpsOnOffCheck();
                    break;
                case R.id.allDataView :
                    Intent intent1 = TotalFestivalActivity.newIntent(getActivity(), searchFestivalItems);
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

        Call<AreaBase> call = RetrofitTourClient.getInstance().getService(null).AreaBase(API_key, "yunal", "AND", "json",
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
        searchFestivalItems = new ArrayList<>();

        Call<SearchFestival> call = RetrofitTourClient.getInstance().getService(null).SearchFestival(API_key, "yunal", "AND" , "json",
                1, 20, "P", monthStartDay, monthEndDay);
        call.enqueue(new Callback<SearchFestival>() {
            @Override
            public void onResponse(Call<SearchFestival> call, Response<SearchFestival> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchFestival searchFestival = response.body();
                    if (searchFestival != null && searchFestival.response.body.items.item != null) {
                        searchFestivalItems.addAll(searchFestival.response.body.items.item);
                        ArrayList<Integer> integers = new ArrayList<>();

                        for (int i = 0; i < searchFestivalItems.size(); i++) {
                            integers.add(searchFestivalItems.get(i).contentid);
                        }
                        loadLikeReviewData(integers);

                    }
                }
            }

            @Override
            public void onFailure(Call<SearchFestival> call, Throwable t) {
            }
        });
    }

    private void loadLikeReviewData(ArrayList<Integer> integers) {

        Call<ArrayList<TourInfoItem>> call = RetrofitServerClient.getInstance().getService().TourInfoResponseBody(email_id, integers);
        call.enqueue(new Callback<ArrayList<TourInfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<TourInfoItem>> call, Response<ArrayList<TourInfoItem>> response) {
                if (response.isSuccessful()) {
                    ArrayList<TourInfoItem> tourInfoItems = response.body();
                    if (tourInfoItems != null && !tourInfoItems.isEmpty()) { // 관광지 리뷰 정보 등이 있을 때
                        thisMonthFestivalVPAdapter.setData(searchFestivalItems, tourInfoItems);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TourInfoItem>> call, Throwable t) {

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
