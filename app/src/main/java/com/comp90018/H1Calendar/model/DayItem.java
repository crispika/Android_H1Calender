package com.comp90018.H1Calendar.model;


import java.io.Serializable;
import java.util.Date;

public class DayItem implements Serializable {

    private Date mDate; //date
    private int mDayOfTheMonth; //day number in month
    private int mDayOfTheWeek; //day number in week
    private boolean mToday; //today or not
    private boolean mFirstDayOfTheMonth = false; //the first day of current month
    private boolean isSelected;//is selected
    private String mMonth; // short version of month name
    private int weekListPosition;
    private String mMonthFullName; //month full name
    private int mWeekOfTheYear;

    // region Constructor

    public DayItem(Date date, int dayOfTheMonth, boolean today, String month, String monthFullName,int position, int weekOfTheYear) {
        this.mDate = date;
        this.mDayOfTheMonth = dayOfTheMonth;
        this.mToday = today;
        this.mMonth = month;
        this.mMonthFullName = monthFullName;
        this.weekListPosition = position;
        this.mWeekOfTheYear = weekOfTheYear;


        if (mDayOfTheMonth == 1) {
            mFirstDayOfTheMonth = true;
        }
    }

    // endregion

    //region getter/setter
    public String getmMonthFullName() {
        return mMonthFullName;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public int getDayOfTheMonth() {
        return mDayOfTheMonth;
    }

    public void setValue(int dayOfMonth) {
        this.mDayOfTheMonth = dayOfMonth;
    }

    public boolean isToday() {
        return mToday;
    }

    public void setToday(boolean today) {
        this.mToday = today;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isFirstDayOfTheMonth() {
        return mFirstDayOfTheMonth;
    }

    public void setFirstDayOfTheMonth(boolean firstDayOfTheMonth) {
        this.mFirstDayOfTheMonth = firstDayOfTheMonth;
    }

    public String getMonth() {
        return mMonth;
    }

    public void setMonth(String month) {
        this.mMonth = month;
    }

    public int getDayOftheWeek() {
        return mDayOfTheWeek;
    }

    public void setDayOftheWeek(int mDayOftheWeek) {
        this.mDayOfTheWeek = mDayOftheWeek;
    }

    public int getWeekListPosition() {
        return weekListPosition;
    }

    public int getmWeekOfTheYear() {
        return mWeekOfTheYear;
    }

    //endregion


    @Override
    public String toString() {
        return "DayItem{"
                + "Date='"
                + mDate.toString()
                + ", value="
                + mDayOfTheMonth
                + '}';
    }
}
