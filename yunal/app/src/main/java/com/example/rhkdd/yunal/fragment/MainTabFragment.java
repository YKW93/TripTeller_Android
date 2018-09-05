package com.example.rhkdd.yunal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rhkdd.yunal.R;

/**
 * Created by rhkdd on 2018-01-10.
 */

public class MainTabFragment extends Fragment{

    private ImageView touristRecommendBtn;
    private ImageView locationchoiceBtn;
    private ImageView currentLocationBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        touristRecommendBtn = view.findViewById(R.id.main_fragment_touristRecommend);
        locationchoiceBtn = view.findViewById(R.id.main_fragment_locationchoice);
        currentLocationBtn = view.findViewById(R.id.main_fragment_currentLocation);

        touristRecommendBtn.setOnClickListener(onClickListener);
        locationchoiceBtn.setOnClickListener(onClickListener);
        currentLocationBtn.setOnClickListener(onClickListener);


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.main_fragment_touristRecommend :
                    Toast.makeText(getContext(), "여행지 추천", Toast.LENGTH_LONG).show();
                    break;
                case R.id.main_fragment_locationchoice :
                    Toast.makeText(getContext(), "지역 선택", Toast.LENGTH_LONG).show();
                    break;
                case R.id.main_fragment_currentLocation :
                    Toast.makeText(getContext(), "현재 위치", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

}
