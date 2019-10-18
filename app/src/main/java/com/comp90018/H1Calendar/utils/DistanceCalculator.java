package com.comp90018.H1Calendar.utils;

import android.location.Location;

public abstract class DistanceCalculator {
    private static final int speed = 900;
    public static int distanceBetween(Location loc1, Location loc2){
        int distanceInMeters = Float.floatToIntBits(loc1.distanceTo(loc2));
        return distanceInMeters;
    }
    public static int approxTimeInMinute(int distance){
        int time = distance/speed;
        return time;
    }
}
