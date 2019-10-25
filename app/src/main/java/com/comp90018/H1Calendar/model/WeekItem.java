package com.comp90018.H1Calendar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeekItem {
    private int mWeekInYear;
    private int mYear;
    private int mMonth;
    private Date mDate; //the date for current week
    private String mLabel;
    private ArrayList<DayItem> mDayItems;

    // region Constructor

    //    public WeekItem(int weekInYear, int year, Date date, String label, int month) {
    public WeekItem(int year, int month, int weekInYear, Date date) {
        this.mWeekInYear = weekInYear;
        this.mYear = year;
        this.mDate = date;
//        this.mLabel = label;
        this.mMonth = month;
    }

    // endregion

    // region Getters/Setters

    public int getWeekInYear() {
        return mWeekInYear;
    }

    public void setWeekInYear(int weekInYear) {
        this.mWeekInYear = weekInYear;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        this.mMonth = month;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

//    public String getLabel() {
//        return mLabel;
//    }
//
//    public void setLabel(String label) {
//        this.mLabel = label;
//    }

    public ArrayList<DayItem> getDayItems() {
        return mDayItems;
    }

    public void setDayItems(ArrayList<DayItem> dayItems) {
        this.mDayItems = dayItems;
    }

    // endregion

    @Override
    public String toString() {
        return "WeekItem{"
                + "label='"
                + mLabel
                + '\''
                + ", weekInYear="
                + mWeekInYear
                + ", year="
                + mYear
                + '}';
    }
}
