package com.mingjiang.android.app.memo.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hasee on 2016/11/28.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydataa.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table task(taskId INTEGER PRIMARY KEY AUTOINCREMENT,taskDate varchar(60), taskTime varchar(60),taskDetail varchar(60) ,taskStatus varchar(60));";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}