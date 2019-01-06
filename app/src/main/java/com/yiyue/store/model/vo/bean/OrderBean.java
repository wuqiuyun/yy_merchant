package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/9/20.
 */
public class OrderBean {

    private long id;
    private long userId;
    private long storeId;
    private long stylistId;
    private int servicemodel;
    private String serviceId;
    private String packageId;
    private String ordername;
    private String orderno; // 订单编号
    private double orderamount;
    private double payamount;
    private int status;
    private String handlingfee;
    private long starttime;
    private long endtime;
    private long canceltime;
    private long refundtime;
    private String userPath;
    private String userNickname;
    private int userGender;
    private String userLabel;
    private long ordertime;
    private String datename;
    private String stylistname;
    private int nexus;
    private String describe;
    private int coupontype; // 满减 2 折扣 3 抵扣券

    public int getCoupontype() {
        return coupontype;
    }

    public void setCoupontype(int coupontype) {
        this.coupontype = coupontype;
    }

    public String getId() {
        return String.valueOf(id);
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

    public int getServicemodel() {
        return servicemodel;
    }

    public void setServicemodel(int servicemodel) {
        this.servicemodel = servicemodel;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
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

    public String getOrderamount() {
        return String.valueOf(orderamount);
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

    public String getHandlingfee() {
        return handlingfee;
    }

    public void setHandlingfee(String handlingfee) {
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

    public String getStylistname() {
        return stylistname;
    }

    public void setStylistname(String stylistname) {
        this.stylistname = stylistname;
    }

    public int getNexus() {
        return nexus;
    }

    public void setNexus(int nexus) {
        this.nexus = nexus;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
