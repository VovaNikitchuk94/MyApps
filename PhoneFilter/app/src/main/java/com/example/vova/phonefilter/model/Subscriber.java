package com.example.vova.phonefilter.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.phonefilter.constans.DBConstans;
import com.example.vova.phonefilter.constans.DBConstans.SubscriberTable;

public class Subscriber extends BaseEntity{

    private String mSubscriberName;
    private String mSubscriberNumber;
    private int mIsBlackListAdded;

    public Subscriber(String subscriberName, String subscriberNumber, int isBlackListAdded) {
        mSubscriberName = subscriberName;
        mSubscriberNumber = subscriberNumber;
        mIsBlackListAdded = isBlackListAdded;
    }

    public Subscriber(String subscriberNumber, int isBlackListAdded) {
        mSubscriberNumber = subscriberNumber;
        mIsBlackListAdded = isBlackListAdded;
    }

    public Subscriber(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_ID)));
        mSubscriberName = cursor.getString(cursor.getColumnIndex(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NAME));
        mSubscriberNumber = cursor.getString(cursor.getColumnIndex(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NUMBER));
        mIsBlackListAdded = cursor.getInt(cursor.getColumnIndex(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_BLACKLIST));
    }

    public String getSubscriberName() {
        return mSubscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        mSubscriberName = subscriberName;
    }

    public String getSubscriberNumber() {
        return mSubscriberNumber;
    }

    public void setSubscriberNumber(String subscriberNumber) {
        mSubscriberNumber = subscriberNumber;
    }

    public int getIsBlackListAdded() {
        return mIsBlackListAdded;
    }

    public void setIsBlackListAdded(int isBlackListAdded) {
        mIsBlackListAdded = isBlackListAdded;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NAME, getSubscriberName());
        values.put(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NUMBER, getSubscriberNumber());
        values.put(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_BLACKLIST, getIsBlackListAdded());
        return values;
    }
}
