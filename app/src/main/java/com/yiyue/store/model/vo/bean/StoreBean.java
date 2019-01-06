package com.yiyue.store.model.vo.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zm on 2018/10/10.
 */
public class StoreBean implements Serializable{

    private double distance;
    private String location;
    private ArrayList<String> serves;
    private String storeCover;
    private long storeId; // 店铺id
    private String storeName;
    private long userId;

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public ArrayList<String> getServes()
    {
        return serves;
    }

    public void setServes(ArrayList<String> serves)
    {
        this.serves = serves;
    }

    public String getStoreCover()
    {
        return storeCover;
    }

    public void setStoreCover(String storeCover)
    {
        this.storeCover = storeCover;
    }

    public long getStoreId()
    {
        return storeId;
    }

    public void setStoreId(long storeId)
    {
        this.storeId = storeId;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }
}
