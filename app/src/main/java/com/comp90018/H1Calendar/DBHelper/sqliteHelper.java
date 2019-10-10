package com.comp90018.H1Calendar.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.comp90018.H1Calendar.utils.CalenderEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class sqliteHelper extends SQLiteOpenHelper {

    // create a DB
    public static final String DATABASENAME = "H1Calendar.db";

    public sqliteHelper(@Nullable Context context) {
        super(context, DATABASENAME, null, 1);
    }


    // onCreate()方法在数据库文件第一次创建时调用。
    // 如果数据库文件不存在，SQLiteOpenHelper在自动创建数据库后会调用onCreate()方法，在该方法中一般需要创建表、视图等组件
    // 在创建前数据库一般是空的，因此不需要先删除数据库中相关的组件。
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create user table
        String createUserTable = "CREATE TABLE USER(userId char(36) PRIMARY KEY, userName char(20), password char(20), email char(50))";
        sqLiteDatabase.execSQL(createUserTable);

        // create event table
        String createEventTable = "CREATE TABLE EVENT(eventId char(36) PRIMARY KEY, title char(20), isAllday int(20), " +
                "isNeedNotify int(20), date char(20), startTimeHour int(20),startTimeMinute int(20), endTimeHour int(20),endTimeMinute int(20), " +
                "eventColor char(20), local char(20), description char(500),updateTime datetime)";
        // , FOREIGN KEY (userId) REFERENCES USER (userId)
        sqLiteDatabase.execSQL(createEventTable);

        // print log msg
        Log.d("createDB", "successful");
    }

    // 如果数据库文件存在，并且当前版本号高于上次创建或升级的版本号，SQLiteOpenHelper会调用onUpgrade()方法，调用该方法后会更新数据库的版本号。
    // 在onUpgrade()方法中除了创建表、视图等组件外，还需要先删除这些相关的组件，因此，在调用onUpgrade()方法前，数据库是存在的，里面还原许多数据库组建。
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String dropUserTable = "DROP TABLE IF EXISTS USER";
        sqLiteDatabase.execSQL(dropUserTable);

        String dropEventTable = "DROP TABLE IF EXISTS EVENT";
        sqLiteDatabase.execSQL(dropEventTable);

        onCreate(sqLiteDatabase);

        // print log msg
        Log.d("updateDB", "successful");

    }


    // add events
    public boolean insert(CalenderEvent calenderEvent){

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("eventId", calenderEvent.getEventId());
        contentValues.put("title", calenderEvent.getTitle());
        contentValues.put("isAllday", calenderEvent.getIsAllday());
        contentValues.put("isNeedNotify", calenderEvent.getIsNeedNotify());

        // combine day, month, year into date
        int day = calenderEvent.getDay();
        int month = calenderEvent.getMonth();
        int year = calenderEvent.getYear();

        String dateStr = day + "/" + month + "/" + year;
        Log.d("dateeee1", String.valueOf(day) + String.valueOf(month) + String.valueOf(year));

        contentValues.put("date", dateStr);
        contentValues.put("startTimeHour", calenderEvent.getStartTimeHour());
        contentValues.put("startTimeMinute", calenderEvent.getStartTimeMinute());
        contentValues.put("endTimeHour", calenderEvent.getEndTimeHour());
        contentValues.put("endTimeMinute", calenderEvent.getEndTimeMinute());
        contentValues.put("eventColor", calenderEvent.getEventColor());
        contentValues.put("local", calenderEvent.getLocal());
        contentValues.put("description", calenderEvent.getDescription());

        // CalenderEvent中没有updateTime
        // contentValues.put("updateTime", calenderEvent.getUpdateTime());

        long result = sqlitedb.insert("EVENT", null, contentValues);



        if(result == -1){
            return false;
        }
        else{
            return true;
        }

    }


    // 查找
    // get an event by eventId
    public CalenderEvent getEventByEventId(String eventId){
        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        CalenderEvent calenderEvent = new CalenderEvent();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM EVENT WHERE eventId = ?", new String[] {eventId});

        while (cursor.moveToNext()){

            calenderEvent.setEventId(cursor.getString(0));
            calenderEvent.setTitle(cursor.getString(1));
            calenderEvent.setIsAllday((cursor.getInt(2)==1)?true:false);
            calenderEvent.setIsNeedNotify((cursor.getInt(3)==1)?true:false);

            String date = cursor.getString(4);
            String[] subStrings = date.split("/");
            calenderEvent.setDay(Integer.parseInt(subStrings[0]));
            calenderEvent.setMonth(Integer.parseInt(subStrings[1]));
            calenderEvent.setYear(Integer.parseInt(subStrings[2]));

            calenderEvent.setStartTimeHour(Integer.parseInt(cursor.getString(5)));
            calenderEvent.setStartTimeMinute(Integer.parseInt(cursor.getString(6)));
            calenderEvent.setEndTimeHour(Integer.parseInt(cursor.getString(7)));
            calenderEvent.setEndTimeMinute(Integer.parseInt(cursor.getString(8)));
            calenderEvent.setEventColor(cursor.getString(9));
            calenderEvent.setLocal(cursor.getString(10));
            calenderEvent.setDescription(cursor.getString(11));

        }



        return calenderEvent;

    }


    // get all events
    // input: userId
    public List<CalenderEvent> getAllEvents(){
        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        List<CalenderEvent> calenderEventList = new ArrayList<CalenderEvent>();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM EVENT", null);

        while (cursor.moveToNext()){
            CalenderEvent calenderEvent = new CalenderEvent();

            calenderEvent.setEventId(cursor.getString(0));
            calenderEvent.setTitle(cursor.getString(1));
            calenderEvent.setIsAllday((cursor.getInt(2)==1)?true:false);
            calenderEvent.setIsNeedNotify((cursor.getInt(3)==1)?true:false);

            String date = cursor.getString(4);
            String[] subStrings = date.split("/");
            calenderEvent.setDay(Integer.parseInt(subStrings[0]));
            calenderEvent.setMonth(Integer.parseInt(subStrings[1]));
            calenderEvent.setYear(Integer.parseInt(subStrings[2]));

            calenderEvent.setStartTimeHour(Integer.parseInt(cursor.getString(5)));
            calenderEvent.setStartTimeMinute(Integer.parseInt(cursor.getString(6)));
            calenderEvent.setEndTimeHour(Integer.parseInt(cursor.getString(7)));
            calenderEvent.setEndTimeMinute(Integer.parseInt(cursor.getString(8)));
            calenderEvent.setEventColor(cursor.getString(9));
            calenderEvent.setLocal(cursor.getString(10));
            calenderEvent.setDescription(cursor.getString(11));

            calenderEventList.add(calenderEvent);

        }



        return calenderEventList;

    }

    // get events by day
    // day format: dd/MM/yyyy
    public List<CalenderEvent> getEventsByDay(String day){
        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        List<CalenderEvent> calenderEventList = new ArrayList<CalenderEvent>();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM EVENT WHERE date = ?", new String[] {day});

        while (cursor.moveToNext()){
            CalenderEvent calenderEvent = new CalenderEvent();

            calenderEvent.setEventId(cursor.getString(0));
            calenderEvent.setTitle(cursor.getString(1));
            calenderEvent.setIsAllday((cursor.getInt(2)==1)?true:false);
            calenderEvent.setIsNeedNotify((cursor.getInt(3)==1)?true:false);

            String date = cursor.getString(4);
            String[] subStrings = date.split("/");
            calenderEvent.setDay(Integer.parseInt(subStrings[0]));
            calenderEvent.setMonth(Integer.parseInt(subStrings[1]));
            calenderEvent.setYear(Integer.parseInt(subStrings[2]));

            calenderEvent.setStartTimeHour(Integer.parseInt(cursor.getString(5)));
            calenderEvent.setStartTimeMinute(Integer.parseInt(cursor.getString(6)));
            calenderEvent.setEndTimeHour(Integer.parseInt(cursor.getString(7)));
            calenderEvent.setEndTimeMinute(Integer.parseInt(cursor.getString(8)));
            calenderEvent.setEventColor(cursor.getString(9));
            calenderEvent.setLocal(cursor.getString(10));
            calenderEvent.setDescription(cursor.getString(11));

            calenderEventList.add(calenderEvent);

        }



        return calenderEventList;

    }


    // get events by week
    // day format: dd/mm/yyyy
    public List<CalenderEvent> getEventsByWeek(String startDay, String endDay){
        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        List<CalenderEvent> calenderEventList = new ArrayList<CalenderEvent>();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM EVENT WHERE date BETWEEN ? AND ?", new String[] {startDay, endDay});

        while (cursor.moveToNext()){
            CalenderEvent calenderEvent = new CalenderEvent();

            calenderEvent.setEventId(cursor.getString(0));
            calenderEvent.setTitle(cursor.getString(1));
            calenderEvent.setIsAllday((cursor.getInt(2)==1)?true:false);
            calenderEvent.setIsNeedNotify((cursor.getInt(3)==1)?true:false);

            String date = cursor.getString(4);
            String[] subStrings = date.split("/");
            calenderEvent.setDay(Integer.parseInt(subStrings[0]));
            calenderEvent.setMonth(Integer.parseInt(subStrings[1]));
            calenderEvent.setYear(Integer.parseInt(subStrings[2]));

            calenderEvent.setStartTimeHour(Integer.parseInt(cursor.getString(5)));
            calenderEvent.setStartTimeMinute(Integer.parseInt(cursor.getString(6)));
            calenderEvent.setEndTimeHour(Integer.parseInt(cursor.getString(7)));
            calenderEvent.setEndTimeMinute(Integer.parseInt(cursor.getString(8)));
            calenderEvent.setEventColor(cursor.getString(9));
            calenderEvent.setLocal(cursor.getString(10));
            calenderEvent.setDescription(cursor.getString(11));

            calenderEventList.add(calenderEvent);

        }



        return calenderEventList;

    }




    // 删除
    public int deleteDataById(String id){
        SQLiteDatabase sqlitedb = this.getWritableDatabase();
        return sqlitedb.delete("EVENT", "id = ?", new String[] {id});
    }



    // 更新
    public boolean updateData(String id){

        SQLiteDatabase sqlitedb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("col2", id);

        sqlitedb.update("EVENT", contentValues, "ID = ?", new String[] {id});
        return true;

    }
}
