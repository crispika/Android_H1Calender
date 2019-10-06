package com.comp90018.H1Calendar.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;


public class sqliteHelper extends SQLiteOpenHelper {

    public sqliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    // create a DB
    public static final String DATABASENAME = "dbName.db";
    // create a table
    public static final String TABLENAME = "studentTable";
    public static final String col1 = "ID";
    public static final String col2 = "NAME";
    public static final String col3 = "SURNAME";
    public static final String col4 = "MARKS";



//    public sqliteHelper(@Nullable Context context) {
//        super(context, DATABASENAME, null, 1);
//    }


    // onCreate()方法在数据库文件第一次创建时调用。
    // 如果数据库文件不存在，SQLiteOpenHelper在自动创建数据库后会调用onCreate()方法，在该方法中一般需要创建表、视图等组件
    // 在创建前数据库一般是空的，因此不需要先删除数据库中相关的组件。
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create table1
        String sqlStatement = "CREATE TABLE IF NOT EXISTS alarmlist(_id integer primary key autoincrement,title char(20),isAllday int(20)," +
                "isVibrate int(20),year int(20),month int(20),day int(20),startTimeHour int(20),startTimeMinute int(20),"+
                "endTimeHour int(20),endTimeMinute int(20),alarmTime char(20),alarmColor char(20),alarmTonePath char(20),local char(20),"+
                "description char(100),replay char(20))";

        sqLiteDatabase.execSQL(sqlStatement);

    }

    // 如果数据库文件存在，并且当前版本号高于上次创建或升级的版本号，SQLiteOpenHelper会调用onUpgrade()方法，调用该方法后会更新数据库的版本号。
    // 在onUpgrade()方法中除了创建表、视图等组件外，还需要先删除这些相关的组件，因此，在调用onUpgrade()方法前，数据库是存在的，里面还原许多数据库组建。
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(sqLiteDatabase);

    }





    // 增加
    public boolean insert(String name){

        SQLiteDatabase sqlitedb = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, name);

        long result = sqlitedb.insert(TABLENAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }

    }



    // 删除
    public int deleteDataById(String id){
        SQLiteDatabase sqlitedb = this.getWritableDatabase();
        return sqlitedb.delete(TABLENAME, "id = ?", new String[] {id});
    }

    // 查找
    // get all data
    public Cursor getAllData(){
        SQLiteDatabase sqlitedb = this.getWritableDatabase();
        Cursor result = sqlitedb.rawQuery("select * from " + TABLENAME, null);
        return result;

    }

    // 更新
    public boolean updateData(String id){

        SQLiteDatabase sqlitedb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, id);

        sqlitedb.update(TABLENAME, contentValues, "ID = ?", new String[] {id});
        return true;

    }
}
