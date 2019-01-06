package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/11/12.
 */
public class DayBean {
    String week; // 星期
    String day; // 天
    String date; // 时间

    public DayBean(String week, String day, String date) {
        this.week = week;
        this.day = day;
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DayBean{" +
                "week='" + week + '\'' +
                ", day='" + day + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
