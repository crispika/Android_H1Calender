package com.comp90018.H1Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailActivity extends AppCompatActivity {

    private RelativeLayout event_detail_layout_header;
    private ImageButton btn_detail_back;
    private ImageButton btn_detail_delete;
    private RapidFloatingActionButton rfab_detail_update;


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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        bindView();
        //bindOnClickAction();

        Bundle bundle = getIntent().getExtras();
        tv_detail_title.setText(bundle.getString("title"));
    }
    public void bindView(){
        event_detail_layout_header = findViewById(R.id.event_detail_header);
        btn_detail_back = findViewById(R.id.event_detail_back);
        btn_detail_delete = findViewById(R.id.event_detail_delete);
        rfab_detail_update = findViewById(R.id.event_detail_update_rfab);

        tv_detail_title = findViewById(R.id.event_detail_title);
        tv_detail_date = findViewById(R.id.event_detail_date);
        tv_detail_start_end_time = findViewById(R.id.event_detail_start_end_time);
        tv_detail_alarm_time = findViewById(R.id.event_detail_alarm_time);
        tv_detail_location = findViewById(R.id.event_detail_location);
        tv_detail_event_description = findViewById(R.id.event_detail_description);
    }
    public void bindOnClickAction(){

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
