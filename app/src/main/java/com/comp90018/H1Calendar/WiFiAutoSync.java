package com.comp90018.H1Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.utils.Event;
import com.comp90018.H1Calendar.utils.EventSync;
import com.comp90018.H1Calendar.utils.Location;
import com.comp90018.H1Calendar.utils.ResultFromSync;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WiFiAutoSync extends Service {
    private Timer timer = null;
    // each 10 seconds, execute timer.scheduleAtFixedRate once
    static final long PERIOD = 10 * 1000;
    // 0 seconds, between calling timer.scheduleAtFixedRate and executing run
    static final int DELAY = 0;

    // store user info into shared preferences
    private static final String SHAREDPREFS = "sharedPrefs";
    private static final String USERTOKEN = "usertoken";
    private static final String USERID = "userid";
    private static final String USEREMAIL = "useremail";
    private static final String USERNAME = "username";
    private static final String USERPWD = "userpwd";

    // variable used to store user info that get from shared preferences
    private String userToken, userId, userEmail, userName, userPwd;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.e("background service", "===========create=======");
    }

    // background service, showing toast message to indicate whether sync successfully
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (null == timer ) {
            timer = new Timer();
        }

        timer.scheduleAtFixedRate(new TimerTask() {


            // calling UI handler to show the message
            sqliteHelper dbhelper = new sqliteHelper(getApplicationContext());
            List<Event> allCurrentEventList = dbhelper.syncGetAllEventsByUserId(userId);
            // locationList need more word
            List<Location> allCurentLocationList = dbhelper.syncGetAllLocationsByUserId(userId);
            // EventSync
            EventSync eventSync = new EventSync();

            Gson gson1 = new GsonBuilder().serializeNulls().create();
            Gson gson = new Gson();
            String jsonObject = gson1.toJson(eventSync);
            String urlAddress = "http://35.197.167.33:8222/sync";
            Handler handler = new Handler() {
                public void handleMessage(Message msg) {

                    super.handleMessage(msg);
                    eventSync.events = allCurrentEventList;
                    eventSync.locations = allCurentLocationList;
                    eventSync.token = userToken;
                    eventSync.username = userName;
                    Bundle bundle = msg.getData();
                    String jsonString = bundle.getString("result");
                    ResultFromSync json = gson.fromJson(jsonString, ResultFromSync.class);
                    //register success, login
                    if (json.code == 201) {
                        saveUserInfo(json.token, json.userInfo.userid,json.userInfo.email,json.userInfo.username);
                        loadUserInfo();
                        //updateEventAndLocationFromSync(userId, json.events, json.locations);
                        dbhelper.deleteEventByEventIdForReal();
                        dbhelper.deleteLocationByLocationIdForReal();

                    } else if (json.code == 408) {
                        //jumpToNavigationHeaderLogin();

                    } else {

                    }
                }
            };

            @Override
            public void run() {
                if (isWifiConnected(getApplicationContext())){


                    Log.d("start post:", jsonObject);
                    if(urlAddress.equals("") || urlAddress == null) {
                        Log.d("No url:", jsonObject);
                        return;
                    }
                    String res = "";
                    HttpURLConnection connection = null;
                    OutputStream outStream = null;
                    BufferedReader bufferedReader = null;
                    Message msg = null;
                    try{
                        URL url_path = new URL(urlAddress.trim());
                        connection = (HttpURLConnection) url_path.openConnection();
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setUseCaches(false);
                        connection.setInstanceFollowRedirects(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        //获取连接
                        connection.connect();
                        outStream = connection.getOutputStream();
                        outStream.write(jsonObject.getBytes());
                        outStream.flush();
                        outStream.close();
                        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));


                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            res += line;
                        }
                        Log.d("nework", res);
                        bufferedReader.close();
                        msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("result", res);
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    }catch (Exception e){
                        Log.d("nework", "quest problem");
                        e.printStackTrace();
                    }finally {
                        try {
                            if (outStream != null) {
                                outStream.close();
                            }
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            if (connection != null) {
                                connection.disconnect();
                            }
                        } catch (Exception e) {
                            Log.d("nework", "close problem");
                            e.printStackTrace();
                        } }, DELAY, PERIOD);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // check for wifi
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public void showToastMessage(){
        Toast.makeText(getApplicationContext(),"sync successfully", Toast.LENGTH_LONG).show();
    }

    // syncToCloud();

    public void loadUserInfo() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        // the default values of these four variables are ""
        userToken = sharedPreferences.getString(USERTOKEN, "");
        userId = sharedPreferences.getString(USERID, "");
        userEmail = sharedPreferences.getString(USEREMAIL, "");
        userName = sharedPreferences.getString(USERNAME, "");
        userPwd = sharedPreferences.getString(USERPWD, "");

    }

    public void saveUserInfo(String usertoken, String userid, String useremail, String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERTOKEN, usertoken);
        editor.putString(USERID, userid);
        editor.putString(USEREMAIL, useremail);
        editor.putString(USERNAME, username);

        // apply(): apply会把数据同步写入内存缓存，然后异步保存到磁盘，可能会执行失败，失败不会收到错误回调
        // commit(): commit将同步的把数据写入磁盘和内存缓存
        editor.apply();
        //Toast.makeText(this, "data saved", Toast.LENGTH_SHORT).show();
    }

    public void syncToCloud(String usertoken, String username){

        // token
        // userinfo
        // events
        // code 201 /

        // getAllEventsByUserId

        sqliteHelper dbhelper = new sqliteHelper(getApplicationContext());
        List<Event> allCurrentEventList = dbhelper.syncGetAllEventsByUserId(userId);
        // locationList need more word
        List<Location> allCurentLocationList = dbhelper.syncGetAllLocationsByUserId(userId);
        // EventSync
        EventSync eventSync = new EventSync();
        eventSync.events = allCurrentEventList;
        eventSync.locations = allCurentLocationList;
        eventSync.token = usertoken;
        eventSync.username = username;
        Gson gson1 = new GsonBuilder().serializeNulls().create();
        Gson gson = new Gson();
        String jsonObject = gson1.toJson(eventSync);
        String urlAddress = "http://35.197.167.33:8222/sync";
        Log.d("sendJson", jsonObject);
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String jsonString = bundle.getString("result");
                ResultFromSync json = gson.fromJson(jsonString, ResultFromSync.class);
                //register success, login
                if (json.code == 201) {
                    saveUserInfo(json.token, json.userInfo.userid,json.userInfo.email,json.userInfo.username);
                    loadUserInfo();
                    //updateEventAndLocationFromSync(userId, json.events, json.locations);
                    dbhelper.deleteEventByEventIdForReal();
                    dbhelper.deleteLocationByLocationIdForReal();

                    Toast.makeText(getApplicationContext(), "Sync success",
                            Toast.LENGTH_SHORT).show();
                } else if (json.code == 408) {
                    //jumpToNavigationHeaderLogin();
                    Toast.makeText(getApplicationContext(), json.msg,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sync failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        SendPostJson sendPostJson = new SendPostJson(urlAddress, jsonObject, handler);
        sendPostJson.request();
        // Http request

        //Log.d("json", jsonString);
//                [{"day":10,"description":"None","endTimeHour":0,"endTimeMinute":0,"eventId":"509b23f6-a73a-4121-8b80-403681c258e6","isAllday":false,"isNeedNotify":false,"local":"None","month":10,"startTimeHour":0,"startTimeMinute":0,"title":"default1","year":2019},
//                {"day":11,"description":"None","endTimeHour":0,"endTimeMinute":0,"eventId":"e93e3d36-8836-434d-bba7-352846a28eae","isAllday":false,"isNeedNotify":false,"local":"None","month":10,"startTimeHour":0,"startTimeMinute":0,"title":"default2","year":2019},
//                {"day":13,"description":"None","endTimeHour":0,"endTimeMinute":0,"eventId":"9d01331f-ac2a-4e0a-9b8b-04069d1acd90","isAllday":false,"isNeedNotify":false,"local":"None","month":10,"startTimeHour":0,"startTimeMinute":0,"title":"default3","year":2019}]


        // send http


        // 收到反馈

        // sync successfully
        //if(true){
        //    Toast.makeText(getApplicationContext(), "sync successfully",
        //            Toast.LENGTH_SHORT).show();
        //}
        // sync failed
        //else{
        //    Toast.makeText(getApplicationContext(), "sync failed",
        //            Toast.LENGTH_SHORT).show();

        //}


    }

    @Override
    public void onDestroy(){
        Log.e("background service", "===========destroy=======");
        super.onDestroy();
    }
}
