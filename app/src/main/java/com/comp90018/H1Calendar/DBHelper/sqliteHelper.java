package com.comp90018.H1Calendar.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.comp90018.H1Calendar.MainActivity;
import com.comp90018.H1Calendar.utils.CalenderEvent;
import com.comp90018.H1Calendar.utils.EventLocation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class sqliteHelper extends SQLiteOpenHelper {

    // create a DB
    private static final String DATABASENAME = "H1Calendar.db";
    private static final int DATABASEVERSION = 1;
    Context context;
    // store user info into shared preferences
    private static final String SHAREDPREFS = "sharedPrefs";
    private static final String USERTOKEN = "usertoken";
    private static final String USERID = "userid";
    private static final String USEREMAIL = "useremail";
    private static final String USERNAME = "username";
    private static final String USERPWD = "userpwd";

    // variable used to store user info that get from shared preferences
    private String userToken, userId, userEmail, userName, userPwd;

    // variable used to store user info that get from shared preferences
    // private String userId, userEmail, userPwd;

    public sqliteHelper(Context context) {

        super(context, DATABASENAME, null, DATABASEVERSION);
        this.context = context;
    }


    // onCreate()方法在数据库文件第一次创建时调用。
    // 如果数据库文件不存在，SQLiteOpenHelper在自动创建数据库后会调用onCreate()方法，在该方法中一般需要创建表、视图等组件
    // 在创建前数据库一般是空的，因此不需要先删除数据库中相关的组件。
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create event table
        String createEventTable = "CREATE TABLE EVENT(eventId char(36) PRIMARY KEY, title varchar(50), isAllday int(20), " +
                "isNeedNotify int(20), adate char(20), startTimeHour int(20),startTimeMinute int(20), endTimeHour int(20),endTimeMinute int(20), " +
                "eventColor char(20), eventTime char(50), local char(20), description varchar(500), coordinate char(50), updateTime char(50), userId char(36), locationId char(36), isDelete char(1))";

        sqLiteDatabase.execSQL(createEventTable);

        // create location table
        String createLocationTable = "CREATE TABLE LOCATION(locationId char(36) PRIMARY KEY, locationName varchar(200), coordinate char(50), userId char(36), isDelete char(1))";
        sqLiteDatabase.execSQL(createLocationTable);

        // print log msg
        Log.d("createDB", "successful");
    }

    // 如果数据库文件存在，并且当前版本号高于上次创建或升级的版本号，SQLiteOpenHelper会调用onUpgrade()方法，调用该方法后会更新数据库的版本号。
    // 在onUpgrade()方法中除了创建表、视图等组件外，还需要先删除这些相关的组件，因此，在调用onUpgrade()方法前，数据库是存在的，里面还原许多数据库组建。
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String dropEventTable = "DROP TABLE IF EXISTS EVENT";
        sqLiteDatabase.execSQL(dropEventTable);

        String dropLocationTable = "DROP TABLE IF EXISTS LOCATION";
        sqLiteDatabase.execSQL(dropLocationTable);

        onCreate(sqLiteDatabase);

        // print log msg
        Log.d("updateDB", "successful");

    }


    // add events
    public boolean insert(CalenderEvent calenderEvent) {

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("eventId", calenderEvent.getEventId());
        Log.d("eventId", calenderEvent.getEventId());
        contentValues.put("title", calenderEvent.getTitle());
        contentValues.put("isAllday", calenderEvent.getIsAllday());
        contentValues.put("isNeedNotify", calenderEvent.getIsNeedNotify());

        // combine day, month, year into date
        int day = calenderEvent.getDay();
        // month starts from 0 (0 == Jan.)
        int month = calenderEvent.getMonth() + 1;
        int year = calenderEvent.getYear();

        String dateStr = day + "/" + month + "/" + year;
        Log.d("dateeee1", String.valueOf(day) + String.valueOf(month) + String.valueOf(year));

        contentValues.put("adate", dateStr);
        contentValues.put("startTimeHour", calenderEvent.getStartTimeHour());
        contentValues.put("startTimeMinute", calenderEvent.getStartTimeMinute());
        contentValues.put("endTimeHour", calenderEvent.getEndTimeHour());
        contentValues.put("endTimeMinute", calenderEvent.getEndTimeMinute());
        contentValues.put("eventColor", calenderEvent.getEventColor());
        contentValues.put("eventTime", calenderEvent.getEventTime());
        contentValues.put("local", calenderEvent.getLocal());
        contentValues.put("coordinate", calenderEvent.getCoordinate());
        contentValues.put("description", calenderEvent.getDescription());
        contentValues.put("userId", calenderEvent.getUserId());
        contentValues.put("locationId", calenderEvent.getLocationId());

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        contentValues.put("updateTime", sdf.format(date).toString());
        // F 没有删除
        contentValues.put("isDelete", "F");


        long result = sqlitedb.insert("EVENT", null, contentValues);

        sqlitedb.close();


        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    // lookup
    // get an event by eventId
    public CalenderEvent getEventByEventId(String eventId) {
        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM EVENT WHERE eventId = ? and isDelete = 'F'", new String[]{eventId});

        CalenderEvent calenderEvent = new CalenderEvent();


        while (cursor.moveToNext()) {
            calenderEvent = returnCalenderEvent(cursor);
        }

        cursor.close();
        sqlitedb.close();

        return calenderEvent;

    }


    // get all events by userId
    public List<CalenderEvent> getAllEventsByUserId(String userId) {
        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        List<CalenderEvent> calenderEventList = new ArrayList<CalenderEvent>();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM EVENT WHERE userId = ? and isDelete = 'F'", new String[]{userId});

        while (cursor.moveToNext()) {

            calenderEventList.add(returnCalenderEvent(cursor));

        }

        cursor.close();
        sqlitedb.close();

        return calenderEventList;

    }

    // get events by day
    // day format: dd/MM/yyyy
    public List<CalenderEvent> getEventsByDay(String day) {
        // get current userid
        loadUserInfo();
        String userid = userId;

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        List<CalenderEvent> calenderEventList = new ArrayList<CalenderEvent>();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM EVENT WHERE userId = ? and adate = ? and isDelete = 'F'", new String[]{userid, day});

        while (cursor.moveToNext()) {

            calenderEventList.add(returnCalenderEvent(cursor));

        }

        cursor.close();
        sqlitedb.close();

        return calenderEventList;

    }


    // get events by week
    // day format: dd/mm/yyyy
    public List<CalenderEvent> getEventsByWeek(String startDay, String endDay) {
        // get current userid
        loadUserInfo();
        String userid = userId;

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        List<CalenderEvent> calenderEventList = new ArrayList<CalenderEvent>();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM EVENT WHERE userId = ? and isDelete = 'F' and adate BETWEEN ? AND ?", new String[]{userid, startDay, endDay});

        while (cursor.moveToNext()) {

            calenderEventList.add(returnCalenderEvent(cursor));

        }

        cursor.close();
        sqlitedb.close();

        return calenderEventList;

    }


    // delete
    public boolean deleteEventByEventId(String eventId) {

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", "T");

        // number of rows affected
        int result = sqlitedb.update("EVENT", contentValues, "eventId = ?", new String[]{eventId});

        sqlitedb.close();

        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public void deleteEventByEventIdForReal() {

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        // number of rows affected
        int result = sqlitedb.delete("EVENT", "isDelete = 'T'", null);

        sqlitedb.close();

//        if (result > 0) {
//            return true;
//        } else {
//            return false;
//        }


    }

    // update
    public boolean updateEventByEventId(String eventId, CalenderEvent calenderEvent) {
        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("eventId", calenderEvent.getEventId());
        contentValues.put("title", calenderEvent.getTitle());
        contentValues.put("isAllday", calenderEvent.getIsAllday());
        contentValues.put("isNeedNotify", calenderEvent.getIsNeedNotify());

        // combine day, month, year into date
        int day = calenderEvent.getDay();
        // month starts from 0 (0 == Jan.)
        int month = calenderEvent.getMonth() + 1;
        int year = calenderEvent.getYear();

        String dateStr = day + "/" + month + "/" + year;

        contentValues.put("adate", dateStr);
        contentValues.put("startTimeHour", calenderEvent.getStartTimeHour());
        contentValues.put("startTimeMinute", calenderEvent.getStartTimeMinute());
        contentValues.put("endTimeHour", calenderEvent.getEndTimeHour());
        contentValues.put("endTimeMinute", calenderEvent.getEndTimeMinute());
        contentValues.put("eventColor", calenderEvent.getEventColor());
        contentValues.put("eventTime", calenderEvent.getEventTime());
        contentValues.put("local", calenderEvent.getLocal());
        contentValues.put("coordinate", calenderEvent.getCoordinate());
        contentValues.put("description", calenderEvent.getDescription());
        contentValues.put("userId", calenderEvent.getUserId());
        contentValues.put("locationId", calenderEvent.getLocationId());

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        contentValues.put("updateTime", sdf.format(date).toString());

        // number of rows affected
        int result = sqlitedb.update("EVENT", contentValues, "eventId = ?", new String[]{eventId});

        sqlitedb.close();

        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    // 把default userID 更新到 current userid
    public boolean updateEventsByUserId(String defaultUserId, String currentUserId) {
        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", currentUserId);

        // number of rows affected
        int result = sqlitedb.update("EVENT", contentValues, "userId = ?", new String[]{defaultUserId});

        sqlitedb.close();

        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }


    // assign values getting from db to the object
    public CalenderEvent returnCalenderEvent(Cursor cursor) {

        CalenderEvent calenderEvent = new CalenderEvent();

        calenderEvent.setEventId(cursor.getString(0));
        calenderEvent.setTitle(cursor.getString(1));
        calenderEvent.setIsAllday((cursor.getInt(2) == 1) ? true : false);
        calenderEvent.setIsNeedNotify((cursor.getInt(3) == 1) ? true : false);

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
        calenderEvent.setEventTime(cursor.getString(10));
        calenderEvent.setLocal(cursor.getString(11));
        calenderEvent.setDescription(cursor.getString(12));
        calenderEvent.setCoordinate(cursor.getString(13));
        calenderEvent.setUserId(cursor.getString(15));
        calenderEvent.setLocationId(cursor.getString(16));
        // 14 is update time

        return calenderEvent;

    }

    // location
    // add locations
    public boolean insertLocations(EventLocation eventLocation) {

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("locationId", eventLocation.getLocationId());
        contentValues.put("locationName", eventLocation.getName());
        contentValues.put("coordinate", eventLocation.getCoordinate());
        contentValues.put("userId", eventLocation.getUserId());
        // F == 没有删除
        contentValues.put("isDelete", "F");

        long result = sqlitedb.insert("LOCATION", null, contentValues);

        sqlitedb.close();


        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    // get all locations by userId
    // public List<EventLocation> getAllLocationsByUserId()
    public List<EventLocation> getAllLocationsByUserId(String userId) {
//        loadUserInfo();
//        String userid = userId;

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        List<EventLocation> eventLocationsList = new ArrayList<EventLocation>();

        Cursor cursor = sqlitedb.rawQuery("SELECT * FROM LOCATION WHERE userId = ?", new String[]{userId});

        while (cursor.moveToNext()) {

            eventLocationsList.add(returnEventLocation(cursor));

        }

        cursor.close();
        sqlitedb.close();

        return eventLocationsList;

    }

    // assign values getting from db to the object
    public EventLocation returnEventLocation(Cursor cursor) {
        EventLocation eventLocation = new EventLocation();

        eventLocation.setLocationId(cursor.getString(0));
        eventLocation.setName(cursor.getString(1));
        eventLocation.setCoordinate(cursor.getString(2));
        eventLocation.setUserId(cursor.getString(3));

        return eventLocation;

    }

    // delete location
    public boolean deleteLocationByLocationId(String locationId){

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", "T");

        // number of rows affected
        int result = sqlitedb.update("LOCATION", contentValues, "locationId = ?", new String[]{locationId});

        sqlitedb.close();

        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public void deleteLocationByLocationIdForReal() {

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        // number of rows affected
        int result = sqlitedb.delete("LOCATION", "isDelete = 'T'", null);

        sqlitedb.close();

//        if (result > 0) {
//            return true;
//        } else {
//            return false;
//        }


    }




    public void loadUserInfo() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        // the default values of these four variables are ""
        userToken = sharedPreferences.getString(USERTOKEN, "");
        userId = sharedPreferences.getString(USERID, "");
        userEmail = sharedPreferences.getString(USEREMAIL, "");
        userName = sharedPreferences.getString(USERNAME, "");
        userPwd = sharedPreferences.getString(USERPWD, "");

    }


}
