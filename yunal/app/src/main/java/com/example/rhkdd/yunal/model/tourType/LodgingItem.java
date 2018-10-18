package com.example.rhkdd.yunal.model.tourType;

import java.io.Serializable;

/**
 * Created by rhkdd on 2018-03-20.
 */

public class LodgingItem implements Serializable {

    public String checkintime; // 체크인
    public String checkouttime; // 체크아웃
    public String chkcooking; // 객실 취사
    public String infocenterlodging; // 예약안내
    public String parkinglodging; // 주차 시설 ("")
    public String pickup; // 픽업 서비스
    public String roomcount; // 객실 수
    public String scalelodging; // 규모
    public String subfacility; // 부대시설(기타) ("")
    public int barbecue; // 바비큐장 여부 (1 : 있음 0 : 없음)
    public int seminar; // 세미나실 여부 (1 : 있음 0 : 없음)
}
