package com.comp90018.H1Calendar.Alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.CalenderEvent;
import com.comp90018.H1Calendar.utils.DateManager;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AlarmService extends Service {

    private sqliteHelper db;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("Notification","just startService...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                CalenderEvent event = getNext();
                if (event != null) {
                    event.setAlarm(getApplicationContext());
                    Log.d("Notification","service already started");
                }
            }
        }).start();
        return START_STICKY;
    }

    private CalenderEvent getNext() {
//        Log.d("Notification","start getNext()");
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTime(CalendarManager.getInstance().getToday());
        tomorrow.add(Calendar.DATE,1);

        db = new sqliteHelper(getApplicationContext());
        List<CalenderEvent> todayEvents = db.getEventsByDay(DateManager.dateToStr(CalendarManager.getInstance().getToday()));
        List<CalenderEvent> tomorrowEvents = db.getEventsByDay(DateManager.dateToStr(tomorrow.getTime()));

        todayEvents.addAll(tomorrowEvents);
        if (todayEvents.size() == 0) return null;

        //sort the event list to make earlier events before later events.
        Collections.sort(todayEvents);

        CalenderEvent nextEvent;
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(System.currentTimeMillis());
        for (CalenderEvent event : todayEvents) {
//            if (event.getIsNeedNotify() && event.getAlarmTime().getTimeInMillis() < currentTime.getTimeInMillis()) {
            if (event.getIsNeedNotify() && event.getAlarmTime().after(currentTime)) {
                Log.d("Notification", "CurrentTime:  "+currentTime.getTime().toString());
                return event;
            }
        }
        Log.d("Notification","Currently there is no event to notify.");
        return null;
    }

}
