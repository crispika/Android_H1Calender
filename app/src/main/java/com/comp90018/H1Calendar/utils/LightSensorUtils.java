package com.comp90018.H1Calendar.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * For light sensor switching themes
 */
public class LightSensorUtils implements SensorEventListener {

    private int count = 0;
    private boolean isOnAuto = true;
    private boolean nowIsDay = true;

    public boolean isOnAuto() {
        return isOnAuto;
    }

    public void setOnAuto(boolean onAuto) {
        isOnAuto = onAuto;
    }

    public interface LightListener {
        public void toLight();
        public void toDark();
    }



    private LightListener mlightListener;
    private SensorManager mSensorManager;
    boolean isDay;
    private final float DAY_LIGHT = 70.0f;


    public void setDark(){
        mlightListener.toDark();
    }

    public void  setLight(){
        mlightListener.toLight();
    }

    public LightSensorUtils(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void setLightListener(LightListener lightListener) {
        this.mlightListener = lightListener;
    }


    public void register() {
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegister() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        System.out.println(isOnAuto);
        if (!isOnAuto){
            return;
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            int i = 0;
            Log.d("Theme", "Sensor Changed " + count + nowIsDay);
            if (nowIsDay){
                if (sensorEvent.values[0] < DAY_LIGHT){
                    count++;
                }
                else{
                    count = 0;
                }
                if (count > 2){
                    nowIsDay = false;
                    count = 0;
                    mlightListener.toDark();
                }
            }else{
                if (sensorEvent.values[0] > DAY_LIGHT){
                    count++;
                }
                else{
                    count = 0;
                }
                if (count > 2){
                    nowIsDay = true;
                    count = 0;
                    mlightListener.toLight();
                }
            }

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
