package com.comp90018.H1Calendar.Alarm;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.EventDetailActivity;
import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalenderEvent;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    String channelId = "calendar_event";
    String channelName = "Event is coming";
    int notificationId = 1;



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notification","Receive AlarmManager,show notification....");
        Bundle bundle = intent.getExtras();
//        CalenderEvent event = (CalenderEvent) bundle.getSerializable("alarm");
        String eventId = (String) bundle.get("id");
        CalenderEvent event = selectDb(context,eventId);

        Log.d("Notification",event.toJsonStr());
        showNotification(context,event);

        //restart the service to get alarm of next calendar event.
        Intent startService = new Intent(context, StartAlarmReceiver.class);
        context.sendBroadcast(startService);
    }

    private CalenderEvent selectDb(Context context, String eventId){
        sqliteHelper db = new sqliteHelper(context);
        CalenderEvent event = db.getEventByEventId(eventId);
        return event;
    }

    private void showNotification(Context context, CalenderEvent event){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager, channelId, channelName);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

        builder.setContentTitle(event.getTitle()+"is coming in one hour!")
                .setContentText("Your notes: " + event.getDescription())
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                //Clear Notification after click
                .setAutoCancel(true)
                //show all content of the notification
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{100, 200, 300, 400})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(Color.YELLOW, 300, 0);

        Intent intent = new Intent(context, EventDetailActivity.class);
        intent.putExtra("id", event.getEventId());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        Log.d("Notification","build notification");
        //set Notification id = 1
        notificationManager.notify(1,notification);
        Log.d("Notification","push notification");
    }

    private void createNotificationChannel(NotificationManager manager, String channelId, String channelName) {
        //only need after Android.0.
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
