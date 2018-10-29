package com.example.rhkdd.yunal.model.mainReview;

import com.example.rhkdd.yunal.model.tourDetail.PhotoItem;

import java.io.Serializable;
import java.util.ArrayList;

public class MainReviewItem implements Serializable {
    public int pk; //리뷰 pk값
    public int content_id; //관광지 content id 값
    public String author; // 작성자 닉네임
    public String content; // 후기 내용
    public String created_at; // 생성 날짜
    public int like; // 좋아요 갯수
    public ArrayList<PhotoItem> photo; // 후기 사진
    public ArrayList<CommentItem> comment; // 사용자 댓글(리뷰안 댓글)
    public boolean is_like; // 사용자가 해당 좋아요를 눌렀는지 안눌렀는지 판별값
    public String image; // 사용자 프로필 이미지
}
