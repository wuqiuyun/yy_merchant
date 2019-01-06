package com.yiyue.store.model.vo.bean.daobean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 群成员
 * Created by Lizhuo on 2018/10/17.
 */
@Entity
public class GroupUserBean implements Parcelable
{
    private static final long serialVersionUID = 1L;
    /**
     * {
     "id": 114,
     "createtime": 1539745413000,
     "friendId": null,
     "nickname": "18664964529",
     "status": 0,
     "updatetime": 1539745413000,
     "userId": 436,
     "imusername": "im_18664964529",
     "path": null,
     "remarks": null,
     "gender": 2,
     "birthday": null,
     "job": null,
     "facetype": null,
     "hairlength": null,
     "hobby": null,
     "label": null
     }
     */
    @Id(autoincrement = true)
    private Long _id;
    private long id;
    private long createtime;
    private long friendId; 
    private String nickname;
    private int status;//状态 0 正常 -1 禁止
    private int role;//角色 1：普通群员 2 狗管理 3 狗群主
    private long updatetime;
    private long userId;
    private String imusername;
    private String path;
    private String remarks;
    private int gender;
    private int birthday;
    private String job;
    private String facetype;
    private String hairlength;
    private String hobby;
    private String label;
    @Transient
    private String index;//索引
    @Transient
    private boolean isAdd;//是否允许点击后进入邀请群员界面

    @Generated(hash = 1786753879)
    public GroupUserBean(Long _id, long id, long createtime, long friendId,
            String nickname, int status, int role, long updatetime, long userId,
            String imusername, String path, String remarks, int gender,
            int birthday, String job, String facetype, String hairlength,
            String hobby, String label) {
        this._id = _id;
        this.id = id;
        this.createtime = createtime;
        this.friendId = friendId;
        this.nickname = nickname;
        this.status = status;
        this.role = role;
        this.updatetime = updatetime;
        this.userId = userId;
        this.imusername = imusername;
        this.path = path;
        this.remarks = remarks;
        this.gender = gender;
        this.birthday = birthday;
        this.job = job;
        this.facetype = facetype;
        this.hairlength = hairlength;
        this.hobby = hobby;
        this.label = label;
    }

    @Generated(hash = 1019575134)
    public GroupUserBean() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(long createtime)
    {
        this.createtime = createtime;
    }

    public long getFriendId()
    {
        return friendId;
    }

    public void setFriendId(long friendId)
    {
        this.friendId = friendId;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public long getUpdatetime()
    {
        return updatetime;
    }

    public void setUpdatetime(long updatetime)
    {
        this.updatetime = updatetime;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getImusername()
    {
        return imusername;
    }

    public void setImusername(String imusername)
    {
        this.imusername = imusername;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public int getGender()
    {
        return gender;
    }

    public void setGender(int gender)
    {
        this.gender = gender;
    }

    public int getBirthday()
    {
        return birthday;
    }

    public void setBirthday(int birthday)
    {
        this.birthday = birthday;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public String getFacetype()
    {
        return facetype;
    }

    public void setFacetype(String facetype)
    {
        this.facetype = facetype;
    }

    public String getHairlength()
    {
        return hairlength;
    }

    public void setHairlength(String hairlength)
    {
        this.hairlength = hairlength;
    }

    public String getHobby()
    {
        return hobby;
    }

    public void setHobby(String hobby)
    {
        this.hobby = hobby;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public int getRole()
    {
        return role;
    }

    public void setRole(int role)
    {
        this.role = role;
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public boolean isAdd()
    {
        return isAdd;
    }

    public void setAdd(boolean add)
    {
        isAdd = add;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeLong(this.id);
        dest.writeLong(this.createtime);
        dest.writeLong(this.friendId);
        dest.writeString(this.nickname);
        dest.writeInt(this.status);
        dest.writeInt(this.role);
        dest.writeLong(this.updatetime);
        dest.writeLong(this.userId);
        dest.writeString(this.imusername);
        dest.writeString(this.path);
        dest.writeString(this.remarks);
        dest.writeInt(this.gender);
        dest.writeInt(this.birthday);
        dest.writeString(this.job);
        dest.writeString(this.facetype);
        dest.writeString(this.hairlength);
        dest.writeString(this.hobby);
        dest.writeString(this.label);
        dest.writeString(this.index);
        dest.writeByte(this.isAdd ? (byte) 1 : (byte) 0);
    }

    protected GroupUserBean(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readLong();
        this.createtime = in.readLong();
        this.friendId = in.readLong();
        this.nickname = in.readString();
        this.status = in.readInt();
        this.role = in.readInt();
        this.updatetime = in.readLong();
        this.userId = in.readLong();
        this.imusername = in.readString();
        this.path = in.readString();
        this.remarks = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readInt();
        this.job = in.readString();
        this.facetype = in.readString();
        this.hairlength = in.readString();
        this.hobby = in.readString();
        this.label = in.readString();
        this.index = in.readString();
        this.isAdd = in.readByte() != 0;
    }

    public static final Creator<GroupUserBean> CREATOR = new Creator<GroupUserBean>() {
        @Override
        public GroupUserBean createFromParcel(Parcel source) {
            return new GroupUserBean(source);
        }

        @Override
        public GroupUserBean[] newArray(int size) {
            return new GroupUserBean[size];
        }
    };
}
