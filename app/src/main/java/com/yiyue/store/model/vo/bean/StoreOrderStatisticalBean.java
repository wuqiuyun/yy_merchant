package com.yiyue.store.model.vo.bean;

/**
 * 订单统计
 * <p>
 * Created by zm on 2018/10/24.
 */
public class StoreOrderStatisticalBean {

    private int pendingSum; // 待确认
    private int acceptAndServiceSum; // 待核销
    private int successSum; // 已完成
    private int refundSum; // 已退款

    public int getPendingSum() {
        return pendingSum;
    }

    public void setPendingSum(int pendingSum) {
        this.pendingSum = pendingSum;
    }

    public int getAcceptAndServiceSum() {
        return acceptAndServiceSum;
    }

    public void setAcceptAndServiceSum(int acceptAndServiceSum) {
        this.acceptAndServiceSum = acceptAndServiceSum;
    }

    public int getSuccessSum() {
        return successSum;
    }

    public void setSuccessSum(int successSum) {
        this.successSum = successSum;
    }

    public int getRefundSum() {
        return refundSum;
    }

    public void setRefundSum(int refundSum) {
        this.refundSum = refundSum;
    }
}
