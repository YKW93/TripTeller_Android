package com.example.rhkdd.yunal.model.tourDetail;

import java.io.Serializable;

public class TourInfoItem implements Serializable {

    public int content_id; // 여행지 contentid값
    public float star; // 별점 평점
    public int review; // 후기 갯수
    public boolean mark; // 사용자가 찜을 눌렀는지 안눌렀는지
    public int mark_cnt;
}
