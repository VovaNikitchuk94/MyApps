package com.example.vova.phonefilter.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.phonefilter.constans.DBConstans.SubscriberTable;

public class Subscriber extends BaseEntity{

    private String mStringPhotoUri;
    private String mSubscriberName;
    private String mSubscriberNumber;
    private int mIsBlackListAdded;

    public Subscriber(String stringPhotoUri, String subscriberName,
                      String subscriberNumber, int isBlackListAdded) {
        mStringPhotoUri = stringPhotoUri;
        mSubscriberName = subscriberName;
        mSubscriberNumber = subscriberNumber;
        mIsBlackListAdded = isBlackListAdded;
    }

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
        mStringPhotoUri = cursor.getString(cursor.getColumnIndex(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_PHOTO_URI));
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

    public String getStringPhotoUri() {
        return mStringPhotoUri;
    }

    public void setStringPhotoUri(String stringPhotoUri) {
        mStringPhotoUri = stringPhotoUri;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_PHOTO_URI, getStringPhotoUri());
        values.put(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NAME, getSubscriberName());
        values.put(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NUMBER, getSubscriberNumber());
        values.put(SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_BLACKLIST, getIsBlackListAdded());
        return values;
    }
}
