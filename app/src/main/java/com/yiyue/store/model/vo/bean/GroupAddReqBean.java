package com.yiyue.store.model.vo.bean;

/**
 * Created by zhangzz on 2018/10/22
 */
public class GroupAddReqBean {
    String imusername;
    String userId;

    public GroupAddReqBean(String imusername, String userId) {
        this.imusername = imusername;
        this.userId = userId;
    }

    public String getImusername() {
        return imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
