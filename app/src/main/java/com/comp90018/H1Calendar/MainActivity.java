package com.comp90018.H1Calendar;

import java.io.OutputStream;
import java.io.InputStream;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.EventView.DayEventView;
import com.comp90018.H1Calendar.EventView.WeekEventView;
import com.comp90018.H1Calendar.calendar.CalendarView;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.CalenderEvent;
import com.comp90018.H1Calendar.utils.Event;
import com.comp90018.H1Calendar.utils.EventBus;
import com.comp90018.H1Calendar.utils.EventSync;
import com.comp90018.H1Calendar.utils.Events;
import com.comp90018.H1Calendar.utils.Location;
import com.comp90018.H1Calendar.utils.ResultFromLogin;
import com.comp90018.H1Calendar.utils.ResultFromSync;
import com.comp90018.H1Calendar.utils.UserLogin;
import com.comp90018.H1Calendar.utils.UserRegister;
import com.comp90018.H1Calendar.utils.LightSensorUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.GsonBuilder;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.gson.Gson;

//import android.app.Activity;


public class MainActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private LightSensorUtils lightSensor;

    private DayEventView dayEventView;
    private WeekEventView weekEventView;
    private Button btnDWswitch;
    private ListView leftList;
    private NavigationView myNavigationView;

    // Tao: start here

    private NavigationView navigationView;
    private View loginView, logoutView, registerView;
    private EditText loginusername, loginpwd, registerusername, registeruseremail, registerpwd, registerpwdagain;
    private TextView logoutusername, logoutuseremail;
    private Button loginlogin, loginregister, logoutlogout, logoutsync, registerregister, registercancel;

    // store user info into shared preferences
    private static final String SHAREDPREFS = "sharedPrefs";
    private static final String USERTOKEN = "usertoken";
    private static final String USERID = "userid";
    private static final String USEREMAIL = "useremail";
    private static final String USERNAME = "username";
    private static final String USERPWD = "userpwd";

    // variable used to store user info that get from shared preferences
    private String userToken, userId, userEmail, userName, userPwd;

    // db helper
    sqliteHelper dbhelper;
    // default userId to current userId
    String defaultUserId = "";


    // Tao: end here


    //region Main Layout Widgets

    //侧栏开关
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @OnClick(R.id.open_drawer_btn)
    public void open_drawer() {
        drawer_layout.openDrawer(GravityCompat.START);
    }

    //日历视图开关
    private boolean isOpen = true;

    @BindView(R.id.calendar_view)
    CalendarView calendar_view;

    @OnClick(R.id.hide_calendar)
    public void hide_calendar_view() {
        if (isOpen) {
            calendar_view.setVisibility(View.GONE);
            isOpen = false;
        } else {
            calendar_view.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }

    //Title_bar上的月份标签
    @BindView(R.id.title_month_label)
    TextView title_month_label;

    //Btn for back to today
    @OnClick(R.id.back_to_today)
    public void back_to_today() {
        EventBus.getInstance().send(new Events.BackToToday());
        calendar_view.scrollToDate(CalendarManager.getInstance().getToday(), CalendarManager.getInstance().getWeekList());
    }

    //CalendarView自定义控件的属性
    private int calendar_CurrentDayTextColor, calendar_PastDayTextColor, calendar_HeaderTextColor;

    //FAB 按钮
    private RapidFloatingActionHelper rfabHelper; //Helper for Fab

    @BindView(R.id.main_fab_layout)
    RapidFloatingActionLayout main_fab_layout;

    @BindView(R.id.main_fab_button)
    RapidFloatingActionButton main_fab_button;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawerlayout);
        ButterKnife.bind(this);
        setCalendarInfo();
        initCalendarView();
        setMonthLabel();
        init_FAB();
        initLightUtils();

        dayEventView = new DayEventView();
        weekEventView = new WeekEventView();
        myNavigationView = this.findViewById(R.id.navigation);
        if (myNavigationView != null) {
            myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.dayview:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Event_container, dayEventView).commitAllowingStateLoss();
                            drawer_layout.closeDrawers();
                            //EventBus.getInstance().send();
                            break;
                        case R.id.weeklyview:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Event_container, weekEventView).commitAllowingStateLoss();
                            drawer_layout.closeDrawers();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
        getSupportFragmentManager().beginTransaction().add(R.id.Event_container, dayEventView).commitAllowingStateLoss();


        // Tao: start here

        // start background service - auto sync if wifi is available
//        Intent intent = new Intent(this, WiFiAutoSync.class);
//        startService(intent);

        navigationView = findViewById(R.id.navigation);

        // add three header layouts to navigation view
        loginView = navigationView.inflateHeaderView(R.layout.navigation_header_login);
        logoutView = navigationView.inflateHeaderView(R.layout.navigation_header_logout);
        registerView = navigationView.inflateHeaderView(R.layout.navigation_header_register);

        // widgets
        // login
        loginusername = loginView.findViewById(R.id.et_username);
        loginpwd = loginView.findViewById(R.id.et_pwd);
        loginlogin = loginView.findViewById(R.id.btn_login);
        loginregister = loginView.findViewById(R.id.btn_register);

        // logout
        logoutusername = logoutView.findViewById(R.id.tv_userName);
        logoutuseremail = logoutView.findViewById(R.id.tv_useremail);
        logoutlogout = logoutView.findViewById(R.id.btn_logout);
        logoutsync = logoutView.findViewById(R.id.btn_sync);

        // register
        registerusername = registerView.findViewById(R.id.et_username);
        registeruseremail = registerView.findViewById(R.id.et_useremail);
        registerpwd = registerView.findViewById(R.id.et_pwd);
        registerpwdagain = registerView.findViewById(R.id.et_pwdConfirm);
        registerregister = registerView.findViewById(R.id.btn_register);
        registercancel = registerView.findViewById(R.id.btn_cancel);

        // db helper
        dbhelper = new sqliteHelper(getApplicationContext());

        // load user info from shared preferences
        // assign the values to userToken, userId, userName, userPwd, the default values are ""
        loadUserInfo();

        // user info is available
        if (!userToken.equals("") && !userName.equals("") && !userId.equals("") && !userEmail.equals("") && !userPwd.equals("")) {

            // network is working
            if(isNetworkConnected(getApplicationContext())){

                userValidation(userName, userPwd);

            }
            else{
                Toast.makeText(getApplicationContext(), "network is not available", Toast.LENGTH_SHORT).show();
            }

        }
        // user info is not available
        else {

            jumpToNavigationHeaderLogin();

        }

        // login: login button
        loginlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginUsername = loginusername.getText().toString();
                String loginPwd = loginpwd.getText().toString();

                // network is working
                if(isNetworkConnected(getApplicationContext())) {

                    if (loginUsername.equals("") || loginPwd.equals("")) {

                        Toast.makeText(getApplicationContext(), "username or pwd is missing",
                                Toast.LENGTH_SHORT).show();

                    }
                    else {

                        userValidation(loginUsername, loginPwd);

                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "network is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // login: register button
        loginregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jumpToNavigationHeaderRegister();

            }
        });

        // logout: logout button
        logoutlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveUserInfo("", "", "", "", "");
                loadUserInfo();

                jumpToNavigationHeaderLogin();

            }
        });

        // logout: sync button
        logoutsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check wifi
                if (isWifiConnected(getApplicationContext())) {
                    System.out.println("wifi");
                    // sync
                    syncToCloud(userToken, userName);


                } else if (isMobileConnected(getApplicationContext())) {
                    System.out.println("mobile");
                    //popup dialog
                    syncUnderMobileNetwork();


                } else {

                    Toast.makeText(getApplicationContext(), "network is not available", Toast.LENGTH_SHORT).show();

                }
            }

        });

        // register: register buttion
        registerregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // network is working
                if (isNetworkConnected(getApplicationContext())) {

                    String registerUsername = registerusername.getText().toString();
                    String registerUseremail = registeruseremail.getText().toString();
                    String registerPwd = registerpwd.getText().toString();
                    String registerPwdAgain = registerpwdagain.getText().toString();

                    if (registerUsername.equals("") || registerUseremail.equals("") || registerPwd.equals("") || registerPwdAgain.equals("")) {

                        Toast.makeText(getApplicationContext(), "username, useremail or pwd is missing", Toast.LENGTH_SHORT).show();

                    } else if (!registerPwd.equals(registerPwdAgain)) {

                        Toast.makeText(getApplicationContext(), "passwords are not same", Toast.LENGTH_SHORT).show();

                    } else {
                        String urlAddress = "http://35.197.167.33:8222/register";
                        Gson gson = new Gson();
                        UserRegister userRegister = new UserRegister(registerUsername, registerPwd, registerUseremail);
                        String jsonObject = gson.toJson(userRegister);
                        Handler handler = new Handler() {
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                Bundle bundle = msg.getData();
                                String jsonString = bundle.getString("result");
                                ResultFromLogin json = gson.fromJson(jsonString, ResultFromLogin.class);
                                //register success, login
                                if (json.code == 200) {

                                    saveUserInfo(json.token, json.userInfo.userid,json.userInfo.email,json.userInfo.username,registerPwd);
                                    loadUserInfo();

                                    jumpToNavigationHeaderLogout();
                                    Toast.makeText(getApplicationContext(), json.msg,
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), json.msg,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        SendPostJson sendPostJson = new SendPostJson(urlAddress, jsonObject, handler);
                        sendPostJson.request();
                    }
                }
            }

        });

        // register: cancel buttion
        registercancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jumpToNavigationHeaderLogin();

            }
        });


        // Tao: end here

    }

    @Override
    protected void onResume() {
        super.onResume();
        lightSensor.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lightSensor.unRegister();
    }

    private void initLightUtils(){
        lightSensor = new LightSensorUtils(this);
        lightSensor.setLightListener(new LightSensorUtils.LightListener() {

            @Override
            public void toLight() {
                if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){
                    //change to DayTheme
                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Log.d("Theme_sensor", "Sensor: Switch to Light");
                }
            }

            @Override
            public void toDark() {
                if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO){
                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Log.d("Theme_sensor", "Sensor: Switch to Dark");
                }
            }
        });
    }

//    private void ThemeChange(boolean isDay){
//        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
//            & isDay){
//            //change to DayTheme
//            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            Log.d("Theme_sensor", "Sensor: Switch to Light");
//
//        }
//        else if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO
//            & (!isDay)){
//            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            Log.d("Theme_sensor", "Sensor: Switch to Dark");
//        }
//    }

    //region CalendarView Settings

    /**
     * set the data in the calendar
     */
    private void setCalendarInfo() {
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        //前推10个月
        min.add(Calendar.MONTH, -10);
        min.add(Calendar.DAY_OF_MONTH, 1);

        //后推10个月
        max.add(Calendar.MONTH, 11);
        max.set(Calendar.DAY_OF_MONTH, 1);
        max.add(Calendar.DAY_OF_MONTH, -1);

        Locale locale = Locale.getDefault();

        CalendarManager.getInstance().initCalendar(min, max, locale);
    }

    /**
     * init the style, color, theme of the calendar
     */
    private void initCalendarView() {
        calendar_HeaderTextColor = getColor(R.color.calendar_text_header);
        calendar_CurrentDayTextColor = getColor(R.color.calendar_text_current_day);
        calendar_PastDayTextColor = getColor(R.color.calendar_text_past_day);

        calendar_view.init(calendar_HeaderTextColor, calendar_CurrentDayTextColor, calendar_PastDayTextColor);
    }
    //endregion

    //region Fab Setting
    private void init_FAB() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Add Event by Form")
                .setResId(R.drawable.ic_form)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff283593)
                .setLabelColor(0xff283593)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Add Event by QR Code")
                .setResId(R.drawable.ic_qrcode)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Bookmark Current Location")
                .setResId(R.drawable.ic_location_on_grey_600_24dp)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(1)
        );

        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                main_fab_layout,
                main_fab_button,
                rfaContent
        ).build();
    }


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
    }

    /**
     * 控制点击FAB后，展开列表中按钮的click事件
     *
     * @param position button的序号
     * @param item     列表中button的item
     */
    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        switch (position) {
            case 0:
                Intent intent_to_form = new Intent();
                intent_to_form.setClass(this, AddFormScheduleActivity.class);
                intent_to_form.putExtra("type", "addEvent");
                startActivity(intent_to_form);
                break;
            case 1:
                Intent intent_to_qrcode = new Intent();
                intent_to_qrcode.setClass(this, AddQRCodeScheduleActivity.class);
                startActivity(intent_to_qrcode);
                break;
            case 2:
                Intent intent_to_location = new Intent();
                intent_to_location.setClass(this,AddGPSLocationActivity.class);
                startActivity(intent_to_location);
                break;
            default:
                break;
        }


    }
    //endregion

    private void setMonthLabel() {
        EventBus.getInstance().getSubject().subscribe(event -> {
            if (event instanceof Events.MonthChangeEvent) {
                title_month_label.setText(((Events.MonthChangeEvent) event).getMonthFullName());
            }
        });
    }


    // Tao: start here

    public void saveUserInfo(String usertoken, String userid, String useremail, String username, String userpwd) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERTOKEN, usertoken);
        editor.putString(USERID, userid);
        editor.putString(USEREMAIL, useremail);
        editor.putString(USERNAME, username);
        editor.putString(USERPWD, userpwd);

        // apply(): apply会把数据同步写入内存缓存，然后异步保存到磁盘，可能会执行失败，失败不会收到错误回调
        // commit(): commit将同步的把数据写入磁盘和内存缓存
        editor.apply();
        //Toast.makeText(this, "data saved", Toast.LENGTH_SHORT).show();
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

    public void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        // the default values of these four variables are ""
        userToken = sharedPreferences.getString(USERTOKEN, "");
        userId = sharedPreferences.getString(USERID, "");
        userEmail = sharedPreferences.getString(USEREMAIL, "");
        userName = sharedPreferences.getString(USERNAME, "");
        userPwd = sharedPreferences.getString(USERPWD, "");

    }

    // http post
//        public String sendPost(String urlAddress, String paramValue){
//
//            int responseCode = 0;
//            OutputStream out = null;
//            InputStream in = null;
//            try {
//                URL url = new URL(urlAddress);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoOutput(true);
//                connection.setDoInput(true);
//                connection.setUseCaches(false);
//                connection.setInstanceFollowRedirects(true);
//                connection.setRequestMethod("POST");
//                // 设置接收数据的格式
//                // connection.setRequestProperty("Accept", "application/json");
//                // 设置发送数据的格式
//                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//                connection.connect();
//                out = connection.getOutputStream();
//                out.write(paramValue.getBytes());
//                out.flush();
//                out.close();
//
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//                String line;
//                String res = "";
//                while ((line = reader.readLine()) != null) {
//                    res += line;
//                }
//                reader.close();
//
//                return res;
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            // 自定义错误信息
//            return "error";
//
//        }



    // TODO: user register
//    public boolean userRegister(String registerUseremail, String registerPwd){
//
//        String returnToken, returnUserId, returnUserEmail, returnUserPwd;
//        String result, urlAddress, paramValue;
//        urlAddress = "";
//        paramValue = "{" + '"' + "userEmail" + '"' + ":" + registerUseremail + ", " + '"' + "userPwd" + '"' + ":" + registerPwd + "}";
//
//        //result = sendPost(urlAddress, paramValue);
//
//        // split result or read json
//
//
//        // register is successful
//        if(true){
//            // saveUserInfo(returnUserId, returnUserEmail, returnUserPwd);
//            // loadUserInfo();
//            return true;
//        }
//        // failed
//        else{
//            return false;
//        }
//
//    }

    // TODO: user validation
    // return user info and validation state
    public void userValidation(String username, String password){

        // code: 200 / 401
        // token:
        // userinfo: email, id, username

        Gson gson = new Gson();
        UserLogin userLogin = new UserLogin(username, password);

        String jsonObject = gson.toJson(userLogin);
        Log.d("abiosdjifos", jsonObject);



        // variables used to store user info that returned by cloud
        //String returnUserId, returnUserEmail, returnUserPwd;
        String  returnToken, returnUserId, returnUserEmail, returnUserPwd;
        String result, urlAddress, paramValue;
        urlAddress = "http://35.197.167.33:8222/login";
        //paramValue = "{" + '"' + "userEmail" + '"' + ":" + useremail + ", " + '"' + "userPwd" + '"' + ":" + password + "}";
        // abc 12345678
        Handler handler=new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String jsonString = bundle.getString("result");
                ResultFromLogin json = gson.fromJson(jsonString, ResultFromLogin.class);
                if(json.code == 200) {

                    saveUserInfo(json.token, json.userInfo.userid, json.userInfo.email, json.userInfo.username, password);
                    loadUserInfo();

                    jumpToNavigationHeaderLogout();
                    Toast.makeText(getApplicationContext(), "login successfully",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), json.msg,
                            Toast.LENGTH_SHORT).show();
                }
            }};
        SendPostJson sendPostJson = new SendPostJson(urlAddress, jsonObject, handler);
        sendPostJson.request();

    }

    public void jumpToNavigationHeaderLogin() {

        // setting of login
        loginusername.setText("");
        loginpwd.setText("");

        // setting of logout
        logoutusername.setText("");
        logoutuseremail.setText("");

        // setting of register
        registerusername.setText("");
        registeruseremail.setText("");
        registerpwd.setText("");
        registerpwdagain.setText("");

        // View.INVISIBLE -- 看不见，但占位
        // View.GONE -- 看不见，不占位
        loginView.setVisibility(View.VISIBLE);
        logoutView.setVisibility(View.GONE);
        registerView.setVisibility(View.GONE);

    }

    public void jumpToNavigationHeaderLogout() {

        // ask for move default events from default user to current user
        List<CalenderEvent> allDefaultCalenderEventList = new ArrayList<CalenderEvent>();
        allDefaultCalenderEventList = dbhelper.getAllEventsByUserId(defaultUserId);

        int numberOfDefaultEvents = allDefaultCalenderEventList.size();

        if (numberOfDefaultEvents > 0) {
            // popup window
            changeDefaultEvents(numberOfDefaultEvents);

        }


        // setting of login
        loginusername.setText("");
        loginpwd.setText("");

        // setting of logout
        logoutusername.setText(userName);
        logoutuseremail.setText(userEmail);

        // setting of register
        registerusername.setText("");
        registeruseremail.setText("");
        registerpwd.setText("");
        registerpwdagain.setText("");


        loginView.setVisibility(View.GONE);
        logoutView.setVisibility(View.VISIBLE);
        registerView.setVisibility(View.GONE);

    }

    public void jumpToNavigationHeaderRegister() {

        // setting of login
        loginusername.setText("");
        loginpwd.setText("");

        // setting of logout
        logoutusername.setText("");
        logoutuseremail.setText("");

        // setting of register
        registerusername.setText("");
        registeruseremail.setText("");
        registerpwd.setText("");
        registerpwdagain.setText("");


        loginView.setVisibility(View.GONE);
        logoutView.setVisibility(View.GONE);
        registerView.setVisibility(View.VISIBLE);

    }

    // popup dialog
    // popup dialog: move default events to a user account
    public void changeDefaultEvents(int numberOfDefaultEvents){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("changeDefaultEvents");
        builder.setMessage("There are " + numberOfDefaultEvents + " default events. Do you want to move them into your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                boolean isSucceed = dbhelper.updateEventsByUserId(defaultUserId, userId);

                if (isSucceed) {
                    Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "no", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    // check for network availability
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
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

    // check for mobile
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null && mMobileNetworkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    // popup dialog: sync under mobile network
    public void syncUnderMobileNetwork(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("syncUnderMobileNetwork");
        builder.setMessage("Do you want to use Mobile Network to do synchronization?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                syncToCloud(userToken, userName);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(MainActivity.this, "no sync",Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    // TODO: http
    // sync
    // use token and name to sync this user's events and locations
    // call /sync with EventSync
    public void syncToCloud(String usertoken, String username){

        // token
        // userinfo
        // events
        // code 201 /

        // getAllEventsByUserId

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
                    updateEventAndLocationFromSync(userId, json.events, json.locations);
                    dbhelper.deleteEventByEventIdForReal();
                    dbhelper.deleteLocationByLocationIdForReal();

                    Toast.makeText(getApplicationContext(), "Sync success",
                            Toast.LENGTH_SHORT).show();
                } else if (json.code == 408) {
                    jumpToNavigationHeaderLogin();
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

    public void updateEventAndLocationFromSync(String userid, List<Event> events, List<Location> locations){

        List<Event> localEvents = dbhelper.syncGetAllEventsByUserId(userid);
        HashMap<String, Event> eventMap = new HashMap<String, Event>();
        //Log.d("update", "update");
        for (Event localevent: localEvents){
            eventMap.put(localevent.eventid, localevent);

        }
        for (Event event: events){
            if (event.isdelete != null && event.isdelete.compareTo("T") == 0){
                if (eventMap.get(event.eventid) != null) {
                    dbhelper.deleteEventByEventId(event.eventid);
                }
            }
            if (eventMap.get(event.eventid) == null) {
                dbhelper.insertSyncEvent(event);
            }
            else{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Event localEvent = eventMap.get(event.eventid);
                if(event.updatetime != null && localEvent.updatetime != null){
                    try{
                        if (simpleDateFormat.parse(event.updatetime).getTime() > simpleDateFormat.parse(localEvent.updatetime).getTime()){
                            dbhelper.updateEventFromSyncByEventId(event.eventid, event);
                        }
                    }catch(ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        List<Location> localLocations = dbhelper.syncGetAllLocationsByUserId(userid);
        HashMap<String, Location> locationMap = new HashMap<String, Location>();
        for (Location locallocation: localLocations){
            locationMap.put(locallocation.locationid, locallocation);
        }
        for (Location location: locations){
            if (location.isdelete != null && location.isdelete.compareTo("T") == 0){
                if (locationMap.get(location.locationid) != null) {
                    dbhelper.deleteLocationByLocationId(location.locationid);
                }
            }
            if (locationMap.get(location.locationid) == null) {
                dbhelper.insertSyncLocation(location);
            }
        }
    }

    // Tao: end here

}
