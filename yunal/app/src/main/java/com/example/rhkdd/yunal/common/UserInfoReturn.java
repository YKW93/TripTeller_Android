package com.example.rhkdd.yunal.common;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class UserInfoReturn {

    private static UserInfoReturn instance = null;

    public static UserInfoReturn getInstance() {
        if (instance == null) {
            instance = new UserInfoReturn();
        }
        return instance;
    }

    private UserInfoReturn() {

    }

    public String getUserNicname(Activity activity) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("TripTeller", MODE_PRIVATE);
        String email_id = sharedPreferences.getString("userId", "이메일 정보 없음");

        return email_id;
    }

    public String getUserToken(Activity activity) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("TripTeller", MODE_PRIVATE);
        String token = sharedPreferences.getString("userToken", "토큰 정보 없음");

        return token;
    }
}
