package com.example.vova.phonefilter.model;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.phonefilter.constans.DBConstans.SubscriberTable;
import com.example.vova.phonefilter.db.DBHelper;

import java.util.ArrayList;

public class SubscriberDBWrapper {

    private DBHelper mDBHelper;
    private String mStrTableName;

    public SubscriberDBWrapper(Context context) {
        mDBHelper = new DBHelper(context);
        mStrTableName = SubscriberTable.TABLE_NAME;
    }

    public String getStrTableName() {
        return mStrTableName;
    }

    public SQLiteDatabase getWritable() {
        return mDBHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadable() {
        return mDBHelper.getReadableDatabase();
    }

    public void addToBlackList(Subscriber subscriber) {
        SQLiteDatabase database = getWritable();
        database.insert(getStrTableName(), null, subscriber.getContentValues());
        database.close();
    }

    public void deleteFromBlackLIst(Subscriber subscriber) {
        SQLiteDatabase database = getWritable();
        String whereClause = SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_ID + "=?";
        String[] whereArgs = new String[] {Long.toString(subscriber.getId())};
        database.delete(getStrTableName(), whereClause, whereArgs);
        database.close();
    }

    public Subscriber getSubscriberByNumber(String number) {
        Subscriber subscriber = null;
        SQLiteDatabase database = getReadable();
        String whereClause = SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NUMBER + "=?";
        String[] whereArgs = new String[] {number};
        Cursor cursor = database.query(getStrTableName(), null, whereClause, whereArgs, null, null, null);
        try {
            if (cursor != null && cursor.moveToNext()) {
                subscriber = new Subscriber(cursor);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return subscriber;
    }

    public ArrayList<Subscriber> getAllSubscribers() {
        ArrayList<Subscriber> subscribers = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getStrTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Subscriber subscriber = new Subscriber(cursor);
                    subscribers.add(subscriber);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return subscribers;
    }
}
