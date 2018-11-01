package com.example.rhkdd.yunal.common;

import com.example.rhkdd.yunal.model.areaBase.AreaBase;
import com.example.rhkdd.yunal.model.areaCode.AreaCode;
import com.example.rhkdd.yunal.model.detailCommon.DetailCommon;
import com.example.rhkdd.yunal.model.detailImage.DetailImage;
import com.example.rhkdd.yunal.model.mainReview.CommentItem;
import com.example.rhkdd.yunal.model.mainReview.MainReviewItem;
import com.example.rhkdd.yunal.model.tourType.DetailIntro;
import com.example.rhkdd.yunal.model.locationBased.LocationBased;
import com.example.rhkdd.yunal.model.searchFestival.SearchFestival;
import com.example.rhkdd.yunal.model.searchKeyword.SearchKeyword;
import com.example.rhkdd.yunal.model.tourDetail.TourInfoItem;
import com.example.rhkdd.yunal.model.tourDetail.TourReviewItem;
import com.example.rhkdd.yunal.model.userResponseResult.LoginResponseResult;
import com.example.rhkdd.yunal.model.userResponseResult.MyMarkResponseResult;
import com.example.rhkdd.yunal.model.userResponseResult.SignupResponseResult;
import com.example.rhkdd.yunal.model.userResponseResult.UserInfoResponseResult;

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

    // 관광 api
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
            @Query("overviewYN") String overviewYN, @Query("mapinfoYN") String mapinfoYN, @Query("firstImageYN") String firstImageYN,
            @Query("areacodeYN") String areacodeYN
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


    // 개인 서버
    @Multipart
    @POST("api/user/signup/") // 회원가입
    Call<SignupResponseResult> SignupResponseResult(
            @Part("nickname") RequestBody nickname, @Part("email") RequestBody email, @Part("password") RequestBody password, @Part MultipartBody.Part photo
    );

    @FormUrlEncoded
    @POST("api/jwt/create") // 로그인
    Call<LoginResponseResult> LoginResponseResult (
            @Field("email") String email, @Field("password") String password
    );

    @GET("api/main") // 메인 리뷰 조회
    Call<ArrayList<MainReviewItem>> MainReviewResponseResult (
            @Query("email") String email
    );

    @GET("api/main") // 리뷰 pk 값을 이용한 한개 아이템 조회
    Call<ArrayList<MainReviewItem>>  ReviewResponseResult (
            @Query("email") String email, @Query("pk") int pk
    );

    @Multipart
    @POST("api/review/") // 관광지 리뷰 작성
    Call<ResponseBody> reviewResponseBody (
            @Header("Authorization") String token, @Part("author") RequestBody author, @Part("content_id") RequestBody content_id, @Part("content") RequestBody content,
            @Part ArrayList<MultipartBody.Part> photo, @Part("star_score") RequestBody star_core, @Part("areacode") RequestBody areacode, @Part("sigungucode") RequestBody sigungucode
    );

    @GET("api/tourist/") // 관광지 평균 별점값, 리뷰 갯수, 찜 클릭 유무 값 리턴
    Call<ArrayList<TourInfoItem>> TourInfoResponseResult(
            @Query("email") String email, @Query("content_id") ArrayList<Integer> content_id
    );

    @GET("api/review") // 관광지 모든 리뷰 리턴
    Call<ArrayList<TourReviewItem>> TourReviewResponseResult(
            @Query("content_id") int content_id
    );

    @GET("api/review") // 지역관광지 리뷰 리턴
    Call<ArrayList<TourReviewItem>> AreaSigunguReviewResponseResult(
            @Query("areacode") int areacode, @Query("sigungucode") int sigungucode
    );

    @GET("api/review") // 지역관광지 리뷰 리턴
    Call<ArrayList<TourReviewItem>> AreaTotalReviewResponseResult(
            @Query("areacode") int areacode
    );

    @GET("api/review") // 유저가 작성한 총 리뷰수 리턴
    Call<ArrayList<TourReviewItem>> MyReviewResponseResult (
            @Query("user") String user
    );

    @FormUrlEncoded
    @POST("api/mark/") // 관광지 찜 마크 유무
    Call<ResponseBody> MarkResponseBody (
            @Field("user") String user, @Field("content_id") int content_id
    );

    @GET("api/mark") // 유저가 찜한 관광지 content_id 리턴
    Call<ArrayList<MyMarkResponseResult>> MyMarkResponseResult (
            @Query("user") String user
    );

    @FormUrlEncoded
    @POST("api/like/") // 좋아요 클릭시
    Call<ResponseBody> LikeResponseBody (
            @Field("review") int review, @Field("user") String user
    );

    @FormUrlEncoded
    @POST("api/comment/") // 좋아요 클릭시
    Call<CommentItem> CommentItemResponseResult (
            @Field("review") int review, @Field("author") String author, @Field("content") String content
    );

    @GET("api/user/")
    Call<ArrayList<UserInfoResponseResult>> UserInfoResponseResult (
            @Query("user") String user
    );





//    @GET("SearchFestival")
//    Call<SearchFestival> TotalFestival ( // 전체 날짜 행사 조회
//            @Query(value = "ServiceKey", encoded = true) String ServiceKey, @Query("MobileApp") String MobileApp, @Query("MobileOS") String MobileOS,
//            @Query("_type") String type, @Query("pageNo") int pageNo, @Query("numOfRow") int numOfRow, @Query("arrange") String arrange, @Query("eventStartDate") int eventStartDate
//    );

}
