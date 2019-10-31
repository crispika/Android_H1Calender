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
import android.os.Looper;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WiFiAutoSync extends Service {
    private Timer timer = null;
    // every 10 seconds, execute timer.scheduleAtFixedRate once
    static final long PERIOD = 10 * 1000;
    // 0 second, between calling timer.scheduleAtFixedRate and executing run
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

        if (null == timer) {
            timer = new Timer();
        }

        timer.scheduleAtFixedRate(new TimerTask() {

            // calling UI handler to show the message
            private Handler updateUI = new Handler() {
                @Override
                public void dispatchMessage(Message message) {
                    super.dispatchMessage(message);
                    showToastMessage();
                }
            };

            @Override
            public void run() {
                try {

                    if (isWifiConnected(getApplicationContext())) {
                        System.out.println("wifi");
                        loadUserInfo();
                        String userid = userId;
                        String usertoken = userToken;

                        Log.e("background service", "===========runnable=======");
                        //updateUI.sendEmptyMessage(0);

                        if (!userid.equals("") && !usertoken.equals("")) {

                            //updateUI.sendEmptyMessage(0);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, DELAY, PERIOD);

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

    public void showToastMessage() {
        Toast.makeText(getApplicationContext(), "sync successfully", Toast.LENGTH_LONG).show();
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
        editor.apply();
    }

    public void syncToCloud(String usertoken, String username) {

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
        Looper.prepare();
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String jsonString = bundle.getString("result");
                ResultFromSync json = gson.fromJson(jsonString, ResultFromSync.class);
                //register success, login
                if (json.code == 201) {
                    Log.e("background service", "===========sync=======");
                    saveUserInfo(json.token, json.userInfo.userid, json.userInfo.email, json.userInfo.username);
                    loadUserInfo();
                    updateEventAndLocationFromSync(userId, json.events, json.locations, dbhelper);
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
    }

    public void updateEventAndLocationFromSync(String userid, List<Event> events, List<Location> locations, sqliteHelper dbhelper) {

        List<Event> localEvents = dbhelper.syncGetAllEventsByUserId(userid);
        HashMap<String, Event> eventMap = new HashMap<String, Event>();
        for (Event localevent : localEvents) {
            eventMap.put(localevent.eventid, localevent);

        }
        for (Event event : events) {
            if (event.isdelete != null && event.isdelete.compareTo("T") == 0) {
                if (eventMap.get(event.eventid) != null) {
                    dbhelper.deleteEventByEventId(event.eventid);
                }
            }
            if (eventMap.get(event.eventid) == null) {
                dbhelper.insertSyncEvent(event);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Event localEvent = eventMap.get(event.eventid);
                if (event.updatetime != null && localEvent.updatetime != null) {
                    try {
                        if (simpleDateFormat.parse(event.updatetime).getTime() > simpleDateFormat.parse(localEvent.updatetime).getTime()) {
                            dbhelper.updateEventFromSyncByEventId(event.eventid, event);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        List<Location> localLocations = dbhelper.syncGetAllLocationsByUserId(userid);
        HashMap<String, Location> locationMap = new HashMap<String, Location>();
        for (Location locallocation : localLocations) {
            locationMap.put(locallocation.locationid, locallocation);
        }
        for (Location location : locations) {
            if (location.isdelete != null && location.isdelete.compareTo("T") == 0) {
                if (locationMap.get(location.locationid) != null) {
                    dbhelper.deleteLocationByLocationId(location.locationid);
                }
            }
            if (locationMap.get(location.locationid) == null) {
                dbhelper.insertSyncLocation(location);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.e("background service", "===========destroy=======");
        super.onDestroy();
    }
}
