package com.yiyue.store.model.vo.bean;

/*
    代币余额信息
 * Create by lvlong on  2018/10/27
 */

public class CoinInfoBean {
    private String id;          //代币钱包ID
    private String coinId;      //代币类型
    private double balance;     //代币余额
    private String address;     //钱包地址
    private String nlock;       //钱包是否锁定 0:没有; 1:锁定
    private double rate;        //代币总价值

    public CoinInfoBean(String id, String coinId, double balance, String address, String nlock ,double rate) {
        this.id = id;
        this.coinId = coinId;
        this.balance = balance;
        this.address = address;
        this.nlock = nlock;
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNlock() {
        return nlock;
    }

    public void setNlock(String nlock) {
        this.nlock = nlock;
    }
}
