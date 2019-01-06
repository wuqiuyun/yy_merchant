package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by wqy on 2018/12/19.
 */

public class IncomeBean implements Serializable {

    private int id;
    private int userId;
    private int type;
    private int status;
    private double changebalance;
    private double oldbalance;
    private double newbalance;
    private String remark;
    private String createtime;
    private String title;
    private int fromUserId;
    private int toUserId;
    private String fromUserAddress;
    private String toUserAddress;
    private double amount;
    private double fee;

    public IncomeBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getChangebalance() {
        return changebalance;
    }

    public void setChangebalance(double changebalance) {
        this.changebalance = changebalance;
    }

    public double getOldbalance() {
        return oldbalance;
    }

    public void setOldbalance(double oldbalance) {
        this.oldbalance = oldbalance;
    }

    public double getNewbalance() {
        return newbalance;
    }

    public void setNewbalance(double newbalance) {
        this.newbalance = newbalance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserAddress() {
        return fromUserAddress;
    }

    public void setFromUserAddress(String fromUserAddress) {
        this.fromUserAddress = fromUserAddress;
    }

    public String getToUserAddress() {
        return toUserAddress;
    }

    public void setToUserAddress(String toUserAddress) {
        this.toUserAddress = toUserAddress;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "IncomeBean{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", status=" + status +
                ", changebalance=" + changebalance +
                ", oldbalance=" + oldbalance +
                ", newbalance=" + newbalance +
                ", remark='" + remark + '\'' +
                ", createtime='" + createtime + '\'' +
                ", title='" + title + '\'' +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", fromUserAddress='" + fromUserAddress + '\'' +
                ", toUserAddress='" + toUserAddress + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                '}';
    }
}
