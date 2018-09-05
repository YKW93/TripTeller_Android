package com.example.rhkdd.yunal.data.detailCommon;

/**
 * Created by rhkdd on 2018-03-11.
 */

public class DetailCommon {
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
        public DetailCommonItem item;
    }
}
