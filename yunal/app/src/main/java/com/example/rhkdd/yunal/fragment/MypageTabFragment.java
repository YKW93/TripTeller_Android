package com.example.rhkdd.yunal.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rhkdd.yunal.MyMarkActivity;
import com.example.rhkdd.yunal.MyReviewActivity;
import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.common.GlideApp;
import com.example.rhkdd.yunal.common.RetrofitServerClient;
import com.example.rhkdd.yunal.model.userResponseResult.UserInfoResponseResult;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypageTabFragment extends Fragment {

    private CircleImageView user_profile;
    private TextView user_name;
    private TextView review_cnt;
    private TextView mark_cnt;
    private LinearLayout myReview_layout;
    private LinearLayout myMark_layout;

    private String email_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mypage, container, false);

        user_profile = v.findViewById(R.id.user_profile);
        user_name = v.findViewById(R.id.user_name);
        review_cnt = v.findViewById(R.id.review_cnt);
        mark_cnt = v.findViewById(R.id.mark_cnt);
        myReview_layout = v.findViewById(R.id.myReview_Layout);
        myMark_layout = v.findViewById(R.id.myMark_Layout);

        myReview_layout.setOnClickListener(onClickListener);
        myMark_layout.setOnClickListener(onClickListener);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 휴대폰 내에 저장된 사용자 email 값 가져오기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TripTeller", Context.MODE_PRIVATE);
        email_id = sharedPreferences.getString("userId", "이메일 정보 없음");

        loadUserInfoData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfoData();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.myReview_Layout :
                    Intent intent = new Intent(getActivity(), MyReviewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.myMark_Layout :
                    Intent intent2 = new Intent(getActivity(), MyMarkActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    };
    private void loadUserInfoData() {

        Call<ArrayList<UserInfoResponseResult>> call = RetrofitServerClient.getInstance().getService().UserInfoResponseResult(email_id);
        call.enqueue(new Callback<ArrayList<UserInfoResponseResult>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfoResponseResult>> call, Response<ArrayList<UserInfoResponseResult>> response) {
                if (response.isSuccessful()) {
                    ArrayList<UserInfoResponseResult> userInfoResponseResult = response.body();
                    if (userInfoResponseResult != null) {
                        GlideApp.with(getActivity()).load(userInfoResponseResult.get(0).image).into(user_profile);
                        user_name.setText(userInfoResponseResult.get(0).nickname);
                        review_cnt.setText(String.valueOf(userInfoResponseResult.get(0).review_cnt));
                        mark_cnt.setText(String.valueOf(userInfoResponseResult.get(0).mark_cnt));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserInfoResponseResult>> call, Throwable t) {

            }
        });
    }
}
