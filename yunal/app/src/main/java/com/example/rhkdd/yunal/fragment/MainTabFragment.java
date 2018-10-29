package com.example.rhkdd.yunal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.adapter.MainReviewRVAdapter;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.model.mainReview.MainReviewItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rhkdd on 2018-01-10.
 */

public class MainTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private MainReviewRVAdapter mainReviewRVAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainReviewRVAdapter = new MainReviewRVAdapter(getActivity());
        recyclerView.setAdapter(mainReviewRVAdapter);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loadTotalReviewData();

    }

    private void loadTotalReviewData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TripTeller", Context.MODE_PRIVATE);
        String email_id = sharedPreferences.getString("userId", "이메일 정보 없음");

        Call<ArrayList<MainReviewItem>> call = RetrofitServerClient.getInstance().getService().MainReviewResponseResult(email_id);
        Log.d("test1414", String.valueOf(call.request().url()));
        call.enqueue(new Callback<ArrayList<MainReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MainReviewItem>> call, Response<ArrayList<MainReviewItem>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MainReviewItem> mainReviewItems = response.body();
                    if (!mainReviewItems.isEmpty()) {
                        mainReviewRVAdapter.setData(mainReviewItems);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MainReviewItem>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onRefresh() {
        loadTotalReviewData();
    }
}
