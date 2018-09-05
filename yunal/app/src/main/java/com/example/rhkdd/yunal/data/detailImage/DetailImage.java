package com.example.rhkdd.yunal.data.detailImage;

import java.util.ArrayList;

/**
 * Created by rhkdd on 2018-02-17.
 */

public class DetailImage {
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
        public Item items;
        public int numOfRows;
        public int pageNo;
        public int totalCount;
    }

    public class Item {
        public ArrayList<DetailImageItem> item;
    }
}
