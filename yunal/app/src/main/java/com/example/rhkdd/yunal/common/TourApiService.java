package com.example.rhkdd.yunal.common;

import com.example.rhkdd.yunal.model.areaBase.AreaBase;
import com.example.rhkdd.yunal.model.areaCode.AreaCode;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommon;
import com.example.rhkdd.yunal.model.detailImage.DetailImage;
import com.example.rhkdd.yunal.model.tourType.DetailIntro;
import com.example.rhkdd.yunal.model.locationBased.LocationBased;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestival;
import com.example.rhkdd.yunal.model.searchKeyword.SearchKeyword;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;
import com.example.rhkdd.yunal.model.tourDetail.TourReviewItem;
import com.example.rhkdd.yunal.model.userResponseResult.LoginResponseResult;
import com.example.rhkdd.yunal.model.userResponseResult.SignupResponseResult;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @FormUrlEncoded
    @POST("api/users/create")
    Call<SignupResponseResult> SignupResponseResult(
            @Field("nickname") String nickname, @Field("email") String email, @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/jwt/create")
    Call<LoginResponseResult> LoginResponseResult (
            @Field("email") String email, @Field("password") String password
    );

    @Multipart
    @POST("api/review/")
    Call<ResponseBody> reviewResponseBody (
            @Header("Authorization") String token, @Part("author") RequestBody author, @Part("content_id") RequestBody content_id, @Part("content") RequestBody content,
            @Part ArrayList<MultipartBody.Part> photo, @Part("star_score") RequestBody star_core, @Part("areacode") RequestBody areacode, @Part("sigungucode") RequestBody sigungucode
    );

    @GET("api/tourist/") // 관광지 평균 별점값, 리뷰 갯수, 찜 클릭 유무 값 리턴
    Call<ArrayList<TourInfoItem>> TourInfoResponseBody(
            @Query("email") String email, @Query("content_id") ArrayList<Integer> content_id
    );

    @GET("api/review") // 관광지 모든 리뷰 리턴
    Call<ArrayList<TourReviewItem>> TourReviewResponseBody (
            @Query("content_id") int content_id
    );

    @FormUrlEncoded
    @POST("api/mark/")
    Call<ResponseBody> MarkResponseBody (
            @Field("user") String user, @Field("content_id") int content_id
    );

    @FormUrlEncoded
    @POST("api/like/")
    Call<ResponseBody> LikeResponseBody (
            @Field("review") int review, @Field("user") String user
    );



//    @GET("SearchFestival")
//    Call<SearchFestival> TotalFestival ( // 전체 날짜 행사 조회
//            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
//            @Query("_type") String type, @Query("pageNo") int pageNo, @Query("numOfRow") int numOfRow, @Query("arrange") String arrange, @Query("eventStartDate") int eventStartDate
//    );

}
