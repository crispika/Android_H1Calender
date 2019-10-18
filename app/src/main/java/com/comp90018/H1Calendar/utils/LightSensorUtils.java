package com.comp90018.H1Calendar.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 用于光线传感器切换主题
 */
public class LightSensorUtils implements SensorEventListener {

    private static final Object mLock = new Object();
    private static LightSensorUtils instance;
    private static Context mContext;
    private SensorManager mSensorManager;
    boolean isDay;
    private final float dayLight = 40.0f;

    public static LightSensorUtils getInstance() {
        if (instance == null) {
            synchronized (mLock) {
                if (instance == null) {
                    instance = new LightSensorUtils();
                }
            }
        }
        return instance;
    }


    public void register(Context context) {
        mContext = context;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        Sensor light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegister() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    public boolean isDay() {
        return isDay;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                isDay = sensorEvent.values[0] > dayLight ? true : false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
