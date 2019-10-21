package com.comp90018.H1Calendar.utils;

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

    public String toJsonStr(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    // Tao
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }


//    @Override
//    public int compareTo(CalenderEvent event) {
//        if (getYear() != event.getYear()) return compareInt(getYear(),event.getYear());
//        if (getMonth() != event.getMonth()) return compareInt(getMonth(),event.getMonth());
//        if (getDay() != event.getDay()) return compareInt(getDay(),event.getDay());
//        if (getStartTimeHour() != event.getStartTimeHour()) return compareInt(getStartTimeHour(),event.getStartTimeHour());
//        if (getStartTimeMinute() != event.getStartTimeMinute()) return compareInt(getStartTimeMinute(),event.getStartTimeMinute());
//        return 0;
//    }
//    private int compareInt(int a, int b){
//        if (a<b) return -1;
//        else if (a>b) return 1;
//        else return 0;
//    }

    private Calendar getCalendarTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(),getMonth(),getDay(),getStartTimeHour(),getStartTimeMinute());
        return cal;
    }

    @Override
    public int compareTo(CalenderEvent event) {
        if (getCalendarTime().getTimeInMillis() > event.getCalendarTime().getTimeInMillis()) return 1;
        if (getCalendarTime().getTimeInMillis() < event.getCalendarTime().getTimeInMillis()) return -1;
        return 0;
    }



}
