package com.dazzcoder.morenews.db;/**
 * Created by zc on 2016/1/4.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dazzcoder.morenews.utils.Contacts;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2016-01-04
 * Time: 19:38
 * ReadMe:
 */

public class FavoriteDataBaseHelper extends SQLiteOpenHelper {
    public static final String FAVORITE_TABLE = "favorite";
    public static final String HISTORY_TABLE = "history";
    public static final String DB_NAME = "name";
    public static final String DB_ID = "id";
    public static final String DB_DATE = "date";

    public static final String CREATE_FAVORITE = "create table "+FAVORITE_TABLE+" (" + "_id integer primary key autoincrement, "
            + DB_NAME + " text, " + DB_ID + " text," + DB_DATE + " text)";

    public static final String CREATE_HISTORY = "create table "+HISTORY_TABLE+" (" + "_id integer primary key autoincrement, "
            + DB_NAME + " text, " + DB_ID + " text," + DB_DATE + " text)";

    private String dbName;
    public FavoriteDataBaseHelper(Context context,String DB_NAME){
        super(context, DB_NAME, null, 1);
        dbName = DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        switch (dbName){
            case Contacts.HISTORY:
                db.execSQL(CREATE_HISTORY);
                break;
            case Contacts.FAVORITE:
                db.execSQL(CREATE_FAVORITE);
                break;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
