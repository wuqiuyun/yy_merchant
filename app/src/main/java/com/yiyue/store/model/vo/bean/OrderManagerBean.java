package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/12/17.
 */
public class OrderManagerBean {
    private String orderId;
    private String stylistname;
    private String time;
    private String usernickname;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStylistname() {
        return stylistname;
    }

    public void setStylistname(String stylistname) {
        this.stylistname = stylistname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsernickname() {
        return usernickname;
    }

    public void setUsernickname(String usernickname) {
        this.usernickname = usernickname;
    }
}
