package com.example.rhkdd.yunal.model.tourType;

import java.io.Serializable;

/**
 * Created by rhkdd on 2018-03-20.
 */

public class FestivalItem implements Serializable {

    public String agelimit; // 관람 가능 연령
    public String discountinfofestival; // 할인 정보 ("")
    public String eventplace; // 행사 장소?
    public String placeinfo; // 행사장 위치 안내 ("")
    public String sponsor1; // 주최자 정보?
    public String spendtimefestival; // 관람 소요 시간
    public String program; // 행사 프로그램 ("")
    public String playtime; // 행사 기간 ("")
    public String subevent; // 부대 행사 ("")
    public String usetimefestival; // 이용 요금 ("")

}
