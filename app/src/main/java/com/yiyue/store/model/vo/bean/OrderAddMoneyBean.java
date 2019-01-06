package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/10/27.
 */
public class OrderAddMoneyBean {

    private long id;
    private String adddesc;
    private double addmoney;
    private String description;
    private long orderId;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdddesc() {
        return adddesc;
    }

    public void setAdddesc(String adddesc) {
        this.adddesc = adddesc;
    }

    public double getAddmoney() {
        return addmoney;
    }

    public void setAddmoney(double addmoney) {
        this.addmoney = addmoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
