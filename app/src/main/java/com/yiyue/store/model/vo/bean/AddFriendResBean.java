package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by zhangzz on 2018/10/16
 */
public class AddFriendResBean implements Serializable {
    /**
     * id : 2.840171702809023E28
     * requestUserId : 0
     * friendId : 0
     * remarks : null
     * status : 0
     * createtime : 2018-10-16 11:22:45
     * updatetime : 2018-10-16 11:22:45
     */
    private String id;
    private int requestUserId;
    private int friendId;
    private Object remarks;
    private int status;
    private String createtime;
    private String updatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(int requestUserId) {
        this.requestUserId = requestUserId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
