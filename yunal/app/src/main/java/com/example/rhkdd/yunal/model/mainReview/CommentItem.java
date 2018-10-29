package com.example.rhkdd.yunal.model.mainReview;

import java.io.Serializable;

public class CommentItem implements Serializable {
    public int review_pk; // 구속되어있는 리뷰 pk값
    public String author; //작성자 이름
    public String content; // 작성 내용
    public String image; // 사용자 프로필 이미지
    public String created_at; // 리뷰 작성 날짜
}
