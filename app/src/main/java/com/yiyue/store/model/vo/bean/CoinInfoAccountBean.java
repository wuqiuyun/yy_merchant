package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by luj on 2018/10/29.
 */
public class CoinInfoAccountBean implements Serializable{

    /**
     * id : 1305
     * fromUserId : 2
     * toUserId : 1
     * fromUserAddress : null
     * toUserAddress : null
     * amount : 2
     * fee : 0
     * createtime : 2018-10-24 21:12:53
     */
    /**
    * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
     * `fromUserId` bigint(20) NOT NULL COMMENT '转出用户id（user表中id），0系统奖励',
     * `fromAddress` varchar(255) DEFAULT NULL COMMENT 'coin转出地址',
     * `toUserId` bigint(20) NOT NULL COMMENT '转入用户id（user表中id）',
     *  `toAddress` varchar(255) DEFAULT NULL COMMENT '转入地址',
     * `amount` bigint(20) NOT NULL COMMENT 'coin转出额度',
     *  `fee` bigint(20) NOT NULL COMMENT '手续费',
     * `createtime` datetime NOT NULL COMMENT '创建时间',
    * */

    private int id;
    private int fromUserId;
    private int toUserId;
    private Object fromUserAddress;
    private Object toUserAddress;
    private double amount;
    private double fee;
    private String createtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Object getFromUserAddress() {
        return fromUserAddress;
    }

    public void setFromUserAddress(Object fromUserAddress) {
        this.fromUserAddress = fromUserAddress;
    }

    public Object getToUserAddress() {
        return toUserAddress;
    }

    public void setToUserAddress(Object toUserAddress) {
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
