package com.iflytek.wordlock.database.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iflytek.wordlock.app.DemoApp;
import com.iflytek.wordlock.database.Model.AppMsg;

import java.util.Random;


/**
 * Created by 57628 on 2019/6/26.
 */

public class AppMsgDao extends SQLiteOpenHelper {

    public AppMsgDao(Context context) {
        super(context, AppMsg.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + AppMsg.DATABASE_TABLE + "(id integer primary key,uid varchar(4096),isregister integer)";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL("insert into " + AppMsg.DATABASE_TABLE + "(id,uid,isregister) Values(1," + String.valueOf((int)(1 + Math.random() * (65536 - 1 + 1))) + ",0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table " + AppMsg.DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public int updateData(SQLiteDatabase sqLiteDatabase, Integer isRegister) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", AppMsg.id);
        contentValues.put("isregister", isRegister);
        return sqLiteDatabase.update(AppMsg.DATABASE_TABLE, contentValues, "id=?", new String[]{AppMsg.ID});
    }

    public boolean isRegister(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.query(AppMsg.DATABASE_TABLE, null, null, null, null, null, "id ASC");
        cursor.moveToNext();
        cursor.getColumnIndex("id");
        Integer isRegister = cursor.getInt(2);
        cursor.close();
        return isRegister != 0;
        }

    public String getUid(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.query(AppMsg.DATABASE_TABLE, null, null, null, null, null, "id ASC");
        cursor.moveToNext();
        cursor.getColumnIndex("uid");
        String uid = cursor.getString(1);
        cursor.close();
        return uid;
    }

}