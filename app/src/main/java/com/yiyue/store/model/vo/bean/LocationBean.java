package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/11/22.
 * 坐标点的经纬度和名字
 */
public class LocationBean {
    private double longitude;
    private double latitude;
    private String name;

    public LocationBean() {
    }

    public LocationBean(double longitude, double latitude, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
