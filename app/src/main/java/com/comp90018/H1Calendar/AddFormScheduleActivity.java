package com.comp90018.H1Calendar;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.ButterKnife;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.EventSettingActivity.EventColorSet;
import com.comp90018.H1Calendar.EventSettingActivity.EventLocalSet;
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
            cEvent.setEventId(getEventID());
            //TODO: store event into DB
            sqliteHelper helper = new sqliteHelper(this);
            if(helper.insert(cEvent)){
                Log.d("insert", "successful");
            }
            else{
                Log.d("insert", "unsuccessful");
            }


            boolean isSucceed = dbhelper.insert(cEvent);
            if(isSucceed){
                Toast.makeText(this, "Save Sucessful!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Save ERROR!", Toast.LENGTH_SHORT).show();
            }

            startActivity(new Intent(this, MainActivity.class));
            finish();
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
        dbhelper = new sqliteHelper(getApplicationContext());
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        cEvent = new CalenderEvent();
        if (getIntent().getStringExtra("type").equals("addEvent")) {
            edit_event_title.setText("New Event");
        } else {
            Intent intent = getIntent();
            CalenderEvent StoreEvent = (CalenderEvent) intent.getSerializableExtra("CalenderEvent");
            eventId = StoreEvent.getEventId();
            event_title.setText(StoreEvent.getTitle());
            event_color.setText(StoreEvent.getEventColor());
            event_detail.setText(StoreEvent.getDescription());
            event_local.setText(StoreEvent.getLocal());
            edit_event_title.setText("Edit Event");

        }
    }

    private void getDatePicker() {
        Calendar calendar = getToday();
        mDataPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd  EEE");
                event_date.setText(df.format(calendar.getTime()));
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

    private String getEventID(){
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        System.out.println("eventID: "+uniqueId);
        return uniqueId;
    }


}


