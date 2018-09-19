package com.example.rhkdd.yunal.model.searchKeyword;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rhkdd on 2018-01-31.
 */

public class SearchKeyword {

    public Response response;

    public class Response {
        public Header header;
        public Body body;

    }

    public class Header {
        public String resultCode;
        public String reusultMsg;
    }

    public class Body {
        public Item items;
        public int numOfRows;
        public int pageNo;
        public int totalCount;
    }

    public static class Item {
        public ArrayList<SearchKeywordItem> item;

        public Item(SearchKeywordItem... data) { // data 들어오는걸 arraylist로 변환해줌
            item = new ArrayList<>(Arrays.asList(data));
        }
    }
}
