package com.comp90018.H1Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class WiFiAutoSync extends Service {
    private Timer timer = null;
    // each 10 seconds, execute timer.scheduleAtFixedRate once
    static final long PERIOD = 10 * 1000;
    // 0 seconds, between calling timer.scheduleAtFixedRate and executing run
    static final int DELAY = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.e("background service", "===========create=======");
    }

    // background service, showing toast message to indicate whether sync successfully
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (null == timer ) {
            timer = new Timer();
        }

        timer.scheduleAtFixedRate(new TimerTask() {

            // calling UI handler to show the message
            private Handler updateUI = new Handler(){
                @Override
                public void dispatchMessage(Message message){
                    super.dispatchMessage(message);
                    showToastMessage();
                }
            };

            @Override
            public void run() {
                try{

                    if (isWifiConnected(getApplicationContext())) {
                        System.out.println("wifi");
                        // sync
                        // syncToCloud();
                        updateUI.sendEmptyMessage(0);
                    }
                }
                catch (Exception e) {e.printStackTrace(); }
            }
        }, DELAY, PERIOD);

        return super.onStartCommand(intent, flags, startId);
    }

    // check for wifi
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public void showToastMessage(){
        Log.e("background service", "===========runnable=======");
        Toast.makeText(getApplicationContext(),"sync successfully", Toast.LENGTH_LONG).show();
    }

    // syncToCloud();

    @Override
    public void onDestroy(){
        Log.e("background service", "===========destroy=======");
        super.onDestroy();
    }
}
