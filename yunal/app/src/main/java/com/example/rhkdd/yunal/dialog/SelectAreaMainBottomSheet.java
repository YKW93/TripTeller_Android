package com.example.rhkdd.yunal.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SearchResultActivity;
import com.example.rhkdd.yunal.SelectAreaMainActivity;

import info.hoang8f.android.segmented.SegmentedGroup;

public class SelectAreaMainBottomSheet extends BottomSheetDialogFragment {

    private TextView completeBtn;
    private SegmentedGroup segmentedGroup;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private String arrange;
    private String contentTypeId;
    private Boolean isChecked = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_selectareamain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetDialogFragment = (SelectAreaMainBottomSheet) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag("selectAreaMainBottomSheet");

        completeBtn = view.findViewById(R.id.completeBtn);
        segmentedGroup = view.findViewById(R.id.segmented);
        radioGroup1 = view.findViewById(R.id.radioGroup1);
        radioGroup2 = view.findViewById(R.id.radioGroup2);
        radioGroup3 = view.findViewById(R.id.radioGroup3);

        completeBtn.setOnClickListener(onClickListener);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if (checkId != -1 && isChecked) {
                    isChecked = false;
                    radioGroup2.clearCheck();
                    radioGroup3.clearCheck();
                    switch (checkId) {
                        case R.id.travelRbtn :
                            contentTypeId = String.valueOf(12);
                            break;
                        case R.id.cultureRbtn :
                            contentTypeId = String.valueOf(14);
                            break;
                        case R.id.leisureRbtn :
                            contentTypeId = String.valueOf(28);
                            break;
                    }
                }
                isChecked = true;
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if (checkId != -1 && isChecked) {
                    isChecked = false;
                    radioGroup1.clearCheck();
                    radioGroup3.clearCheck();
                    switch (checkId) {
                        case R.id.lodgmentRbtn :
                            contentTypeId = String.valueOf(32);
                            break;
                        case R.id.shoppingRbtn :
                            contentTypeId = String.valueOf(38);
                            break;
                        case R.id.foodRbtn :
                            contentTypeId = String.valueOf(39);
                            break;
                    }
                }
                isChecked = true;
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if (checkId != -1 && isChecked) {
                    isChecked = false;
                    radioGroup1.clearCheck();
                    radioGroup2.clearCheck();
                    switch (checkId) {
                        case R.id.totalRbtn :
                            contentTypeId = null;
                            break;
                    }
                }
                isChecked = true;
            }
        });
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.completeBtn :
                    int id = segmentedGroup.getCheckedRadioButtonId();
                    if (id == R.id.titleRbtn) { // 제목 순
                        arrange = "O";
                    } else if (id == R.id.popularityRbtn) { // 인기순
                        arrange = "P";
                    } else if (id == R.id.recentModifyRbtn) { // 최근 수정 순
                        arrange = "Q";
                    } else if (id == R.id.registerRbtn) { // 등록 순
                        arrange = "R";
                    } else {
                        arrange = null;
                    }
                    ((SelectAreaMainActivity)getActivity()).resetData(arrange, contentTypeId);
                    bottomSheetDialogFragment.dismiss(); // 프래그먼트 종료
                    break;
            }
        }
    };

}
