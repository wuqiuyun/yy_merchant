package com.yiyue.store.model.vo.bean;

import java.util.List;

/**
 * Created by wqy on 2018/12/20.
 */

public class BankDetailsBean {

    /**
     * bits : 1
     * takeSum : 0
     * takeLogs : [{"id":2342,"amount":100,"fee":1,"remark":"余额-转出到银行卡","status":"成功","createTime":"2018-12-12"}]
     */

    private int bits;
    private int takeSum;
    private int takeSize;
    private List<TakeLogsBean> takeLogs;

    public int getTakeSize() {
        return takeSize;
    }

    public void setTakeSize(int takeSize) {
        this.takeSize = takeSize;
    }

    public int getBits() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
    }

    public int getTakeSum() {
        return takeSum;
    }

    public void setTakeSum(int takeSum) {
        this.takeSum = takeSum;
    }

    public List<TakeLogsBean> getTakeLogs() {
        return takeLogs;
    }

    public void setTakeLogs(List<TakeLogsBean> takeLogs) {
        this.takeLogs = takeLogs;
    }

    public static class TakeLogsBean {
        /**
         * id : 2342
         * amount : 100
         * fee : 1
         * remark : 余额-转出到银行卡
         * status : 成功
         * createTime : 2018-12-12
         */

        private int id;
        private double amount;
        private double fee;
        private String remark;
        private String status;
        private String createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
