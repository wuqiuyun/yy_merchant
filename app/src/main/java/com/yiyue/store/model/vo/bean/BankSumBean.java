package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by wqy on 2018/12/20.
 */

public class BankSumBean implements Serializable {

    /**
     * shortName : CMB
     * accountno : 48655845415456159
     * date : 2018-12
     * sumMoney : 100
     * bankName : 招商银行
     */

    private String shortName;
    private String accountno;
    private String date;
    private double sumMoney;
    private String bankName;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(double sumMoney) {
        this.sumMoney = sumMoney;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
