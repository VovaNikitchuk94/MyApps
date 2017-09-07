package com.example.vova.phonefilter.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.phonefilter.constans.DBConstans;
import com.example.vova.phonefilter.db.DBHelper;

public class DBWrapper {

    private DBHelper mDBHelper;
    private String mStrTableName;

    public DBWrapper(Context context, String strTableName) {
        mDBHelper = new DBHelper(context);
        mStrTableName = strTableName;
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
        String whereClause = DBConstans.SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_ID + "=?";
        String[] whereArgs = new String[] {Long.toString(subscriber.getId())};
        database.delete(getStrTableName(), whereClause, whereArgs);
        database.close();
    }
}
