package com.dazzcoder.morenews.db;/**
 * Created by zc on 2016/1/4.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.bean.NewsClassification;
import com.dazzcoder.morenews.utils.Contacts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2016-01-04
 * Time: 20:19
 * ReadMe:
 */

public class FavoriteDataBase {
    Context mContext;
    SQLiteOpenHelper dataBaseHelper;
    String TABLE_NAME = "";

    public FavoriteDataBase(Context mContext, String DB_NAME) {
        this.mContext = mContext;
        this.dataBaseHelper = new FavoriteDataBaseHelper(mContext,DB_NAME);
        switch (DB_NAME){
            case Contacts.FAVORITE:
                TABLE_NAME = FavoriteDataBaseHelper.FAVORITE_TABLE;
                break;
            case Contacts.HISTORY:
                TABLE_NAME = FavoriteDataBaseHelper.HISTORY_TABLE;
                break;
        }
    }

    /**
     * 增
     *
     * @param data
     */
    public void insert(News data) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteDataBaseHelper.DB_NAME, data.getTitle());
        values.put(FavoriteDataBaseHelper.DB_ID, data.getDocid());
        values.put(FavoriteDataBaseHelper.DB_DATE, data.getPubDate());
        db.insert(TABLE_NAME, null, values);
        values.clear();
        db.close();
    }


    /**
     * 删
     *
     * @param where
     */
    public void delete(String where) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "name=?", new String[]{where});
        db.close();
    }

    /**
     * 改
     *
     * @param where
     * @param data
     */
    public void update(int where, News data) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteDataBaseHelper.DB_NAME, data.getTitle());
        values.put(FavoriteDataBaseHelper.DB_ID, data.getDocid());
        values.put(FavoriteDataBaseHelper.DB_DATE, data.getPubDate());
        db.update(TABLE_NAME, values, "_id=?", new String[]{String.valueOf(where)});
        db.close();
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<News> query(){

        return query(" ");
    }

    /**
     * 查
     * @param where
     * @return
     */
    public List<News> query(String where) {
        ArrayList<News> list = new ArrayList<>();
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("select * from "
                + TABLE_NAME + where, null);
        while (cursor.moveToNext()) {
            News data = new News();
            data.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteDataBaseHelper.DB_NAME)));
            data.setDocid(cursor.getString(cursor.getColumnIndex(FavoriteDataBaseHelper.DB_ID)));
            data.setPubDate(cursor.getString(cursor.getColumnIndex(FavoriteDataBaseHelper.DB_DATE)));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }
    public void clear(){
        SQLiteDatabase sqlite = dataBaseHelper.getWritableDatabase();
        // 删除全部
        sqlite.execSQL("delete from " + TABLE_NAME);
        sqlite.close();
    }

    public boolean exists(News data){
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{FavoriteDataBaseHelper.DB_NAME},
                "name=?",
                new String[]{data.getTitle()},
                null,null,null);
        if (cursor==null) return false;

        if (cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }

}
