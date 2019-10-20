package com.comp90018.H1Calendar.utils;

import com.comp90018.H1Calendar.model.DayItem;
import com.comp90018.H1Calendar.model.MonthItem;
import com.comp90018.H1Calendar.model.WeekItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarManager {

    private static CalendarManager calendarManager;

    private Calendar calendar = Calendar.getInstance();
    /**
     * 传入参数，设置calendar的地区
     */
    private Locale locale;

    /**
     * 日历开始的最小、最大日期
     */
    private Calendar min_date, max_Date;

    /**
     * lists store month/week information for the calendar
     */
    private ArrayList<MonthItem> month_list = new ArrayList<>();
    private ArrayList<WeekItem> week_list = new ArrayList<>();

    /**
     * Keep information of current date
     */
    private Calendar today;

    private DayItem todayItem;


    //region Constructor
    public CalendarManager() {

    }

    public static CalendarManager getInstance() {
        if (calendarManager == null) {
            calendarManager = new CalendarManager();
        }
        return calendarManager;
    }
    //endregion

    public void initCalendar(Calendar min_date, Calendar max_date, Locale locale) {
        this.locale = locale;
        this.max_Date = max_date;
        this.min_date = min_date;
        today = Calendar.getInstance(locale);
        System.out.println("min_date: " + min_date.getTime().toString());
        System.out.println("max_date: " + max_date.getTime().toString());
        System.out.println("Today is: " + today.getTime().toString());

        //empty containers
        month_list.clear();
        week_list.clear();

        //A counter to loop from min_date to max_date
        Calendar week_counter = Calendar.getInstance();
        week_counter.setTime(min_date.getTime());


        while (week_counter.getTime().before(max_date.getTime())) {

            //A temperate holder for month#
            int temp_month = week_counter.get(Calendar.MONTH);
            MonthItem monthItem = new MonthItem(week_counter.get(Calendar.YEAR), week_counter.get(Calendar.MONTH));
            ArrayList<WeekItem> week_list_of_month = new ArrayList<>();

            while (week_counter.get(Calendar.MONTH) == temp_month) {
                WeekItem weekItem = getWeekItem(week_counter);
                week_list_of_month.add(weekItem);
                week_list.add(weekItem);
                week_counter.add(Calendar.WEEK_OF_YEAR, 1);
            }
            monthItem.setWeeks(week_list);
            month_list.add(monthItem);

        }
    }

    private WeekItem getWeekItem(Calendar week_counter) {

        int year = week_counter.get(Calendar.YEAR);
        int month = week_counter.get(Calendar.MONTH) + 1;
        int week_of_year = week_counter.get(Calendar.WEEK_OF_YEAR);
        int week_list_position = week_list.size();
        Date date = week_counter.getTime();

        WeekItem weekItem = new WeekItem(year, month, week_of_year, date);

        //将这周include的dayitem列表加入weekItem中
        weekItem.setDayItems(getDayItemList(year, week_of_year,week_list_position));

        return weekItem;
    }

    private ArrayList<DayItem> getDayItemList(int year, int week_of_year, int week_list_postion) {

        Calendar temp_cal = Calendar.getInstance(locale);
        temp_cal.set(Calendar.YEAR, year);
        temp_cal.set(Calendar.WEEK_OF_YEAR, week_of_year);
        temp_cal.set(Calendar.DAY_OF_WEEK, 1);

        ArrayList<DayItem> day_list = new ArrayList<>();
        while (temp_cal.get(Calendar.WEEK_OF_YEAR) == week_of_year) {

            Date date = temp_cal.getTime();
            int day_of_month = temp_cal.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat monthNameFormat = new SimpleDateFormat("MMM",locale);
            SimpleDateFormat monthFullNameFormat = new SimpleDateFormat("MMMM",locale);
            String monthName = monthNameFormat.format(date);
            String fullMonthName = monthFullNameFormat.format(date);
            boolean isToday = DateManager.isSameDay(temp_cal, today);

            DayItem dayItem = new DayItem(date, day_of_month, isToday, monthName,fullMonthName,week_list_postion,week_of_year);
            if(isToday) todayItem =dayItem;
            day_list.add(dayItem);

            //日期往后推一天
            temp_cal.add(Calendar.DATE, 1);
        }
        return day_list;
    }


    //region Getter/Setter
    public Date getToday(){return today.getTime();}

    public Calendar getTodayCalendar(){return today;}

    public ArrayList<MonthItem> getMonthList() {
        return month_list;
    }

    public ArrayList<WeekItem> getWeekList() {
        return week_list;
    }

    public DayItem getTodayItem() {
        return todayItem;
    }

    //endregion
}


