package com.yiyue.store.model.vo.bean;

/**
 * Created by wqy on 2018/12/24.
 */

public class BillDetailsBean {

    /**
     * type : 1
     * assetType : 1
     * ammount : 3
     * createtTime : 2018-12-18 01:00
     * status : 交易成功
     * typeName : 服务收入
     * remark : null
     * otherAccount : null
     * userAccount : null
     * classify : 酬劳
     * userImg : null
     * userName : null
     */

    private int type;
    private int assetType;
    private double ammount;
    private String createtTime;
    private String status;
    private String typeName;
    private String remark;
    private String otherAccount;
    private String userAccount;
    private String classify;
    private String userImg;
    private String  userName;
    private String billName;

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAssetType() {
        return assetType;
    }

    public void setAssetType(int assetType) {
        this.assetType = assetType;
    }

    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }

    public String getCreatetTime() {
        return createtTime;
    }

    public void setCreatetTime(String createtTime) {
        this.createtTime = createtTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(String otherAccount) {
        this.otherAccount = otherAccount;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
