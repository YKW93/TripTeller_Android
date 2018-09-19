package com.example.rhkdd.yunal.common;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitApiClient {

    public static final String API_BASE_URL = "http://00b99c68.ngrok.io/";

    private Retrofit retrofit;
    private static RetrofitApiClient instance = null;

    public static RetrofitApiClient getInstance() {
        if (instance == null) {
            instance = new RetrofitApiClient();
        }

        return instance;
    }

    private RetrofitApiClient() {

    }

    public TourApiService getService() {


        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .build();


        TourApiService service = retrofit.create(TourApiService.class);

        return service;
    }
}
