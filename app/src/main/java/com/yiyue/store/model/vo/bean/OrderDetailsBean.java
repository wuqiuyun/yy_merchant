package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/10/27.
 */
public class OrderDetailsBean {

    private long id;
    private long userId;
    private long storeId;
    private long stylistId;
    private long servicemodel; // 1普通订单  2套餐
    private long serviceId;
    private long packageId;
    private String ordername;
    private String orderno;
    private double orderamount; //项目价格
    private double payamount;   //服务总计
    private int status;
    private double handlingfee;
    private long starttime;
    private long endtime;
    private long canceltime;
    private long refundtime;
    private String userPath;
    private String userNickname;
    private int userGender;
    private String userLabel;
    private String storeName;
    private int nexus;
    private String stylistname;
    private long ordertime;
    private String datename;
    private OrderAddMoneyBean orderAddMoney;
    private OrderRefundBean orderRefund;
    private String clearamount;
    private long oldordertime;
    private String remark;

    private int coupontype; // 1 满减 2折扣 3抵扣
    private Double couponamount; // 优惠金额和折扣
    private int coupondirection; // 1 平台 2 美发师

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getOldordertime() {
        return oldordertime;
    }

    public void setOldordertime(long oldordertime) {
        this.oldordertime = oldordertime;
    }

    public int getCoupontype() {
        return coupontype;
    }

    public void setCoupontype(int coupontype) {
        this.coupontype = coupontype;
    }

    public Double getCouponamount() {
        return couponamount;
    }

    public void setCouponamount(Double couponamount) {
        this.couponamount = couponamount;
    }

    public int getCoupondirection() {
        return coupondirection;
    }

    public void setCoupondirection(int coupondirection) {
        this.coupondirection = coupondirection;
    }

    public String getClearamount() {
        return clearamount;
    }

    public void setClearamount(String clearamount) {
        this.clearamount = clearamount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getStylistId() {
        return stylistId;
    }

    public void setStylistId(long stylistId) {
        this.stylistId = stylistId;
    }

    public long getServicemodel() {
        return servicemodel;
    }

    public void setServicemodel(long servicemodel) {
        this.servicemodel = servicemodel;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public double getOrderamount() {
        return orderamount;
    }

    public void setOrderamount(double orderamount) {
        this.orderamount = orderamount;
    }

    public double getPayamount() {
        return payamount;
    }

    public void setPayamount(double payamount) {
        this.payamount = payamount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getHandlingfee() {
        return handlingfee;
    }

    public void setHandlingfee(double handlingfee) {
        this.handlingfee = handlingfee;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public long getCanceltime() {
        return canceltime;
    }

    public void setCanceltime(long canceltime) {
        this.canceltime = canceltime;
    }

    public long getRefundtime() {
        return refundtime;
    }

    public void setRefundtime(long refundtime) {
        this.refundtime = refundtime;
    }

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getNexus() {
        return nexus;
    }

    public void setNexus(int nexus) {
        this.nexus = nexus;
    }

    public String getStylistname() {
        return stylistname;
    }

    public void setStylistname(String stylistname) {
        this.stylistname = stylistname;
    }

    public long getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(long ordertime) {
        this.ordertime = ordertime;
    }

    public String getDatename() {
        return datename;
    }

    public void setDatename(String datename) {
        this.datename = datename;
    }

    public OrderAddMoneyBean getOrderAddMoney() {
        return orderAddMoney;
    }

    public void setOrderAddMoney(OrderAddMoneyBean orderAddMoney) {
        this.orderAddMoney = orderAddMoney;
    }

    public OrderRefundBean getOrderRefund() {
        return orderRefund;
    }

    public void setOrderRefund(OrderRefundBean orderRefund) {
        this.orderRefund = orderRefund;
    }
}
