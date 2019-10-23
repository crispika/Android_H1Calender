package com.comp90018.H1Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comp90018.H1Calendar.Alarm.SendAlarmBroadcast;
import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.EventSettingActivity.EventQRShare;
import com.comp90018.H1Calendar.EventView.DeleteDialog;
import com.comp90018.H1Calendar.utils.CalenderEvent;
import com.comp90018.H1Calendar.utils.DistanceCalculator;
import com.comp90018.H1Calendar.utils.ShakeUtils;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailActivity extends AppCompatActivity implements LocationListener {

    private sqliteHelper dbhelper;
    private CalenderEvent mEvent;
    private String timeStr;

    private RelativeLayout event_detail_layout_header;
    private ImageButton btn_detail_back;
    private ImageButton btn_detail_delete;
    private ImageButton btn_detail_share;
    private RapidFloatingActionButton rfab_detail_update;

    private static final int LOCATION_CODE = 1;
    protected LocationManager locationManager;
    private boolean hasLocation;
    private String eventLocationStr;
    private String eventID;

    private TextView tv_detail_title;
    private TextView tv_detail_date;
    private TextView tv_detail_start_end_time;
    private TextView tv_detail_alarm_time;
    private TextView tv_detail_location;
    private TextView tv_detail_event_description;
    private TextView tv_detail_event_distance;
    private LinearLayout distanceView;

    private ShakeUtils shakeItOff;

    private static Location curLocation;

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
    @OnClick(R.id.event_detail_delete) void deleteEvent(){
        buildDialog();
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
        initSensor();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
        }else {
            grantLocation();
        }
        if(curLocation != null && hasLocation){
            displayDistance(curLocation);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeItOff.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeItOff.unRegister();
    }

    private void initSensor(){
        shakeItOff = new ShakeUtils(this);
        shakeItOff.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                Intent intent = new Intent(EventDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void bindView(){
        event_detail_layout_header = findViewById(R.id.event_detail_header);
        tv_detail_title = findViewById(R.id.event_detail_title);
        tv_detail_date = findViewById(R.id.event_detail_date);
        tv_detail_start_end_time = findViewById(R.id.event_detail_start_end_time);
        tv_detail_alarm_time = findViewById(R.id.event_detail_alarm_time);
        tv_detail_location = findViewById(R.id.event_detail_location);
        tv_detail_event_description = findViewById(R.id.event_detail_description);
        tv_detail_event_distance = findViewById(R.id.event_detail_distance);
        distanceView = findViewById(R.id.detail_distance_view);
    }
    public void setViewDetail(){
        Integer realMonth = mEvent.getMonth()+1;
        String dateStr = mEvent.getDay() + " / "+ realMonth + " / " + mEvent.getYear();
        setTimeStr();

        if(mEvent.getLocationId() != null){
            hasLocation = true;
            eventLocationStr = mEvent.getCoordinate();
            grantLocation();

        }else {
            hasLocation = false;
            distanceView.setVisibility(View.GONE);
        }

        event_detail_layout_header.setBackgroundColor(this.getResources().getColor(getColor(mEvent)));
        tv_detail_title.setText(mEvent.getTitle());
        tv_detail_date.setText(dateStr);
        tv_detail_start_end_time.setText(timeStr);
        setAlermText();
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

    public void setAlermText(){
        if(mEvent.getIsNeedNotify()){
            tv_detail_alarm_time.setText("Alarm Set");
        }else{
            tv_detail_alarm_time.setText("No Alarm");
        }
    }
    public void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailActivity.this);
        builder.setTitle("Attention");
        builder.setMessage("Delete Event?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbhelper.deleteEventByEventId(mEvent.getEventId());
                SendAlarmBroadcast.startAlarmService(EventDetailActivity.this);
                Intent intent = new Intent(EventDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void grantLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List list = locationManager.getProviders(true);

        String provider = "";
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        }
        else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
           //是否为网络位置控制器
           provider = LocationManager.NETWORK_PROVIDER;
        }
        if((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) ){
           locationManager.requestLocationUpdates(provider, 100, 10, this);
        }
    }

    private void displayDistance(Location curLoc){
        Location eventLocation = new Location("dummyprovider");
        String[]latlng = eventLocationStr.split(",");
        //System.out.println(location.getLatitude() +" "+ location.getLongitude());
        eventLocation.setLatitude(Float.valueOf(latlng[0]));
        eventLocation.setLongitude(Float.valueOf(latlng[1]));
        //System.out.println(eventLocation.getLatitude() +" "+ eventLocation.getLongitude());
        String distance = DistanceCalculator.distanceBetween(curLoc,eventLocation);
        tv_detail_event_distance.setText(distance);
    }


    @Override
    public void onLocationChanged(Location location) {
        if(hasLocation){
            displayDistance(location);
            curLocation = location;
        }



    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
