package com.example.rhkdd.yunal.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by rhkdd on 2018-03-29.
 */

/*
커스텀 뷰페이저를 만든 이유는 일반 뷰페이저를 적용하고 핀치 줌을 적용해서
이미지를 최대로 줄였다가 옆으로 슬라이드를 하면 앱이 오류가 뜨면서 꺼진다. (간혹 java.lang.IllegalArgumentException: pointerIndex out of range 에러가 떨어질 때가 있다.
구글링하여 보니 viewpager와 photoview간의 버그라고 한다.)
(즉 viewpager 랑 핀치 줌 기능이 서로 충돌해서 그런듯..)
그래서 onInterceptTouchEvent는 반환값이 true 경우 이벤트를 처리했음을 뜻하고 false는 처리 하지않았음을 뜻함.
결국 false 값을 반환해주면서 에러가 뜨는 값은 try catch 문으로 잡아주고 문제없이 해결(catch문이 예외현상을 잡아주는것이기 때문에 앱이꺼지지 않음.)
 */
public class TouchInterceptViewPager extends ViewPager {

    public TouchInterceptViewPager(@NonNull Context context) {
        super(context);
    }

    public TouchInterceptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }
}
