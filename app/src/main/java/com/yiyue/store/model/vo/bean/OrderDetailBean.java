package com.yiyue.store.model.vo.bean;

/**
 * Created by jinyan on 2018/12/21.
 */
public class OrderDetailBean {

    /**
     * orderId : 903
     * userPhotoPath : https://yiyueuser.oss-cn-shenzhen.aliyuncs.com/2018-12-10/3e14184c71464b2e95169e4bce3d7599-files
     * nickName : 科比
     * storeName : 仲嵩磊门店
     * orderName : 洗剪吹套餐
     * payAmount : 0
     * amount : 105.6
     * cleartime : 2018-12-17 00:00:00
     * createtime : 2018-12-10 22:37:25
     * isclear : 1   1是清算完成0是已完成
     */

    private int orderId;
    private String userPhotoPath;
    private String nickName;
    private String storeName;
    private String orderName;
    private double payAmount;
    private double amount;
    private String cleartime;
    private String createtime;
    private int isclear;

    public int getIsclear() {
        return isclear;
    }

    public void setIsclear(int isclear) {
        this.isclear = isclear;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUserPhotoPath() {
        return userPhotoPath;
    }

    public void setUserPhotoPath(String userPhotoPath) {
        this.userPhotoPath = userPhotoPath;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCleartime() {
        return cleartime;
    }

    public void setCleartime(String cleartime) {
        this.cleartime = cleartime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
