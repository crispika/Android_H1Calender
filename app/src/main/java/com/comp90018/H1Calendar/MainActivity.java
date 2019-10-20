package com.comp90018.H1Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.comp90018.H1Calendar.utils.EventBus;
import com.comp90018.H1Calendar.utils.Events;
import com.comp90018.H1Calendar.utils.LightSensorUtils;
import com.google.android.material.navigation.NavigationView;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private EditText loginuseremail, loginpwd, registeruseremail, registerpwd, registerpwdagain;
    private TextView logoutuseremail, logoutuserid;
    private Button loginlogin, loginregister, logoutlogout, logoutsync, registerregister, registercancel;

    // store user info into shared preferences
    private static final String SHAREDPREFS = "sharedPrefs";
    private static final String USERID = "userid";
    private static final String USEREMAIL = "useremail";
    private static final String USERPWD = "userpwd";

    // variable used to store user info that get from shared preferences
    private String userId, userEmail, userPwd;

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

        //set navigationView click event
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
                            getSupportFragmentManager().beginTransaction().show(weekEventView).commitAllowingStateLoss();
                            drawer_layout.closeDrawers();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
        getSupportFragmentManager().beginTransaction().add(R.id.Event_container, weekEventView).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().hide(weekEventView).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().add(R.id.Event_container, dayEventView).commitAllowingStateLoss();


        // Tao: start here

        navigationView = findViewById(R.id.navigation);

        // add three header layouts to navigation view
        loginView = navigationView.inflateHeaderView(R.layout.navigation_header_login);
        logoutView = navigationView.inflateHeaderView(R.layout.navigation_header_logout);
        registerView = navigationView.inflateHeaderView(R.layout.navigation_header_register);

        // widgets
        // login
        loginuseremail = loginView.findViewById(R.id.et_useremail);
        loginpwd = loginView.findViewById(R.id.et_pwd);
        loginlogin = loginView.findViewById(R.id.btn_login);
        loginregister = loginView.findViewById(R.id.btn_register);

        // logout
        logoutuseremail = logoutView.findViewById(R.id.tv_useremail);
        logoutuserid = logoutView.findViewById(R.id.tv_userId);
        logoutlogout = logoutView.findViewById(R.id.btn_logout);
        logoutsync = logoutView.findViewById(R.id.btn_sync);

        // register
        registeruseremail = registerView.findViewById(R.id.et_useremail);
        registerpwd = registerView.findViewById(R.id.et_pwd);
        registerpwdagain = registerView.findViewById(R.id.et_pwdConfirm);
        registerregister = registerView.findViewById(R.id.btn_register);
        registercancel = registerView.findViewById(R.id.btn_cancel);

        // db helper
        dbhelper = new sqliteHelper(getApplicationContext());

        // load user info from shared preferences
        // assign the values to userId, userName, userPwd, the default values are ""
        loadUserInfo();

        // user info is available
        if (!userId.equals("") && !userEmail.equals("") && !userPwd.equals("")) {

            // return something
            userValidation(userEmail, userPwd);

            // processing bar

            String returnUserId, returnUserEmail, returnUserPwd;


            // successful
            if (true) {

                // save user info (update user info)
                // saveUserInfo(returnUserId, returnUserEmail, returnUserPwd);
                // loadUserInfo();

                // jump to navigation_header_logout
                jumpToNavigationHeaderLogout();

                Toast.makeText(getApplicationContext(), "login successfully",
                        Toast.LENGTH_SHORT).show();

            }
            // failed
            else {

                // jump to navigation_header_login
                jumpToNavigationHeaderLogin();

                Toast.makeText(getApplicationContext(), "login failed, try again please",
                        Toast.LENGTH_SHORT).show();

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
                String loginUseremail = loginuseremail.getText().toString();
                String loginPwd = loginpwd.getText().toString();

                if (loginUseremail.equals("") || loginPwd.equals("")) {

                    Toast.makeText(getApplicationContext(), "username or pwd is missing",
                            Toast.LENGTH_SHORT).show();

                } else {

                    // return something
                    userValidation(loginUseremail, loginPwd);

                    // processing bar

                    String returnUserId, returnUserEmail, returnUserPwd;

                    // successful
                    if (true) {

                        // save user info (update user info)
                        // saveUserInfo(returnUserId, returnUserEmail, returnUserPwd);
                        // loadUserInfo();

                        // for test
                        saveUserInfo("userid", loginUseremail, loginPwd);
                        loadUserInfo();

                        jumpToNavigationHeaderLogout();

                        Toast.makeText(getApplicationContext(), "login successfully",
                                Toast.LENGTH_SHORT).show();


                    }
                    // failed
                    else {

                        Toast.makeText(getApplicationContext(), "login failed, try again please",
                                Toast.LENGTH_SHORT).show();

                    }
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

                saveUserInfo("", "", "");
                loadUserInfo();

                jumpToNavigationHeaderLogin();

            }
        });

        // logout: sync button
        logoutsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getAllEventsByUserId

                List<CalenderEvent> allCurrentCalenderEventList = new ArrayList<CalenderEvent>();
                allCurrentCalenderEventList = dbhelper.getAllEventsByUserId(userId);

                // 生成json传递 {userId : [events]}
                Gson gson = new Gson();
                String jsonString = gson.toJson(allCurrentCalenderEventList);

                jsonString = "{" + '"' + userId + '"' + ":" + jsonString + "}";

                // Http request

                Log.d("json", jsonString);
//                [{"day":10,"description":"None","endTimeHour":0,"endTimeMinute":0,"eventId":"509b23f6-a73a-4121-8b80-403681c258e6","isAllday":false,"isNeedNotify":false,"local":"None","month":10,"startTimeHour":0,"startTimeMinute":0,"title":"default1","year":2019},
//                {"day":11,"description":"None","endTimeHour":0,"endTimeMinute":0,"eventId":"e93e3d36-8836-434d-bba7-352846a28eae","isAllday":false,"isNeedNotify":false,"local":"None","month":10,"startTimeHour":0,"startTimeMinute":0,"title":"default2","year":2019},
//                {"day":13,"description":"None","endTimeHour":0,"endTimeMinute":0,"eventId":"9d01331f-ac2a-4e0a-9b8b-04069d1acd90","isAllday":false,"isNeedNotify":false,"local":"None","month":10,"startTimeHour":0,"startTimeMinute":0,"title":"default3","year":2019}]


                // processing bar

                // 收到反馈

                // sync successfully
                if (true) {
                    Toast.makeText(getApplicationContext(), "sync successfully",
                            Toast.LENGTH_SHORT).show();
                }
                // sync failed
                else {
                    Toast.makeText(getApplicationContext(), "sync failed",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

        // register: register buttion
        registerregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String registerUseremail = registeruseremail.getText().toString();
                String registerPwd = registerpwd.getText().toString();
                String registerPwdAgain = registerpwdagain.getText().toString();

                if (registerUseremail.equals("") || registerPwd.equals("") || registerPwdAgain.equals("")) {

                    Toast.makeText(getApplicationContext(), "username or pwd is missing",
                            Toast.LENGTH_SHORT).show();

                } else if (!registerPwd.equals(registerPwdAgain)) {

                    Toast.makeText(getApplicationContext(), "passwords are not same",
                            Toast.LENGTH_SHORT).show();

                } else {

                    // 用户注册 -- 云端验证
                    // processing bar
                    // 登录成功：返回 true 用户ID，用户名（email），密码），存到本地DB中，跳转到logout页面
                    // 全局的current user ID 设为此用户ID
                    // 登录失败：返回 false，显示信息

                    // return something
                    userValidation(registerUseremail, registerPwd);

                    // processing bar

                    String returnUserId, returnUserEmail, returnUserPwd;

                    // successful
                    if (true) {

                        // save user info (update user info)
                        // saveUserInfo(returnUserId, returnUserEmail, returnUserPwd);
                        // loadUserInfo();

                        // for test
                        saveUserInfo("returnUserId", registerUseremail, registerPwd);
                        loadUserInfo();

                        jumpToNavigationHeaderLogout();

                        Toast.makeText(getApplicationContext(), "register successfully",
                                Toast.LENGTH_SHORT).show();

                    }
                    // failed
                    else {

                        Toast.makeText(getApplicationContext(), "register failed, try again please",
                                Toast.LENGTH_SHORT).show();

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

    public void saveUserInfo(String userid, String useremail, String userpwd) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERID, userid);
        editor.putString(USEREMAIL, useremail);
        editor.putString(USERPWD, userpwd);

        // apply(): apply会把数据同步写入内存缓存，然后异步保存到磁盘，可能会执行失败，失败不会收到错误回调
        // commit(): commit将同步的把数据写入磁盘和内存缓存
        editor.apply();
        Toast.makeText(this, "data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        // the default values of these three variables are ""
        userId = sharedPreferences.getString(USERID, "");
        userEmail = sharedPreferences.getString(USEREMAIL, "");
        userPwd = sharedPreferences.getString(USERPWD, "");

    }

    // return user info and validation state
    public void userValidation(String useremail, String password) {


    }

    public void jumpToNavigationHeaderLogin() {

        // setting of login
        loginuseremail.setText("");
        loginpwd.setText("");

        // setting of logout
        logoutuserid.setText("");
        logoutuseremail.setText("");

        // setting of register
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

        List<CalenderEvent> allDefaultCalenderEventList = new ArrayList<CalenderEvent>();
        allDefaultCalenderEventList = dbhelper.getAllEventsByUserId(defaultUserId);

        int numberOfDefaultEvents = allDefaultCalenderEventList.size();

        if (numberOfDefaultEvents > 0) {
            // popup window
            changeDefaultEvents(numberOfDefaultEvents);

        }


        // setting of login
        loginuseremail.setText("");
        loginpwd.setText("");

        // setting of logout
        logoutuserid.setText(userId);
        logoutuseremail.setText(userEmail);

        // setting of register
        registeruseremail.setText("");
        registerpwd.setText("");
        registerpwdagain.setText("");


        loginView.setVisibility(View.GONE);
        logoutView.setVisibility(View.VISIBLE);
        registerView.setVisibility(View.GONE);

    }

    public void jumpToNavigationHeaderRegister() {

        // setting of login
        loginuseremail.setText("");
        loginpwd.setText("");

        // setting of logout
        logoutuserid.setText("");
        logoutuseremail.setText("");

        // setting of register
        registeruseremail.setText("");
        registerpwd.setText("");
        registerpwdagain.setText("");


        loginView.setVisibility(View.GONE);
        logoutView.setVisibility(View.GONE);
        registerView.setVisibility(View.VISIBLE);

    }

    // popup dialog
    public void changeDefaultEvents(int numberOfDefaultEvents) {
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


    // Tao: end here

}

