package com.example.rhkdd.yunal.common;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitServerClient {

    public static final String API_BASE_URL = "http://ec2-13-124-129-85.ap-northeast-2.compute.amazonaws.com/";

    private Retrofit retrofit;
    private static RetrofitServerClient instance = null;

    public static RetrofitServerClient getInstance() {
        if (instance == null) {
            instance = new RetrofitServerClient();
        }

        return instance;
    }

    private RetrofitServerClient() {

    }

    public ApiService getService() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();


        ApiService service = retrofit.create(ApiService.class);

        return service;
    }
}
