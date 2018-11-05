package com.example.rhkdd.yunal.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.example.rhkdd.yunal.common.UserInfoReturn;
import com.example.rhkdd.yunal.model.mainReview.MainReviewItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rhkdd on 2018-01-10.
 */

public class MainTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final int FRAGMENT_COMMENT = 1000;
    public static final String COMMENT_SIZE = "COMMENT_SIZE";

    private static MainReviewRVAdapter mainReviewRVAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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

        String email_id = UserInfoReturn.getInstance().getUserNicname(getActivity());

        Call<ArrayList<MainReviewItem>> call = RetrofitServerClient.getInstance().getService().MainReviewResponseResult(email_id);
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onRefresh() {
        loadTotalReviewData();
    }


    public static void loadSingleReviewData(String email_id, int pk) {

        Call<ArrayList<MainReviewItem>> call = RetrofitServerClient.getInstance().getService().ReviewResponseResult(email_id, pk);
        call.enqueue(new Callback<ArrayList<MainReviewItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MainReviewItem>> call, Response<ArrayList<MainReviewItem>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MainReviewItem> lists = response.body();
                    if (lists.get(0) != null) {
                        mainReviewRVAdapter.changeData(lists.get(0));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MainReviewItem>> call, Throwable t) {

            }
        });
    }
}
