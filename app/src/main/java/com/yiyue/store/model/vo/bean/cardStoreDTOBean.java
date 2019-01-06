package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/10/24.
 * 美发师名片 门店相关
 */
public class cardStoreDTOBean {

    private double distance;
    private String location;
    private String picture;
    private String storename;
    private long storeId;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}
