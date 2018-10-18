package com.example.rhkdd.yunal.model.tourDetail;

import java.io.Serializable;
import java.util.ArrayList;

public class TourReviewItem implements Serializable { // 후기 아이템

    public int pk; // 리뷰 pk 값
    public int content_id;
    public String author;
    public String content;
    public float star_score;
    public String created_at;
    public int like;
    public ArrayList<PhotoItem> photo;
    public int areacode;
    public int sigungucode;
}
