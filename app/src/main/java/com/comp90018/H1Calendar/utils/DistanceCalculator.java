package com.comp90018.H1Calendar.utils;

import android.location.Location;

import java.text.DecimalFormat;

/**
 * abstract class
 * Used to calculate the distance between coordinates and the estimated time
 */
public abstract class DistanceCalculator {
    private static final int speed = 900;
    public static String distanceBetween(Location loc1, Location loc2){
        DecimalFormat df = new DecimalFormat("0.00");
        float distanceInMeters = loc1.distanceTo(loc2);

        if(distanceInMeters>1000){
            return (df.format(distanceInMeters/1000) + " km");
        }else {
            return df.format(distanceInMeters) + " m";
        }

    }
    public static int approxTimeInMinute(int distance){
        int time = distance/speed;
        return time;
    }
}
