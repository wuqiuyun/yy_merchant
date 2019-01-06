package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by zm on 2018/9/9.
 */
public class UserBean implements Cloneable, Serializable{

    private int id;
    private String createtime;
    private int gender;
    private String idNo;
    private String mobile;
    private String nickname;
    private int role;
    private int status;
    private String updatetime;
    private String username;
    private String token;
    private int userStatus; // 1未注册 2已注册 3未认证 0认证 4绑定手机号码
    private String openId;
    private String impassword;
    private String imusername;
    private long storeId; // 店铺id
    private String headImg;
    private int shutdown; //是否接收新消息通知 0通知，1不通知

    @Override
    public UserBean clone() throws CloneNotSupportedException {
        return (UserBean) super.clone();
    }

    public int getShutdown() {
        return shutdown;
    }

    public void setShutdown(int shutdown) {
        this.shutdown = shutdown;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getImpassword() {
        return impassword;
    }

    public void setImpassword(String impassword) {
        this.impassword = impassword;
    }

    public String getImusername() {
        return imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    public String getId() {
        return id == 0 ? "" : String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStoreId() {
        return this.storeId == 0 ? "" : this.storeId+"";
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public UserBean updateSelf(UserBean userBean) {
        if (null == userBean) {
            return this;
        }
        if (-1 != userBean.id) {
            this.id = userBean.id;
        }
        if (null != userBean.createtime) {
            this.createtime = userBean.createtime;
        }
        if (-1 != userBean.gender) {
            this.gender = userBean.gender;
        }
        if (null != userBean.idNo) {
            this.idNo = userBean.idNo;
        }
        if (null != userBean.mobile) {
            this.mobile = userBean.mobile;
        }
        if (null != userBean.nickname) {
            this.nickname = userBean.nickname;
        }
        if (-1 != userBean.role) {
            this.role = userBean.role;
        }
        if (-1 != userBean.status) {
            this.status = userBean.status;
        }
        if (null != userBean.updatetime) {
            this.updatetime = userBean.updatetime;
        }
        if (null != userBean.token) {
            this.token = userBean.token;
        }
        if (null != userBean.username) {
            this.username = userBean.username;
        }
        if (-1 != userBean.userStatus) {
            this.userStatus = userBean.userStatus;
        }
        if (null != userBean.imusername) {
            this.imusername = userBean.imusername;
        }
        if (null != userBean.impassword) {
            this.impassword = userBean.impassword;
        }
        if (-1 != userBean.storeId) {
            this.storeId = userBean.storeId;
        }
        if (null != userBean.headImg) {
            this.headImg = userBean.headImg;
        }
        if (null != userBean.openId){
            this.openId = userBean.openId;
        }
        if (-1 != userBean.shutdown) {
            this.shutdown = userBean.shutdown;
        }
        return this;
    }
}
