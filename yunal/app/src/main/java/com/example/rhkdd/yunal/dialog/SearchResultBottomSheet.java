package com.example.rhkdd.yunal.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdd.yunal.R;
import com.example.rhkdd.yunal.SearchResultActivity;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by rhkdd on 2018-01-30.
 */

public class SearchResultBottomSheet extends BottomSheetDialogFragment {

    private TextView completeBtn;
    private SegmentedGroup segmentedGroup;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private String arrange;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_searchresult, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

          /*
        프래그먼트를 종료하기 위한 방법
        -findFragmentByTag안의 문자열 값은 상위에서 썻던 태그값과 동일시 해야됨.
         */
        bottomSheetDialogFragment = (SearchResultBottomSheet) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag("searchResultBottomSheet");

        completeBtn = view.findViewById(R.id.completeBtn);
        segmentedGroup = view.findViewById(R.id.segmented);

        completeBtn.setOnClickListener(onClickListener);



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
                    ((SearchResultActivity)getActivity()).resetData(arrange);
                    bottomSheetDialogFragment.dismiss(); // 프래그먼트 종료
                    break;
            }
        }
    };
}
