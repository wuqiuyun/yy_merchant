package com.yiyue.store.model.vo.bean.daobean;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by Lizhuo on 2018/10/16.
 */
@Entity
public class GroupBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long _id;
    private long id;
    private String imgroup; //im群组编号
    private long userId; //群主id
    private String name; //群名称
    private String describe; //群描述
    private int public_; //群组类型 0公开群 1私有群
    private int membersonly; //加群是否需要狗群主/狗管理审批 0是 1否
    private int allowinvites; //是否允许群成员邀请他人入群 0允许 1只允许群主
    private int maxusers; // 群成员上限
    private int affiliationscount; //现有群成员总数
    private long createtime;
    private long updatetime;
    private String imusername;//是群主的  在搜索群的时候用不到
    private String path;//群头像路径
    private int ismember;//1  属于此群成员  0 非本群组成员
    @Transient
    private long groupContactsId;  //群组关系id
    @Transient
    private int groupContactsStatus;  //是否接受群组消息 1为免打扰


    @Generated(hash = 1433655014)
    public GroupBean(Long _id, long id, String imgroup, long userId, String name,
                     String describe, int public_, int membersonly, int allowinvites,
                     int maxusers, int affiliationscount, long createtime, long updatetime,
                     String imusername, String path, int ismember) {
        this._id = _id;
        this.id = id;
        this.imgroup = imgroup;
        this.userId = userId;
        this.name = name;
        this.describe = describe;
        this.public_ = public_;
        this.membersonly = membersonly;
        this.allowinvites = allowinvites;
        this.maxusers = maxusers;
        this.affiliationscount = affiliationscount;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.imusername = imusername;
        this.path = path;
        this.ismember = ismember;
    }

    @Generated(hash = 405578774)
    public GroupBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImgroup() {
        return imgroup;
    }

    public void setImgroup(String imgroup) {
        this.imgroup = imgroup;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getPublic_() {
        return public_;
    }

    public void setPublic_(int public_) {
        this.public_ = public_;
    }

    public int getMembersonly() {
        return membersonly;
    }

    public void setMembersonly(int membersonly) {
        this.membersonly = membersonly;
    }

    public int getAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(int allowinvites) {
        this.allowinvites = allowinvites;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public int getAffiliationscount() {
        return affiliationscount;
    }

    public void setAffiliationscount(int affiliationscount) {
        this.affiliationscount = affiliationscount;
    }

    public String getImusername() {
        return imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    public String getPath() {
        return TextUtils.isEmpty(path) ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
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

    public int getIsmember() {
        return ismember;
    }

    public void setIsmember(int ismember) {
        this.ismember = ismember;
    }

    public long getGroupContactsId() {
        return groupContactsId;
    }

    public void setGroupContactsId(long groupContactsId) {
        this.groupContactsId = groupContactsId;
    }

    public int getGroupContactsStatus() {
        return groupContactsStatus;
    }

    public void setGroupContactsStatus(int groupContactsStatus) {
        this.groupContactsStatus = groupContactsStatus;
    }
}
