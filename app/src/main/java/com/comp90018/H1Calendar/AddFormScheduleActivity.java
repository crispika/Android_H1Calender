package com.comp90018.H1Calendar;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.ButterKnife;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.EventSettingActivity.EventColorSet;
import com.comp90018.H1Calendar.EventSettingActivity.EventLocalSet;
import com.comp90018.H1Calendar.EventSettingActivity.EventQRShare;
import com.comp90018.H1Calendar.utils.*;

public class AddFormScheduleActivity extends Activity {

    private DatePickerDialog mDataPicker;
    private TimePickerDialog mStartTimePicker;
    private TimePickerDialog mEndTimePicker;
    private Calendar startTime;
    private Calendar endTime;
    private CalenderEvent cEvent;
    private String eventId;
    private boolean isAllDay = false;
    private boolean isNeedNotify = false;
    private sqliteHelper dbhelper;

    //Sensor
    private ShakeUtils shakeItOff;

    // Tao
    // store user info into shared preferences
    private static final String SHAREDPREFS  = "sharedPrefs";
    private static final String USERID = "userid";
    private static final String USEREMAIL = "useremail";
    private static final String USERPWD = "userpwd";
    // variable used to store user info that get from shared preferences
    private String userId, userEmail, userPwd;

    @BindView(R.id.event_title)
    EditText event_title;
    @BindView(R.id.event_detail)
    EditText event_detail;
    @BindView(R.id.event_local)
    TextView event_local;
    @BindView(R.id.event_color)
    TextView event_color;
    @BindView(R.id.event_date)
    TextView event_date;
    @BindView(R.id.edit_type)
    TextView edit_event_title;
    @BindView(R.id.action_bar)
    LinearLayout action_bar;

    @OnClick(R.id.event_date)
    void openDatePicker() {
        getDatePicker();
        mDataPicker.show();
    }

    @BindView(R.id.event_start_time)
    TextView event_start_time;

    @OnClick(R.id.event_start_time)
    void openStartTimePicker() {
        getStartTimePicker();
        mStartTimePicker.show();
    }

    @BindView(R.id.event_end_time)
    TextView event_end_time;

    @OnClick(R.id.event_end_time)
    void openEndTimePicker() {
        getEndTimePicker();
        mEndTimePicker.show();
    }


    @BindView(R.id.all_day_switch)
    Switch allDaySwitch;

    @OnClick(R.id.all_day_switch)
    void AllDayEvent() {
        isAllDay = !isAllDay;
        if (isAllDay) {
            event_start_time.setVisibility(View.GONE);
            event_end_time.setVisibility(View.GONE);
        } else {
            event_start_time.setVisibility(View.VISIBLE);
            event_end_time.setVisibility(View.VISIBLE);
        }
        cEvent.setIsAllday(isAllDay);
    }

    @BindView(R.id.notify_switch)
    Switch notify_switch;

    @OnClick(R.id.notify_switch)
    void NeedNotify() {
        isNeedNotify = !isNeedNotify;
        cEvent.setIsNeedNotify(isNeedNotify);
    }
    @OnClick(R.id.event_local)
    void openSetLocalActivity(){
        startActivityForResult(new Intent(AddFormScheduleActivity.this, EventLocalSet.class), 1);
    }

    @OnClick(R.id.event_color)
    void openSetColorActivity() {
        startActivityForResult(new Intent(AddFormScheduleActivity.this, EventColorSet.class), 2);
    }

    @OnClick(R.id.tv_save)
    void SaveEvent() {
        if (event_title.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Event Title Missing",
                    Toast.LENGTH_SHORT).show();
        } else if (event_date.getText().toString().equals("Set Event Date")) {
            Toast.makeText(getApplicationContext(), "Event Date Missing",
                    Toast.LENGTH_SHORT).show();
        } else {
            //set title
            cEvent.setTitle(event_title.getText().toString());
            //event description
            if (event_detail.getText().toString().equals("")) {
                cEvent.setDescription("None");
            } else {
                cEvent.setDescription(event_detail.getText().toString());
            }

            //set location
            if (event_local.getText().equals("Add Location")) {
                cEvent.setLocal("None");
            }else {

            }

            //set is all day
            if (isAllDay) {
                cEvent.setIsAllday(true);
                cEvent.setStartTimeHour(0);
                cEvent.setStartTimeMinute(0);
                cEvent.setEndTimeHour(0);
                cEvent.setEndTimeMinute(0);
            } else {
                cEvent.setIsAllday(false);
            }
            if(cEvent.getEventId() == null){
                cEvent.setEventId(generateEventID());
            }

            // Tao
            // add userId to calender event
            loadUserInfo();
            cEvent.setUserId(userId);



            //TODO: store event into DB

            boolean isSucceed = dbhelper.insert(cEvent);
            if(isSucceed){
                Toast.makeText(this, "Save Successful!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Save ERROR!", Toast.LENGTH_SHORT).show();
            }


            // test for selection statement

            // weekly
//            List<CalenderEvent> weeklyCalenderEventList = new ArrayList<CalenderEvent>();
//            weeklyCalenderEventList = dbhelper.getEventsByWeek("8/9/2019", "8/9/2019");
//            Log.d("weekly", weeklyCalenderEventList.size()+"");
//
//            CalenderEvent c1 = new CalenderEvent();
//            c1 = weeklyCalenderEventList.get(weeklyCalenderEventList.size() - 1);
//            Log.d("weekly", c1.getTitle() + " day " + c1.getDay() + " month " + c1.getMonth() + " year " + c1.getYear());
//
//            // daily
//            List<CalenderEvent> dailyCalenderEventList = new ArrayList<CalenderEvent>();
//            dailyCalenderEventList = dbhelper.getEventsByDay("8/9/2019");
//            Log.d("daily", dailyCalenderEventList.size()+"");
//
//            CalenderEvent c2 = new CalenderEvent();
//            c2 = dailyCalenderEventList.get(dailyCalenderEventList.size() - 1);
//            Log.d("daily", c2.getTitle() + " day " + c2.getDay() + " month " + c2.getMonth() + " year " + c2.getYear());
//
//            // all
//            List<CalenderEvent> allCalenderEventList = new ArrayList<CalenderEvent>();
//            allCalenderEventList = dbhelper.getAllEvents();
//            Log.d("all", allCalenderEventList.size()+"");
//
//            CalenderEvent c3 = new CalenderEvent();
//            c3 = allCalenderEventList.get(allCalenderEventList.size() - 1);
//            Log.d("all", c3.getTitle() + " day " + c3.getDay() + " month " + c3.getMonth() + " year " + c3.getYear());
//
//            // select an event by event Id
//            CalenderEvent c4 = new CalenderEvent();
//            c4 = dbhelper.getEventByEventId("3984429d-f33c-4816-abb7-87d77f32fbce");
//            Log.d("eventId", c4.getTitle() + " day " + c4.getDay() + " month " + c4.getMonth() + " year " + c4.getYear());


            // end here


            startActivity(new Intent(this, MainActivity.class));
            finish();
//            Intent intent = new Intent(this, EventQRShare.class);
//            intent.putExtra("event",cEvent);
//            startActivity(intent);
        }


    }

    @OnClick(R.id.cancel_edit)
    void cancelEdit() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form_schedule);
        ButterKnife.bind(this);

        initSensor(); //shake sensor

        dbhelper = new sqliteHelper(getApplicationContext());
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        cEvent = new CalenderEvent();
        String type = getIntent().getStringExtra("type");
        if (type.equals("addEvent")) {
            edit_event_title.setText("New Event");
        } else if(type.equals("editEvent")){
            Intent intent = getIntent();
            String event_id = intent.getStringExtra("id");
            System.out.println(event_id+"11111111111111111");
            cEvent = dbhelper.getEventByEventId(event_id);

            //initialize the view on edit interface
            event_title.setText(cEvent.getTitle());
            event_color.setText(cEvent.getEventColor());
            event_detail.setText(cEvent.getDescription());
            event_local.setText(cEvent.getLocal());
            event_date.setText(genDateStr(cEvent.getYear(),cEvent.getMonth(),cEvent.getDay()));
            if(cEvent.getIsAllday()){
                allDaySwitch.setChecked(true);
                event_start_time.setVisibility(View.GONE);
                event_end_time.setVisibility(View.GONE);
            }else{
                allDaySwitch.setChecked(false);
                event_start_time.setText(genTimeStr(cEvent.getStartTimeHour(),cEvent.getStartTimeMinute()));
                event_end_time.setText(genTimeStr(cEvent.getEndTimeHour(),cEvent.getEndTimeMinute()));
            }

            if(cEvent.getIsNeedNotify()){
                notify_switch.setChecked(true);
            }


        }else {
            Intent intent = getIntent();
            CalenderEvent storeEvent;
            storeEvent = (CalenderEvent) intent.getSerializableExtra("QR_event");
            cEvent = storeEvent;

            //initialize the view on edit interface
            event_title.setText(storeEvent.getTitle());
            event_color.setText(storeEvent.getEventColor());
            event_detail.setText(storeEvent.getDescription());
            event_local.setText(storeEvent.getLocal());
            event_date.setText(genDateStr(cEvent.getYear(),cEvent.getMonth(),cEvent.getDay()));
            if(cEvent.getIsAllday()){
                allDaySwitch.setChecked(true);
                event_start_time.setVisibility(View.GONE);
                event_end_time.setVisibility(View.GONE);
            }else{
                allDaySwitch.setChecked(false);
                event_start_time.setText(genTimeStr(cEvent.getStartTimeHour(),cEvent.getStartTimeMinute()));
                event_end_time.setText(genTimeStr(cEvent.getEndTimeHour(),cEvent.getEndTimeMinute()));
            }

            if(cEvent.getIsNeedNotify()){
                notify_switch.setChecked(true);
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeItOff.unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeItOff.register();
    }

    private void initSensor(){
        shakeItOff = new ShakeUtils(this);
        shakeItOff.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                Intent intent = new Intent(AddFormScheduleActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDatePicker() {
        Calendar calendar = getToday();
        mDataPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                event_date.setText(genDateStr(year,monthOfYear,dayOfMonth));
                cEvent.setYear(year);
                cEvent.setMonth(monthOfYear);
                cEvent.setDay(dayOfMonth);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void getStartTimePicker() {
        Calendar calendar = getToday();
        mStartTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                if(endTime == null || calendar.before(endTime)){
                    startTime = calendar;
                    event_start_time.setText("From: " + df.format(calendar.getTime()));
                    //设置开始时间的小时、分钟
                    cEvent.setStartTimeHour(hourOfDay);
                    cEvent.setStartTimeMinute(minute);
                }else {
                    Toast.makeText(getApplicationContext(), "It is later than End Time",
                            Toast.LENGTH_SHORT).show();
                    event_start_time.setText("Start Time");
                }


            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    private void getEndTimePicker() {
        Calendar calendar = getToday();
        mEndTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                if(startTime == null || calendar.after(startTime)){
                    endTime = calendar;
                    event_end_time.setText("To:    " + df.format(calendar.getTime()));
                    //设置开始时间的小时、分钟
                    cEvent.setEndTimeHour(hourOfDay);
                    cEvent.setEndTimeMinute(minute);
                }else {
                    Toast.makeText(getApplicationContext(), "It is earlier than start Time",
                            Toast.LENGTH_SHORT).show();
                    event_end_time.setText("End Time");
                }

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    private Calendar getToday() {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        return today;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         if (requestCode == 1) {
            if (resultCode == 1) {
                if (data != null) {
                    event_local.setText(data.getStringExtra("location"));
                    cEvent.setLocal(data.getStringExtra("location"));
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == 2) {
                if (data != null) {
                    event_color.setText(data.getStringExtra("color"));
                    cEvent.setEventColor(data.getStringExtra("color"));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    String genDateStr(int year, int monthOfYear,int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd  EEE");
        return df.format(calendar.getTime());
    }

    String genTimeStr(int hourOfDay,int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(calendar.getTime());
    }

    private String generateEventID(){
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        System.out.println("eventID: "+uniqueId);
        return uniqueId;
    }

    // Tao
    public void loadUserInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);

        // the default values of these three variables are ""
        userId = sharedPreferences.getString(USERID, "");
        userEmail = sharedPreferences.getString(USEREMAIL, "");
        userPwd = sharedPreferences.getString(USERPWD, "");

    }

}


