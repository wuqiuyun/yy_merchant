package com.yiyue.store.model.vo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyj on 2018/10/23.
 */
public class StoreManageScopeInfoBean implements Serializable{

    /**
     * storeId : 1
     * storeName : 你说什么我
     * location : 月球
     * distance : 11877973
     * storePhotos : ["http://source.yunyl.com/photo/1.jpg","http://source.yunyl.com/photo/2.jpg","http://source.yunyl.com/photo/3.jpg"]
     * grade : 4.8
     * isCollection : 1
     */

    private int storeId;
    private String storeName;
    private String location;
    private double distance;
    private double grade;
    private int isCollection;
    private List<String> storePhotos;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
    }

    public List<String> getStorePhotos() {
        return storePhotos == null ? new ArrayList<>() : storePhotos;
    }

    public void setStorePhotos(List<String> storePhotos) {
        this.storePhotos = storePhotos;
    }
}
