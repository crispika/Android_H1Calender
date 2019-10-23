package com.comp90018.H1Calendar;

import com.comp90018.H1Calendar.model.WeekItem;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.DateManager;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testCalendarManager(){
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        //前推10个月
        min.add(Calendar.MONTH, -10);
        min.add(Calendar.DAY_OF_MONTH, 1);

        //后推10个月
        max.add(Calendar.MONTH, 11);
        max.set(Calendar.DAY_OF_MONTH, 1);
        max.add(Calendar.DAY_OF_MONTH, -1);

        Locale locale = Locale.getDefault();

        CalendarManager.getInstance().initCalendar(min, max, locale);

        ArrayList<WeekItem> week_list = CalendarManager.getInstance().getWeekList();
        System.out.println("Size of week_list: " + week_list.size());
        System.out.println(week_list.get(0).getDayItems().get(0).getDate());

    }

    @Test
    public void testDateManager(){
        Locale locale = Locale.getDefault();
        Calendar cal = Calendar.getInstance(locale);

        System.out.println(DateManager.dateToStr(cal.getTime()));
        cal.add(Calendar.DATE,31);
        System.out.println(DateManager.dateToStr(cal.getTime()));
    }

    @Test
    public void testCalendar(){
        Calendar day = Calendar.getInstance();
        day.set(Calendar.WEEK_OF_YEAR, 20);
        day.set(Calendar.DAY_OF_WEEK,1);
        System.out.println(day.getTime().toString());
        day.set(Calendar.DAY_OF_WEEK,7);
        System.out.println(day.getTime().toString());
    }

    @Test
    public void testNotification(){

    }

}

