package com.example.rhkdd.yunal.data.areaBase;

import com.example.rhkdd.yunal.data.searchKeyword.SearchKeywordItem;

import java.util.ArrayList;
import java.util.Arrays;

public class AreaBase {

    public Response response;

    public class  Response {
        public Header header;
        public Body body;
    }

    public class Header {
        String resultCode;
        String resultMsg;
    }

    public class Body {
        public Items items;
        public int numOfRows;
        public int pageNo;
        public int totalCount;
    }

    public static class Items {
        public ArrayList<AreaBaseItem> item;

        public Items(AreaBaseItem... data) { // data 들어오는걸 arraylist로 변환해줌
            item = new ArrayList<>(Arrays.asList(data));
        }
    }
}
