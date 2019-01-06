package com.yiyue.store.model.vo.bean;

/**
 * Created by lyj on 2018/10/31.
 */
public class SecurityInfoBean {

    /**
     * aliToken : string
     * bindAli : 0
     * bindWX : 0
     * mobile : string
     * userId : 0
     * userName : string
     * wxToken : string
     */

    private String aliToken;
    private int bindAli;
    private int bindWX;
    private String mobile;
    private int userId;
    private String userName;
    private String wxToken;
    private int isAuth;
    private String realName;

    public String getAliToken() {
        return aliToken;
    }

    public void setAliToken(String aliToken) {
        this.aliToken = aliToken;
    }

    public int getBindAli() {
        return bindAli;
    }

    public void setBindAli(int bindAli) {
        this.bindAli = bindAli;
    }

    public int getBindWX() {
        return bindWX;
    }

    public void setBindWX(int bindWX) {
        this.bindWX = bindWX;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWxToken() {
        return wxToken;
    }

    public void setWxToken(String wxToken) {
        this.wxToken = wxToken;
    }

    public int getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}
