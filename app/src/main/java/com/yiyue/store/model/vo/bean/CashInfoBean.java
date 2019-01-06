package com.yiyue.store.model.vo.bean;

/*
 * Create by lvlong on  2018/10/27
 */

public class CashInfoBean {

    private String walletId;         //钱包ID
    private String balance;         //余额
    private String freezebalance;   //冻结余额
    private String virtuall;        //虚拟余额
    private String freezevirtual;   //冻结虚拟余额

    public CashInfoBean(String id, String balance, String freezebalance, String virtuall, String freezevirtual) {
        this.walletId = id;
        this.balance = balance;
        this.freezebalance = freezebalance;
        this.virtuall = virtuall;
        this.freezevirtual = freezevirtual;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFreezeBalance() {
        return freezebalance;
    }

    public void setFreezeBalance(String freezeBalance) {
        this.freezebalance = freezeBalance;
    }

    public String getVirtuall() {
        return virtuall;
    }

    public void setVirtuall(String virtuall) {
        this.virtuall = virtuall;
    }

    public String getFreezeVirtual() {
        return freezevirtual;
    }

    public void setFreezeVirtual(String freezeVirtual) {
        this.freezevirtual = freezeVirtual;
    }
}
