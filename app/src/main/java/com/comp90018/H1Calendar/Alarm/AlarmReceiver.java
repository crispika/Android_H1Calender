package com.comp90018.H1Calendar.Alarm;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.comp90018.H1Calendar.EventDetailActivity;
import com.comp90018.H1Calendar.utils.CalenderEvent;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    String channelId = "calendar_event";
    String channelName = "Event is coming";
    int notificationId = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        CalenderEvent event = (CalenderEvent) bundle.getSerializable("alarm");
        showNotification(context,event);

        //restart the service to get alarm of next calendar event.
        Intent startService = new Intent(context,StartAlarmService.class);
        context.sendBroadcast(startService);
    }

    private void showNotification(Context context, CalenderEvent event){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager, channelId, channelName);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

        builder.setContentTitle(event.getTitle()+"is coming in one hour!")
                .setContentText("this is common notification content text")
                .setDefaults(Notification.DEFAULT_ALL)
//                .setSmallIcon()
//                .setLargeIcon()
                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setVibrate(new long[]{100, 200, 300, 400})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(Color.YELLOW, 300, 0);
        Intent intent = new Intent(context, EventDetailActivity.class);
        intent.putExtra("id", event.getEventId());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
    }

    private void createNotificationChannel(NotificationManager manager, String channelId, String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400});


            if (channel.canBypassDnd()) {
                channel.setBypassDnd(true);
            }

            if (channel.canShowBadge()) {
                channel.setShowBadge(true);
            }

            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
        }
    }

    private void showAlarm(Context context, CalenderEvent event){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(event.getTitle()+"is coming in one hour!");
    }
}
