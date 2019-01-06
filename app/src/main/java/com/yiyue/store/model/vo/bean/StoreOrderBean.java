package com.yiyue.store.model.vo.bean;

/*
    订单管理
 * Create by lvlong on  2018/10/26
 */

import java.util.List;

public class StoreOrderBean {
    /**
     * allReceiptNum : 4
     * allReceiptMoney : 30.192
     * allSuccessNum : 1
     * allSuccessMoney : 0
     * orderCategoryCount : [{"name":"洗吹","receiptNum":3,"receiptMoney":1.392,"successNum":1,"successMoney":0},{"name":"染发","receiptNum":1,"receiptMoney":28.8,"successNum":0,"successMoney":0}]
     */

    private int allReceiptNum;
    private double allReceiptMoney;
    private int allSuccessNum;
    private double allSuccessMoney;
    private List<OrderCategoryCountBean> orderCategoryCount;

    public int getAllReceiptNum() {
        return allReceiptNum;
    }

    public void setAllReceiptNum(int allReceiptNum) {
        this.allReceiptNum = allReceiptNum;
    }

    public double getAllReceiptMoney() {
        return allReceiptMoney;
    }

    public void setAllReceiptMoney(double allReceiptMoney) {
        this.allReceiptMoney = allReceiptMoney;
    }

    public int getAllSuccessNum() {
        return allSuccessNum;
    }

    public void setAllSuccessNum(int allSuccessNum) {
        this.allSuccessNum = allSuccessNum;
    }

    public double getAllSuccessMoney() {
        return allSuccessMoney;
    }

    public void setAllSuccessMoney(double allSuccessMoney) {
        this.allSuccessMoney = allSuccessMoney;
    }

    public List<OrderCategoryCountBean> getOrderCategoryCount() {
        return orderCategoryCount;
    }

    public void setOrderCategoryCount(List<OrderCategoryCountBean> orderCategoryCount) {
        this.orderCategoryCount = orderCategoryCount;
    }

    public static class OrderCategoryCountBean {
        /**
         * name : 洗吹
         * receiptNum : 3
         * receiptMoney : 1.392
         * successNum : 1
         * successMoney : 0
         */

        private String name;
        private int receiptNum;
        private double receiptMoney;
        private int successNum;
        private double successMoney;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getReceiptNum() {
            return receiptNum;
        }

        public void setReceiptNum(int receiptNum) {
            this.receiptNum = receiptNum;
        }

        public double getReceiptMoney() {
            return receiptMoney;
        }

        public void setReceiptMoney(double receiptMoney) {
            this.receiptMoney = receiptMoney;
        }

        public int getSuccessNum() {
            return successNum;
        }

        public void setSuccessNum(int successNum) {
            this.successNum = successNum;
        }

        public double getSuccessMoney() {
            return successMoney;
        }

        public void setSuccessMoney(double successMoney) {
            this.successMoney = successMoney;
        }

    }
//    private String orderNum;                //总订单
//    private String verificationOrderNum;    //待核销订单
//    private String incomeMoney;             //总收入
//    private String achievementMoeny;        //总业绩
}
