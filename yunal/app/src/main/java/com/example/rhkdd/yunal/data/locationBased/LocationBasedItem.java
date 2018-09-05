package com.example.rhkdd.yunal.data.locationBased;

import java.io.Serializable;

/**
 * Created by rhkdd on 2018-05-01.
 */

public class LocationBasedItem implements Serializable {

    public int contentid;
    public String title; // 제목
    public String addr1; // 주소
    public String addr2; // 상세 주소
    public int dist; // 거리(단위:m)
    public String firstimage; // 대표 이미지
}
