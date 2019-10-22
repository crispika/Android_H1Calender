package com.comp90018.H1Calendar.utils;

import java.util.UUID;

/**
 * 基础类
 * 用于储存地点信息的对象
 * 包含用户定义的地名以及相应坐标
 *
 */
public class EventLocation {
    private String name;
    private String coordinate;
    private String locationId;
    private String userId;

    public EventLocation(){

    }

    public EventLocation(String name, String coordinate){
        this.name = name;
        this.coordinate = coordinate;

    }

    public EventLocation(String locationId, String name, String coordinate, String userId){
        this.locationId = locationId;
        this.name = name;
        this.coordinate = coordinate;
        this.userId = userId;
    }

    public EventLocation(String userId,String name, String coordinate){
        this.locationId = genLocationId();
        this.name = name;
        this.coordinate = coordinate;
        this.userId = userId;
    }

    public void printString(){
        System.out.println("save info:"+userId+' ' +locationId+' '+name+' '+coordinate);
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String genLocationId(){
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }
}
