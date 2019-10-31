package com.comp90018.H1Calendar.utils;

import java.util.UUID;

/**
 * basic class
 * Object for storing location information
 * Contains user-defined place names and corresponding coordinates
 *
 */
public class EventLocation {
    private String name;
    private String coordinate;
    private String locationId;
    private String userId;

    public EventLocation(){

    }

    public EventLocation(String userId, String name, String coordinate){
        this.userId = userId;
        this.name = name;
        this.coordinate = coordinate;
        this.locationId = genLocationId();

    }

    public EventLocation(String locationId, String name, String coordinate, String userId){
        this.locationId = locationId;
        this.name = name;
        this.coordinate = coordinate;
        this.userId = userId;
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
