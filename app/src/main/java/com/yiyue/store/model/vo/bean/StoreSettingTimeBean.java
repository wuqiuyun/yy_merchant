package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by lyj on 2018/10/22
 */
public class StoreSettingTimeBean implements Serializable {

    /**
     * endtime : 23:00:00
     * id : 788
     * stationlock : 0
     * open : 1
     * workday : 1,2,3
     * starttime : 10:00:00
     * station : 0
     * storeId : 753
     */

    private String endtime;
    private int id;
    private int stationlock;
    private int open;
    private String workday;
    private String starttime;
    private int station;
    private int storeId;

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationlock() {
        return stationlock;
    }

    public void setStationlock(int stationlock) {
        this.stationlock = stationlock;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
