package com.example.android.tmt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;
import java.util.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;


import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="User.db";
    public static final String TABLE_NAME="User_table";

    public static final String Col_1="ID";
    public static final String Col_2="Category";
    public static final String Col_3="date";
    public static final String Col_4="S_Time";
    public static final String Col_5="E_Time";
    public static final String Col_6="Rating";

    public static final int RATING_SAD=1;
    public static final int RATING_HAPPY=3;
    public static final int RATING_OKEY=2;


    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Category TEXT, date TEXT, S_Time TEXT, E_Time TEXT, Rating Integer)");

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
        Log.v(TAG,"entered");
        SQLiteDatabase db1=this.getReadableDatabase();
//        to get today's date
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        String[] projection={Col_2,Col_3,Col_4,Col_5};

        String[] selectionArgs =new String[]{today};
        String queryString = "SELECT Category, SUM((strftime('%s',e_time)-strftime('%s',s_time))/60) AS Diff FROM  User_table WHERE date=? GROUP BY Category";

        Cursor res = db1.rawQuery(queryString,selectionArgs);
        return res;
    }
    public Cursor getWeekData(){
        Log.v(TAG,"enteredWeek");
        SQLiteDatabase db2=this.getReadableDatabase();
        String todayDate=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String oneWeekDate=getCalculatedDate("yyyy-MM-dd",-7);
        Log.v(TAG,"enteredWeek"+oneWeekDate+" "+todayDate);
//        String qString = "SELECT Category, SUM(e_time-s_time) AS Diff FROM  User_table WHERE date=? GROUP BY Category";
        String qString= "SELECT Category, SUM((strftime('%s',e_time)-strftime('%s',s_time))/60) AS Diff FROM User_table WHERE date BETWEEN'"+oneWeekDate+"'AND'"+todayDate+"'"+"GROUP BY Category";
//        String qString= "SELECT * FROM User_table WHERE date =?";
//        String[] aArgs =new String[]{todayDate,oneWeekDate};

        Cursor c=db2.rawQuery(qString,null);

        return c;
    }
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        Log.v(TAG,"enteredWeeks"+s.format(new Date(cal.getTimeInMillis())));
        return s.format(new Date(cal.getTimeInMillis()));
    }
    public Cursor getBarData(){
        SQLiteDatabase db3=this.getReadableDatabase();
        String myToday=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String[] Args =new String[]{myToday};
        String quString = "SELECT Category, Rating, s_time, e_time FROM  User_table WHERE date=?";
        Cursor b=db3.rawQuery(quString,Args);
        return b;
    }

    public Cursor getPrevData(){
        SQLiteDatabase db4=this.getReadableDatabase();
        String myPrevToday=getCalculatedDate("yyyy-MM-dd",-1);
        String[] Args =new String[]{myPrevToday};
        String quString = "SELECT Category, Rating, s_time, e_time FROM  User_table WHERE date=?";
        Cursor b1=db4.rawQuery(quString,Args);
        return b1;
    }
}

