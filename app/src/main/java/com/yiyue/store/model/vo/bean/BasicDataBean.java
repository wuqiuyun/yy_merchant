package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by zm on 2018/11/13.
 */
public class BasicDataBean implements Serializable{

    private String registerReward;//注册现金
    private String inviteReward;//注册积分
    private String sysCoinDefault;//后续页面单位

    public String getRegisterReward() {
        return registerReward;
    }

    public void setRegisterReward(String registerReward) {
        this.registerReward = registerReward;
    }

    public String getInviteReward() {
        return inviteReward;
    }

    public void setInviteReward(String inviteReward) {
        this.inviteReward = inviteReward;
    }

    public String getSysCoinDefault() {
        return sysCoinDefault;
    }

    public void setSysCoinDefault(String sysCoinDefault) {
        this.sysCoinDefault = sysCoinDefault;
    }
}
