package com.comp90018.H1Calendar.Alarm;

import android.app.Activity;
import android.content.Intent;

public class SendAlarmBroadcast {
    public static void startAlarmService(Activity activity){
        Intent startAlarmServiceIntent = new Intent(activity,StartAlarmService.class);
        activity.sendBroadcast(startAlarmServiceIntent,null);
    }
}
