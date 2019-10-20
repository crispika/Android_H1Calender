package com.comp90018.H1Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.EventSettingActivity.EventQRShare;
import com.comp90018.H1Calendar.utils.CalenderEvent;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailActivity extends AppCompatActivity {

    private sqliteHelper dbhelper;
    private CalenderEvent mEvent;
    private String timeStr;

    private RelativeLayout event_detail_layout_header;
    private ImageButton btn_detail_back;
    private ImageButton btn_detail_delete;
    private ImageButton btn_detail_share;
    private RapidFloatingActionButton rfab_detail_update;

    private String eventID;
    private TextView tv_detail_title;
    private TextView tv_detail_date;
    private TextView tv_detail_start_end_time;
    private TextView tv_detail_alarm_time;
    private TextView tv_detail_location;
    private TextView tv_detail_event_description;

    @OnClick(R.id.event_detail_back) void jumpBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.event_detail_update_rfab) void updateEvent(){
        Intent intent = new Intent(this, AddFormScheduleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type","editEvent");
        bundle.putString("id",eventID);
        //System.out.println(eventID);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.event_detail_share) void shareByQR(){
        Intent intent = new Intent(this, EventQRShare.class);
        Bundle bundle = new Bundle();
        bundle.putString("event_id",eventID);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        dbhelper = new sqliteHelper(this);
        ButterKnife.bind(this);
        bindView();
        //bindOnClickAction();

        Bundle bundle = getIntent().getExtras();
        eventID = bundle.getString("id");
        mEvent = dbhelper.getEventByEventId(eventID);
        setViewDetail();
    }
    public void bindView(){
        event_detail_layout_header = findViewById(R.id.event_detail_header);
        tv_detail_title = findViewById(R.id.event_detail_title);
        tv_detail_date = findViewById(R.id.event_detail_date);
        tv_detail_start_end_time = findViewById(R.id.event_detail_start_end_time);
        tv_detail_alarm_time = findViewById(R.id.event_detail_alarm_time);
        tv_detail_location = findViewById(R.id.event_detail_location);
        tv_detail_event_description = findViewById(R.id.event_detail_description);
    }
    public void setViewDetail(){
        String dateStr = mEvent.getDay() + " / "+mEvent.getMonth() + " / " + mEvent.getYear();
        setTimeStr();

        event_detail_layout_header.setBackgroundColor(this.getResources().getColor(getColor(mEvent)));
        tv_detail_title.setText(mEvent.getTitle());
        tv_detail_date.setText(dateStr);
        tv_detail_start_end_time.setText(timeStr);
        tv_detail_alarm_time.setText(mEvent.getEventTime());
        tv_detail_location.setText(mEvent.getLocal());
        tv_detail_event_description.setText(mEvent.getDescription());
    }
    public void bindOnClickAction(){

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private int getColor(CalenderEvent mEvent){
        String color;
        if (mEvent == null) return R.color.Default;
        if(mEvent.getEventColor() == null) color = "default";
        else color = mEvent.getEventColor();

        switch (color){
            case "Green":
                return R.color.Green;
            case "Yellow":
                return R.color.Yellow;
            case "Red":
                return R.color.Red;
            case "Blue":
                return R.color.Blue;
            default:
                return R.color.Default;
        }
    }

    public void setTimeStr(){
        if(mEvent.getIsAllday()){
            timeStr = "Full Day";
        }else{
            timeStr = mEvent.getStartTimeHour() + " : " + mEvent.getStartTimeMinute() + " - " +
                    mEvent.getEndTimeHour() + " : " + mEvent.getEndTimeMinute();
        }
    }
}
