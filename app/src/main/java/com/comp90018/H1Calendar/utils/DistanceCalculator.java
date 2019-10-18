package com.comp90018.H1Calendar.utils;

import android.location.Location;
/**
 * 抽象类
 * 用于计算坐标间距离 以及预估时间
 */
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
