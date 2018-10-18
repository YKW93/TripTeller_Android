package com.example.rhkdd.yunal.model.tourType;

/**
 * Created by rhkdd on 2018-03-18.
 */

public class DetailIntro {
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

    public static class Items<T> {

//        public int contentTypeId;

        public T item;

        public Items(T data) {
            item = data;
        }


    }
}
