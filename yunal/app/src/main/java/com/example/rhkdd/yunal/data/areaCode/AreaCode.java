package com.example.rhkdd.yunal.data.areaCode;

import java.util.ArrayList;
import java.util.Arrays;

public class AreaCode {

    public Response response;

    public class Response {
        public Header header;
        public Body body;
    }

    public class Header {
        public String resultCode;
        public String resultMsg;
    }

    public class Body {
        public Items items;
        public int numOfRows;
        public int pageNo;
        public int totalCount;
    }

    public static class Items {
        public ArrayList<AreaCodeItem> item;

        public Items(AreaCodeItem... data) { // data 들어오는걸 arraylist로 변환해줌
            item = new ArrayList<>(Arrays.asList(data));
        }
    }


}
