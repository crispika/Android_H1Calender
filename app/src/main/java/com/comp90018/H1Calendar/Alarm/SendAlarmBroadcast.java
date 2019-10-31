package com.comp90018.H1Calendar.Alarm;

import android.app.Activity;
import android.content.Intent;

public class SendAlarmBroadcast {
    public static void startAlarmService(Activity activity){
        Intent startAlarmServiceIntent = new Intent(activity, StartAlarmReceiver.class);
        activity.sendBroadcast(startAlarmServiceIntent,null);
//        Log.d("Notification","Send broadcast to start service");
    }
}
