package com.yiyue.store.module.home.service;

/**
 * Created by Lizhuo on 2018/10/11.
 */
public class BusinessTimeBean
{
    private String day;
    private String beginTime;
    private String closeTime;

    public BusinessTimeBean(String day, String beginTime, String closeTime)
    {
        this.day = day;
        this.beginTime = beginTime;
        this.closeTime = closeTime;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public String getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getCloseTime()
    {
        return closeTime;
    }

    public void setCloseTime(String closeTime)
    {
        this.closeTime = closeTime;
    }
}
