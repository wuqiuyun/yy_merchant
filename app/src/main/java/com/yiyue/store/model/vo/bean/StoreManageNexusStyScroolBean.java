package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by lyj on 2018/10/23.
 */
public class StoreManageNexusStyScroolBean implements Serializable{

    /**
     * stylistId : 1
     * stylistName : 我是美发师
     * coverImg : https://source.yunyl.com/cover/stylist/1.jpg
     * userId : 1
     */

    private int stylistId;
    private String stylistName;
    private String coverImg;
    private int userId;

    public int getStylistId() {
        return stylistId;
    }

    public void setStylistId(int stylistId) {
        this.stylistId = stylistId;
    }

    public String getStylistName() {
        return stylistName;
    }

    public void setStylistName(String stylistName) {
        this.stylistName = stylistName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
