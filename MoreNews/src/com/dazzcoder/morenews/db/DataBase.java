package com.dazzcoder.morenews.db;/**
 * Created by zc on 2016/1/4.
 */

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.dazzcoder.morenews.bean.NewsClassification;

import java.util.List;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2016-01-04
 * Time: 20:32
 * ReadMe:
 */
public interface DataBase {
    SQLiteOpenHelper dataBaseHelper = null;
    Context mContext = null;


    public abstract void insert(Object data);
    public abstract void delete(int where);

    public abstract void update(int where, NewsClassification data);
    public abstract List<?> query();
    public abstract List<?> query(String where);

}
