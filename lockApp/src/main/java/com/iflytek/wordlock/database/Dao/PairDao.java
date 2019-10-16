package com.iflytek.wordlock.database.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.iflytek.wordlock.database.Util.DatabaseInfo;
import com.iflytek.wordlock.database.Model.Pair;

/**
 * Created by 57628 on 2019/5/22.
 */

public class PairDao extends SQLiteOpenHelper {

    public PairDao(Context context) {
        super(context, DatabaseInfo.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + DatabaseInfo.DATABASE_TABLE + "(id integer primary key autoincrement,name varchar(255),value varchar(255))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int updateData(SQLiteDatabase sqLiteDatabase, Pair pair) {
        ContentValues values = new ContentValues();
        values.put("name", pair.getName());
        values.put("value", pair.getValue());
        return sqLiteDatabase.update(DatabaseInfo.DATABASE_TABLE, values, "id=?", new String[]{String.valueOf(pair.getId())});
    }

    public long addData(SQLiteDatabase sqLiteDatabase, Pair pair) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", pair.getName());
        contentValues.put("value", pair.getValue());
        return sqLiteDatabase.insert(DatabaseInfo.DATABASE_TABLE, null, contentValues);
    }

    public Pair queryData(SQLiteDatabase sqLiteDatabase, int id) {
        Cursor cursor = sqLiteDatabase.rawQuery("select * from pair where id=?",new String[]{String.valueOf(id)});
        Pair pair = new Pair();
        while(cursor.moveToNext()) {
            String name = cursor.getString(1);
            String value = cursor.getString(2);
            pair.setName(name);
            pair.setValue(value);
        }
        cursor.close();
        return pair;
    }

    public List<Pair> getList(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.query(DatabaseInfo.DATABASE_TABLE, null, null, null, null, null, "id ASC");
        List<Pair> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(1);
            String value = cursor.getString(2);
            list.add(new Pair(id, name, value));
        }
        cursor.close();
        return list;
    }

    public void deleteData(SQLiteDatabase sqLiteDatabase, int id) {
        sqLiteDatabase.delete(DatabaseInfo.DATABASE_TABLE, "id=?", new String[]{String.valueOf(id)});
    }

}
