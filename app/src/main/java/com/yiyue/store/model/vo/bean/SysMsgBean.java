package com.yiyue.store.model.vo.bean;

import java.io.Serializable;

/**
 * Created by zhangzz on 2018/10/16
 */
public class SysMsgBean implements Serializable{
    /**
     * id : 2.8395623662694313E28
     * requestUserId : 13729026553
     * friendId : 2
     * remarks : 请求添加好友
     * status : 0
     * createtime : 2018-10-12 15:37:23
     * updatetime : 2018-10-12 15:37:23
     */

    private String id;
    private long requestUserId; //申请人 的userId
    private String imusername;        //申请人im帐号
    private String requestNickname;   //申请人昵称
    private String requestPtah;       //申请人头像路径
    private long friendId;    //被申请好友id  加群  填写群主的userId
    private long groupId;     //被申请群组id
    private String imgroup;         //被申请群组编号
    private String remarks; //申请备注
    private Integer status; //状态  0 申请添加  1 同意申请 2 拒绝申请
    private Integer type;   //类型  1 好友申请  2 加群申请
    private long createtime;
    private long updatetime;
    //标签
    private String label;       //标签

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(long requestUserId) {
        this.requestUserId = requestUserId;
    }

    public String getImusername() {
        return imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    public String getRequestNickname() {
        return requestNickname;
    }

    public void setRequestNickname(String requestNickname) {
        this.requestNickname = requestNickname;
    }

    public String getRequestPtah() {
        return requestPtah;
    }

    public void setRequestPtah(String requestPtah) {
        this.requestPtah = requestPtah;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getImgroup() {
        return imgroup;
    }

    public void setImgroup(String imgroup) {
        this.imgroup = imgroup;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
