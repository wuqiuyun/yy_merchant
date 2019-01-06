package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/10/27.
 */
public class OrderRefundBean {

    private long id;
    private long orderId;
    private double refundamount;
    private double handlingfee;
    private String remark;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getRefundamount() {
        return refundamount;
    }

    public void setRefundamount(double refundamount) {
        this.refundamount = refundamount;
    }

    public double getHandlingfee() {
        return handlingfee;
    }

    public void setHandlingfee(double handlingfee) {
        this.handlingfee = handlingfee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
