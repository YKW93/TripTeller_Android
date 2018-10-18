package com.example.rhkdd.yunal.model.tourType;

import java.io.Serializable;

/**
 * Created by rhkdd on 2018-03-20.
 */

public class FoodItem implements Serializable {

    public String chkcreditcardfood; // 신용카드 가능 여부
    public String opentimefood; // 영업시간
    public String restdatefood; // 쉬는날
    public String parkingfood; // 주차 시설 ("")
    public int kidsfacility; // 어린이 놀이방 (0 : 없음 1: 있음)
    public String smoking; // 금연/흡연 여부
    public String firstmenu; // 대표 메뉴
    public String treatmenu; // 취급 메뉴
    public String packing; // 포장가능
    public String infocenterfood; // 예약안내


}
