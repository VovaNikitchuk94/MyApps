package com.example.vova.phonefilter.constans;

public class DBConstans {

    public static final String DB_NAME = "subscriber_db";
    public static final int DB_VERSION = 1;

    public static final class SubscriberTable {
        public static final String TABLE_NAME = "SubscriberInfo";

        public static final class Cols {
            public static final String SUBSCRIBER_INFO_FIELD_ID = "_id";
            public static final String SUBSCRIBER_INFO_FIELD_PHOTO_URI = "_photo_uri";
            public static final String SUBSCRIBER_INFO_FIELD_NAME = "_name";
            public static final String SUBSCRIBER_INFO_FIELD_NUMBER = "_number";
            public static final String SUBSCRIBER_INFO_FIELD_BLACKLIST = "_blacklist";
        }
    }
}
