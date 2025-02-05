package com.comp90018.H1Calendar.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 静态类，用于储存日期间比较的方法
 * class to hold static methods for comparing dates and time.
 */

public class DateManager {

    public static boolean isSameDay(Calendar day1, Calendar day2){
        return day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR)
                && day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameWeek(Date day1, Date day2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(day1);
        cal2.setTime(day2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    public static String dateToStr(Date date){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);

//        String part1 = dateString.substring(0,3);
//        String part2 = dateString.substring(3);
//
//        if (part1.substring(0,1).equals("0"))
//            part1 = part1.substring(1);
//        if (part2.substring(0,1).equals("0"))
//            part2 = part2.substring(1);

//        return part1+part2;
        return dateString;
    }

    public static String[]  headTailOfWeek(int week_of_year){
        Calendar head = Calendar.getInstance();
        Calendar tail = Calendar.getInstance();
        head.set(Calendar.WEEK_OF_YEAR, week_of_year);// first day of the week
        tail.set(Calendar.WEEK_OF_YEAR, week_of_year);
        head.set(Calendar.DAY_OF_WEEK,1);
        tail.set(Calendar.DAY_OF_WEEK, 7); // last day of the week
        String[] index = {dateToStr(head.getTime()),dateToStr(tail.getTime())};
        return index;
    }
}
