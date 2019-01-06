package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by luj on 2018/10/29.
 */
public class CashInfoAccountBean implements Serializable{

    /**
     * id : 1
     * userId : 1
     * type : 1
     * status : 1
     * changebalance : 111
     * oldbalance : 111
     * newbalance : 11
     * remark : 1
     * createtime : 2018-10-23 09:50:05
     */

    /**
     * `id` bigint(20) NOT NULL COMMENT '自增id',
     * `userId` bigint(20) NOT NULL COMMENT '用户id（user表中id）',
     * `type` tinyint(4) NOT NULL COMMENT '1 余额 2虚拟余额',
     * `status` tinyint(4) NOT NULL COMMENT '1增加 0减少',
     * `changebalance` decimal(10,0) NOT NULL COMMENT '变更额度',
     * `oldbalance` decimal(10,0) NOT NULL COMMENT '原有额度',
     * `newbalance` decimal(10,0) NOT NULL COMMENT '新额度',
     * `remark` varchar(255) NOT NULL COMMENT '备注',
     * `createtime` datetime NOT NULL COMMENT '创建时间',
     * */

    private int id;
    private int userId;
    private int type;
    private int status;
    private double changebalance;
    private double oldbalance;
    private double newbalance;
    private String remark;
    private String createtime;

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
}
