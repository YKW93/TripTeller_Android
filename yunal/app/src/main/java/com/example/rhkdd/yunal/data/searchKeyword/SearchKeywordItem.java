package com.example.rhkdd.yunal.data.searchKeyword;

import java.io.Serializable;

/**
 * Created by rhkdd on 2018-01-23.
 */

public class SearchKeywordItem implements Serializable{
//    addr1 : "서울특별시 강남구 테헤란로"
//    addr2 : "(역삼동)"
//    areacode : 1
//    cat1 : "A02"
//    cat2 : "A0203"
//    cat3 : "A02030600"
//    contentid : 264570
//    contenttypeid : 12
//    createdtime : 20050623134701
//    firstimage : "http://tong.visitkorea.or.kr/cms/resource/04/1955204_image2_1.jpeg"
//    firstimage2 : "http://tong.visitkorea.or.kr/cms/resource/04/1955204_image3_1.jpeg"
//    mapx : 127.0276129349
//    mapy : 37.4978992118
//    mlevel : 6
//    modifiedtime : 20160525163935
//    readcount : 29842
//    sigungucode : 1
//    title : "강남"
//    zipcode : "06232"

    public String addr1;
    public String addr2;
    public int areacode;
//    public String cat1;
//    public String cat2;
//    public String cat3;
    public int contentid;
    public int contenttypeid;
    public long createdtime;

    public String firstimage;
    public String firstimage2;
    public double mapx;
    public double mapy;
    public int mlevel;
    public String modifiedtime;
    public int readcount;
    public int sigungucode;
    public String title;
    public String zipcode;

    public float rating = (float) 4.0;
    public int review_num = 50;
    public int like_num = 30;






}
