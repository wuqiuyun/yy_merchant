package com.yiyue.store.model.vo.bean;

/**
 * Created by jinyan on 2018/12/21.
 */
public class RegisterGapBetweenBean {
    @Override
    public String toString() {
        return "RegisterGapBetweenBean{" +
                "amountCount=" + amountCount +
                ", registerCount=" + registerCount +
                '}';
    }

    /**
     * amountCount : 180.85
     * registerCount : 4
     */

    private double amountCount;
    private int registerCount;

    public double getAmountCount() {
        return amountCount;
    }

    public void setAmountCount(double amountCount) {
        this.amountCount = amountCount;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }
}
