package com.comp90018.H1Calendar.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartAlarmService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);
    }
}
