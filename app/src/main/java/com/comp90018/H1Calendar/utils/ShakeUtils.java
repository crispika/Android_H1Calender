package com.comp90018.H1Calendar.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 实现摇一摇功能
 */
public class ShakeUtils implements SensorEventListener {

    private SensorManager mSensorManager;
    private OnShakeListener mOnShakeListener;
    private static final int ACCELER_VALUE = 14;


    public ShakeUtils(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void setOnShakeListener(OnShakeListener onShakeListener) {
        mOnShakeListener = onShakeListener;
    }


    public void register() {
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegister() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
            float[] values = sensorEvent.values;
            if (Math.abs(values[0]) > ACCELER_VALUE
                    || Math.abs(values[1]) > ACCELER_VALUE
                    || Math.abs(values[0]) > ACCELER_VALUE){
                mOnShakeListener.onShake();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface OnShakeListener {
        public void onShake();
    }

}
