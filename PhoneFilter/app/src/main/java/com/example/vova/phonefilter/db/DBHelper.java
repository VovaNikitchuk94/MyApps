package com.example.vova.phonefilter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vova.phonefilter.constans.DBConstans;
import com.example.vova.phonefilter.constans.DBConstans.SubscriberTable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstans.DB_NAME, null, DBConstans.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + SubscriberTable.TABLE_NAME
                + " (" + SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_PHOTO_URI + " TEXT, "
                + SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NAME + " TEXT, "
                + SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_NUMBER + " TEXT NOT NULL, "
                + SubscriberTable.Cols.SUBSCRIBER_INFO_FIELD_BLACKLIST + " INTEGER NOT NULL); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
