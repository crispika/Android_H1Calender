package com.comp90018.H1Calendar.utils;
/**
 * 基础类
 * 用于储存地点信息的对象
 * 包含用户定义的地名以及相应坐标
 *
 */
public class EventLocation {
    private String name;
    private String coordinate;

    public EventLocation(){

    }

    public EventLocation(String name, String coordinate){
        this.name = name;
        this.coordinate = coordinate;
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
}
