package com.example.rhkdd.yunal.common;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitServerClient {

    public static final String API_BASE_URL = "http://gelos12.iptime.org/";

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

    public TourApiService getService() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();


        TourApiService service = retrofit.create(TourApiService.class);

        return service;
    }
}
