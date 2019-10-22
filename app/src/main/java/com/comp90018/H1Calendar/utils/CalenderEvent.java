package com.comp90018.H1Calendar.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.comp90018.H1Calendar.Alarm.AlarmReceiver;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Calendar;

public class CalenderEvent implements Serializable,Comparable<CalenderEvent> {

    private String eventId;
    private String title;
    private boolean isAllday;//是否是全天
    private boolean isNeedNotify;//是否需要提醒
    private int year;
    private int month;
    private int day;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;
    private String eventColor;
    private String eventTime;
    private String local;
    private String description;

    private String coordinate;
    private String locationId;

    // Tao
    private String userId;
    public CalenderEvent() {

    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String id) {
        this.eventId = id;
    }

    public void setLocationId(String id) {
        this.locationId = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsAllday() {
        return isAllday;
    }

    public void setIsAllday(boolean isAllday) {
        this.isAllday = isAllday;
    }

    public boolean getIsNeedNotify() {
        return isNeedNotify;
    }

    public void setIsNeedNotify(boolean isNeedNotify) {
        this.isNeedNotify = isNeedNotify;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public int getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(int startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventColor() {
        return eventColor;
    }

    public void setEventColor(String alarmColor) {
        this.eventColor = alarmColor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }


    // Tao
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }
    /**
     * 默认提前一个小时提醒
     *
     * @return
     */
    public Calendar getAlarmTime() {
        Calendar cal = Calendar.getInstance();
        //Java month problem...
        cal.set(getYear(), getMonth()-1, getDay(), getStartTimeHour(), getStartTimeMinute());
        cal.add(Calendar.HOUR_OF_DAY, -1);
        return cal;
    }

    private Calendar getCalendarTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), getMonth(), getDay(), getStartTimeHour(), getStartTimeMinute());
        return cal;
    }


    @Override
    public int compareTo(CalenderEvent event) {
        if (getCalendarTime().getTimeInMillis() > event.getCalendarTime().getTimeInMillis())
            return 1;
        if (getCalendarTime().getTimeInMillis() < event.getCalendarTime().getTimeInMillis())
            return -1;
        return 0;
    }

    public void setAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
//        Intent intent = new Intent().setAction("SendAlarm");
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("alarm",this);
        intent.putExtra("id",getEventId());
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime().getTimeInMillis(), pendingIntent);

        Log.d("Notification","Alarm Time: " + getAlarmTime().getTime().toString());
        Log.d("Notification","Alarm setted.");
    }


    public String toJsonStr(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
