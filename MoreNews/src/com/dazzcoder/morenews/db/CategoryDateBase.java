package com.dazzcoder.morenews.db;/**
 * Created by zc on 2015/12/25.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dazzcoder.morenews.bean.NewsClassification;

import java.util.ArrayList;
import java.util.List;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2015-12-25
 * Time: 18:14
 * ReadMe:新闻分类数据库操作
 */

public class CategoryDateBase{
    SQLiteOpenHelper dataBaseHelper;
    Context mContext;

    public CategoryDateBase(Context context) {
        this.mContext = context;
        dataBaseHelper = new CategotyDataBaseHelper(context);
    }

    /**
     * 增
     *
     * @param data
     */
    public void insert(NewsClassification data) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategotyDataBaseHelper.DB_NAME, data.getName());
        values.put(CategotyDataBaseHelper.DB_ID, data.getId());
        values.put(CategotyDataBaseHelper.DB_URL, data.getApiurl());
        values.put(CategotyDataBaseHelper.DB_INTITLE,data.isIntitle());
        db.insert(CategotyDataBaseHelper.TABLE_NAME, null, values);
        values.clear();
        db.close();
    }


    /**
     * 删
     *
     * @param where
     */
    public void delete(int where) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        db.delete(CategotyDataBaseHelper.TABLE_NAME, "_id=?", new String[]{String.valueOf(where)});
        db.close();
    }

    /**
     * 改
     *
     * @param where
     * @param data
     */
    public void update(int where, NewsClassification data) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategotyDataBaseHelper.DB_NAME, data.getName());
        values.put(CategotyDataBaseHelper.DB_ID, data.getId());
        values.put(CategotyDataBaseHelper.DB_URL, data.getApiurl());
        values.put(CategotyDataBaseHelper.DB_INTITLE,data.isIntitle());
        db.update(CategotyDataBaseHelper.TABLE_NAME, values, "_id=?", new String[]{String.valueOf(where)});
        db.close();
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<NewsClassification> query(){

        return query(" ");
    }

    public List<NewsClassification> queryDisplayCategory(){

        return query(" where "+ CategotyDataBaseHelper.DB_INTITLE+"=1");
    }

    public List<NewsClassification> queryOtherCategory(){

        return query(" where "+ CategotyDataBaseHelper.DB_INTITLE+"=0");
    }

    /**
     * 查
     * @param where
     * @return
     */
    public List<NewsClassification> query(String where) {
        ArrayList<NewsClassification> list = new ArrayList<>();
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = null;
       cursor = db.rawQuery("select * from "
                + CategotyDataBaseHelper.TABLE_NAME + where, null);
        while (cursor.moveToNext()) {
            NewsClassification data = new NewsClassification();
            data.setName(cursor.getString(cursor.getColumnIndex(CategotyDataBaseHelper.DB_NAME)));
            data.setId(cursor.getString(cursor.getColumnIndex(CategotyDataBaseHelper.DB_ID)));
            data.setApiurl(cursor.getString(cursor.getColumnIndex(CategotyDataBaseHelper.DB_URL)));
            data.setIntitle(cursor.getInt(cursor.getColumnIndex(CategotyDataBaseHelper.DB_INTITLE)));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }


    public void resave(List<NewsClassification> datas){
        if (datas != null) {
            SQLiteDatabase sqlite = dataBaseHelper.getWritableDatabase();
            // 删除全部
            sqlite.execSQL("delete from " + CategotyDataBaseHelper.TABLE_NAME);
            // 重新添加
            for (NewsClassification data : datas) {
                insert(data);
            }
            sqlite.close();
        }
    }

    public void clear(){
        SQLiteDatabase sqlite = dataBaseHelper.getWritableDatabase();
        // 删除全部
        sqlite.execSQL("delete from " + CategotyDataBaseHelper.TABLE_NAME);
        sqlite.close();
    }
    /**
     * 初始化数据
     * @param datas
     */
    public void initData(List<NewsClassification> datas){
        if (datas != null) {
            SQLiteDatabase sqlite = dataBaseHelper.getWritableDatabase();
            for (NewsClassification data : datas) {
                Log.d("write", "initData: " +data.getName());
                insert(data);
            }
            sqlite.close();
        }
    }
}
