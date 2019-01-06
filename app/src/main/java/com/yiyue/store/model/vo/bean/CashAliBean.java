package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by lyj on 2018/11/8.
 */
public class CashAliBean implements Serializable{

    /**
     * bindId : 4949
     * userId : 1
     * realName : 郭小
     * type : ALI
     * accountno : adf
     * typeName : 支付宝
     * shortName : ALI
     */

    private int bindId;
    private int userId;
    private String realName;
    private String type;
    private String accountno;
    private String typeName;
    private String shortName;
    private boolean status;

    public CashAliBean(boolean status) {
        this.status = status;
    }

    public CashAliBean() {
    }

    public int getBindId() {
        return bindId;
    }

    public void setBindId(int bindId) {
        this.bindId = bindId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
