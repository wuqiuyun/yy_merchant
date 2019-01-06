package com.yiyue.store.model.vo.bean;

/**
 * Created by jinyan on 2018/12/29.
 */
public class StoreStylistBean {

    /**
     * nexustime : 2018-12-14 10:06:55
     * amount : 0.0
     * orderCount : 0
     * storeName : 只剪不染
     * position : 总监
     * stylistName : 橘子布鲁西
     */

    private String nexustime;
    private double amount;
    private int orderCount;
    private String storeName;
    private String position;
    private String stylistName;

    public String getNexustime() {
        return nexustime;
    }

    public void setNexustime(String nexustime) {
        this.nexustime = nexustime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStylistName() {
        return stylistName;
    }

    public void setStylistName(String stylistName) {
        this.stylistName = stylistName;
    }
}
