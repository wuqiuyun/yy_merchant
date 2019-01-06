package com.yiyue.store.model.vo.bean;

/**
 * Created by jinyan on 2018/12/27.
 */
public class UserOrderBean {

    /**
     * orderId : 1424
     * nickName : 我爱写bug
     * serviceName : 洗吹
     * successTime : 2018-12-14 10:13:29
     * amount : 159.8
     * orderNo : S2018121410123625818
     * userPhotoPath : https://yiyueuser.oss-cn-shenzhen.aliyuncs.com/2018-12-11/3a6fad9e2af5473e9f73c6e363cfcdf3-files
     */

    private String orderId;
    private String nickName;
    private String serviceName;
    private String successTime;
    private String amount;
    private String orderNo;
    private String userPhotoPath;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserPhotoPath() {
        return userPhotoPath;
    }

    public void setUserPhotoPath(String userPhotoPath) {
        this.userPhotoPath = userPhotoPath;
    }
}
