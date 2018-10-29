package com.example.rhkdd.yunal.common;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitTourClient { // 싱글톤

    private Retrofit retrofit;
    public static final String API_BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
    public static final String API_key = "pjUTyt2nvkg4O9pu5PyjjblvqVC3JHFvTn0cqEKWVE7O0a%2BMTP68wcnQW0dEEZ6hA%2BHSYV03I1%2BRl0Wl6YhfTw%3D%3D";
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
