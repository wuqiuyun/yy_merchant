package com.yiyue.store.model.vo.bean;

/**
 * Created by lvlong on 2018/10/29.
 */
public class StoreSettingBean {

    /**
     * storeSettingInfo : {"id":1976,"endtime":"23:00:00","open":1,"starttime":"10:00:00","station":25,"stationlock":0,"storeId":1549,"workday":"2"}
     * categoryIds : 2
     */

    private StoreSettingInfoBean storeSettingInfo;
    private String categoryIds;

    public StoreSettingInfoBean getStoreSettingInfo() {
        return storeSettingInfo;
    }

    public void setStoreSettingInfo(StoreSettingInfoBean storeSettingInfo) {
        this.storeSettingInfo = storeSettingInfo;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public static class StoreSettingInfoBean {
        /**
         * id : 1976
         * endtime : 23:00:00
         * open : 1
         * starttime : 10:00:00
         * station : 25
         * stationlock : 0
         * storeId : 1549
         * workday : 2
         */

        private int id;
        private String endtime;
        private int open;
        private String starttime;
        private int station;
        private int stationlock;
        private int storeId;
        private String workday;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public int getOpen() {
            return open;
        }

        public void setOpen(int open) {
            this.open = open;
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

        public int getStationlock() {
            return stationlock;
        }

        public void setStationlock(int stationlock) {
            this.stationlock = stationlock;
        }

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public String getWorkday() {
            return workday;
        }

        public void setWorkday(String workday) {
            this.workday = workday;
        }
    }
}
