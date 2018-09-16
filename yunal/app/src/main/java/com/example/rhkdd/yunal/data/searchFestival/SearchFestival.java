package com.example.rhkdd.yunal.data.searchFestival;

import java.util.ArrayList;

public class SearchFestival {
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

    public class Items {
        public ArrayList<SearchFestivalItem> item;
    }
}
