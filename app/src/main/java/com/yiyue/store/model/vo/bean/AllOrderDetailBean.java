package com.yiyue.store.model.vo.bean;

/**
 * Created by wqy on 2018/12/5.
 */

public class AllOrderDetailBean {

    /**
     * orderno : S2018112813405954581
     * nickname : 糖豆9
     * userPath : https://yiyuestore.oss-cn-shenzhen.aliyuncs.com/2018-11-23/2fa1f26cf86c4b1599a49c62c28a4882-files
     * orderName : 洗剪吹
     * orderendtime : 1543393109000
     * payamount : 99
     * clearamount : 0
     */

    private String orderno;
    private String nickname;
    private String userPath;
    private String orderName;
    private long orderendtime;
    private double payamount;
    private double clearamount;
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public long getOrderendtime() {
        return orderendtime;
    }

    public void setOrderendtime(long orderendtime) {
        this.orderendtime = orderendtime;
    }

    public double getPayamount() {
        return payamount;
    }

    public void setPayamount(double payamount) {
        this.payamount = payamount;
    }

    public double getClearamount() {
        return clearamount;
    }

    public void setClearamount(double clearamount) {
        this.clearamount = clearamount;
    }
}
