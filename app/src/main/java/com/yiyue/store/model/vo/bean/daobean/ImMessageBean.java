package com.yiyue.store.model.vo.bean.daobean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Lizhuo on 2018/11/13.
 * 系统通知——互动消息
 */
@Entity
public class ImMessageBean {

    @Id(autoincrement = true)
    private Long _id;
    
    private String id;                //同意/拒绝申请的时候 要用这个id
    private long requestUserId;       //申请人 的userId
    private String imusername;        //申请人im帐号
    private String requestNickname;   //申请人昵称
    private String requestPtah;       //申请人头像路径

    private long friendId;            //被申请好友id  加群  填写群主的userId
    private long groupId;             //被申请群组id
    private String imgroup;           //被申请群组编号
    private String friendNickname;    //被申请人昵称 或群组昵称
    private String friendPtah;        //被申请人头像 或群组头像

    private String remarks;           //申请备注
    private Integer status;           //状态  0 申请添加  1 同意申请 2 拒绝申请
    private Integer type;             //类型  1 好友申请  2 加群申请 3.邀请加群

    private long createtime;

    private long updatetime;

    private String label;             //标签

    @Generated(hash = 550960223)
    public ImMessageBean(Long _id, String id, long requestUserId, String imusername,
            String requestNickname, String requestPtah, long friendId, long groupId,
            String imgroup, String friendNickname, String friendPtah,
            String remarks, Integer status, Integer type, long createtime,
            long updatetime, String label) {
        this._id = _id;
        this.id = id;
        this.requestUserId = requestUserId;
        this.imusername = imusername;
        this.requestNickname = requestNickname;
        this.requestPtah = requestPtah;
        this.friendId = friendId;
        this.groupId = groupId;
        this.imgroup = imgroup;
        this.friendNickname = friendNickname;
        this.friendPtah = friendPtah;
        this.remarks = remarks;
        this.status = status;
        this.type = type;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.label = label;
    }

    @Generated(hash = 1758850543)
    public ImMessageBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getRequestUserId() {
        return this.requestUserId;
    }

    public void setRequestUserId(long requestUserId) {
        this.requestUserId = requestUserId;
    }

    public String getImusername() {
        return this.imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    public String getRequestNickname() {
        return this.requestNickname;
    }

    public void setRequestNickname(String requestNickname) {
        this.requestNickname = requestNickname;
    }

    public String getRequestPtah() {
        return this.requestPtah;
    }

    public void setRequestPtah(String requestPtah) {
        this.requestPtah = requestPtah;
    }

    public long getFriendId() {
        return this.friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getImgroup() {
        return this.imgroup;
    }

    public void setImgroup(String imgroup) {
        this.imgroup = imgroup;
    }

    public String getFriendNickname() {
        return this.friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }

    public String getFriendPtah() {
        return this.friendPtah;
    }

    public void setFriendPtah(String friendPtah) {
        this.friendPtah = friendPtah;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public long getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getUpdatetime() {
        return this.updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
