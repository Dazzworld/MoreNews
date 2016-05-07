package com.dazzcoder.morenews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dazzcoder.morenews.utils.Contacts;

public class CategotyDataBaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "CATEGORY";
    public static final String DB_NAME = "name";
    public static final String DB_ID = "channelid";
    public static final String DB_URL = "apiurl";
    public static final String DB_INTITLE="intitle";

    public static final String CREATE_CATEGORY = "create table "+TABLE_NAME+" (" + "_id integer primary key autoincrement, "
            + DB_NAME + " text, " + DB_ID + " text," + DB_URL + " text,"+DB_INTITLE+" integer)";

    public CategotyDataBaseHelper(Context context) {
        super(context, Contacts.NEWS_CATEGORY_DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
