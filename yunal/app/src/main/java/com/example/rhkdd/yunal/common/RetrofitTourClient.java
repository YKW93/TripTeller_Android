package com.example.rhkdd.yunal.common;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rhkdd.yunal.SearchActivity.API_BASE_URL;

public class RetrofitTourClient { // 싱글톤

    private Retrofit retrofit;
    private static RetrofitTourClient instance = null;

    public static RetrofitTourClient getInstance() {
        if (instance == null) {
            instance = new RetrofitTourClient();
        }

        return instance;
    }

    private RetrofitTourClient() {

    }

    public TourApiService getService(Gson gson) {

        if (gson != null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // 파싱등록
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                    .build();
        }

        TourApiService service = retrofit.create(TourApiService.class);

        return service;
    }





}