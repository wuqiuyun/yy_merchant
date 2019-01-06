package com.yiyue.store.model.vo.bean;

/**
 * Created by wqy on 2018/12/24.
 */

public class TotalBillBean {

    /**
     * billId : 10215
     * type : 1
     * inName : 转可提现
     * remark : 清算
     * createTime : 2018-12-21 01:00:02
     * amount : 6
     * inType : 9
     */

    private String billId;
    private int type;
    private String inName;
    private String remark;
    private String createTime;
    private double amount;
    private int inType;
    private String userImg; // 头像，类型为转账或红包时使用

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInName() {
        return inName;
    }

    public void setInName(String inName) {
        this.inName = inName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getInType() {
        return inType;
    }

    public void setInType(int inType) {
        this.inType = inType;
    }
}
