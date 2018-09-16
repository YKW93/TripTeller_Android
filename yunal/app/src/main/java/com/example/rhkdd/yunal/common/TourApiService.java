package com.example.rhkdd.yunal.common;

import com.example.rhkdd.yunal.data.areaBase.AreaBase;
import com.example.rhkdd.yunal.data.areaCode.AreaCode;
import com.example.rhkdd.yunal.data.detailCommon.DetailCommon;
import com.example.rhkdd.yunal.data.detailImage.DetailImage;
import com.example.rhkdd.yunal.data.detailIntro.DetailIntro;
import com.example.rhkdd.yunal.data.locationBased.LocationBased;
import com.example.rhkdd.yunal.data.searchFestival.SearchFestival;
import com.example.rhkdd.yunal.data.searchKeyword.SearchKeyword;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rhkdd on 2018-03-18.
 */

public interface TourApiService {

    @GET("searchKeyword")
    Call<SearchKeyword> searchKeyword ( // 쿼리는 서버에 보낼 형식을 맞춰주는거고 , SearchKeyword 서버에서 받을 형식
                                        @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
                                        @Query("_type") String type, @Query("pageNo") int pageNo, @Query("numOfRows") int numOfRows,
                                        @Query("keyword") String keyword, @Query("arrange") String arrange
    );

    @GET("areaCode")
    Call<AreaCode> AreaCode (
            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
            @Query("_type") String type, @Query("pageNo") int pageNo, @Query("numOfRows") int numOfRows, @Query("areaCode") String areaCode
    );

    @GET("areaBasedList")
    Call<AreaBase> AreaBase (
            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
            @Query("_type") String type, @Query("pageNo") int pageNo, @Query("numOfRows") int numOfRows, @Query("areaCode") String areaCode,
            @Query("sigunguCode") String sigunguCode, @Query("arrange") String arrange, @Query("contentTypeId") String contentTypeId
    );

    @GET("detailIntro") // 소개정보 요청
    Call<DetailIntro> detailIntro (
            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
            @Query("_type") String type, @Query("contentId") int contentId, @Query("contentTypeId") int contentTypeId, @Query("introYN") String introYN
    );

    @GET("detailCommon") // 소개정보 요청
    Call<DetailCommon> detailCommon (
            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
            @Query("_type") String type, @Query("contentId") int contentId,  @Query("defaultYN") String defaultYN, @Query("addrinfoYN") String addrinfoYN,
            @Query("overviewYN") String overviewYN, @Query("mapinfoYN") String mapinfoYN, @Query("firstImageYN") String firstImageYN
    );

    @GET("detailImage") // 추가 이미지 요청
    Call<DetailImage> detailImage (
            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
            @Query("_type") String type, @Query("contentId") int contentId
    );

    @GET("locationBasedList")
    Call<LocationBased> locationBased (
            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
            @Query("_type") String type, @Query("mapX") double mapX, @Query("mapY") double mapY, @Query("radius") int radius,
            @Query("arrange") String arrange, @Query("pageNo") int pageNo, @Query("numOfRows") int numOfRows

    );

    @GET("searchFestival")
    Call<SearchFestival> SearchFestival ( // 특정 날짜 행사 조회
            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
            @Query("_type") String type, @Query("pageNo") int pageNo, @Query("numOfRows") int numOfRows, @Query("arrange") String arrange, @Query("eventStartDate") String eventStartDate,
            @Query("eventEndDate") String eventEndDate
    );

//    @GET("SearchFestival")
//    Call<SearchFestival> TotalFestival ( // 전체 날짜 행사 조회
//            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
//            @Query("_type") String type, @Query("pageNo") int pageNo, @Query("numOfRow") int numOfRow, @Query("arrange") String arrange, @Query("eventStartDate") int eventStartDate
//    );

}
