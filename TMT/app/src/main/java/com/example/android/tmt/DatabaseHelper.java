package com.example.android.tmt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="User.db";
    public static final String TABLE_NAME="User_table";

    public static final String Col_1="ID";
    public static final String Col_2="Category";
    public static final String Col_3="Date";
    public static final String Col_4="S_Time";
    public static final String Col_5="E_Time";
    public static final String Col_6="Rating";



    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Category TEXT, Date TEXT, S_Time TEXT, E_Time TEXT, Rating TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String category, String date, String s_time, String e_time, String rating){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Col_2,category);
        contentValues.put(Col_3,date);
        contentValues.put(Col_4,s_time);
        contentValues.put(Col_5,e_time);
        contentValues.put(Col_6,rating);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if (result==-1)
            return false;
        else
            return true;

    }
    public Cursor getData(){
        SQLiteDatabase db=this.getReadableDatabase();
        String[] column = {Col_2,Col_4,Col_5,Col_6};
        Cursor cursor = db.query(TABLE_NAME,column,null,null,null,null,null);
        return cursor;
    }
}
