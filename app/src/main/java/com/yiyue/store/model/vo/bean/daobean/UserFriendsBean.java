package com.yiyue.store.model.vo.bean.daobean;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangzz on 2018/10/17
 */

@Entity
public class UserFriendsBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id : 8
     * createtime : 2018-10-11T07:41:09.000+0000
     * friendId : 100003
     * nickname : null
     * status : 0
     * updatetime : 2018-10-11T08:07:47.000+0000
     * userId : 100001
     * imusername : pktest002
     * path : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539422758542&di=bf27be6d5644423f50c97bc338ce0dcd&imgtype=0&src=http%3A%2F%2Fwww.goumin.com%2Fattachments%2Fphoto%2F0%2F0%2F69%2F17866%2F4573921.jpg
     * remarks : 例子
     * gender : null
     * labels : [{"userId":100003,"title":"可爱"},{"userId":100003,"title":"大方"}]
     */
    @Id(autoincrement = true)
    private Long _id;
    private long id;
    private long createtime;
    private long friendId;
    private String nickname;//昵称
    private int status;//0正常 1禁言 屏蔽好友与否用
    private String updatetime;
    private long userId;
    private String imusername;
    private String path;//头像路径
    private String remarks;//备注
    private int gender;//性别

    //新添加字段
    private long birthday;   //生日 20181017
    private String job;         //职业
    private String facetype;    //脸型
    private String hairlength;  //f发长
    private String hobby;       //爱好
    //标签
    private String label;       //标签
    @Transient
    private String mobile;
    @Transient
    private String index;//首字母索引
    @Transient
    private boolean isSelect;
    @Transient
    private String idno;//用户编号
    @Transient
    private int faceture;
    @Transient
    private int hairstyle;

    @Transient
    private boolean isEnable;//群成员邀请列表判断是否是本群成员  是的话就不可点击邀请加入
    @Transient
    private int role;//1 普通用户  2 美发师 3 门店

    @Generated(hash = 584647297)
    public UserFriendsBean(Long _id, long id, long createtime, long friendId, String nickname, int status, String updatetime, long userId, String imusername, String path, String remarks, int gender, long birthday, String job,
            String facetype, String hairlength, String hobby, String label) {
        this._id = _id;
        this.id = id;
        this.createtime = createtime;
        this.friendId = friendId;
        this.nickname = nickname;
        this.status = status;
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

    @Generated(hash = 1374967437)
    public UserFriendsBean() {
    }


    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getImusername() {
        return imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return job;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFacetype() {
        return facetype;
    }

    public void setFacetype(String facetype) {
        this.facetype = facetype;
    }

    public String getHairlength() {
        return hairlength;
    }

    public void setHairlength(String hairlength) {
        this.hairlength = hairlength;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public int getFaceture() {
        return faceture;
    }

    public void setFaceture(int faceture) {
        this.faceture = faceture;
    }

    public int getHairstyle() {
        return hairstyle;
    }

    public void setHairstyle(int hairstyle) {
        this.hairstyle = hairstyle;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserFriendsBean other = (UserFriendsBean) obj;
        if (!isEqual(this.nickname, other.getNickname()))
            return false;
        if (!isEqual(this.remarks, other.getRemarks()))
            return false;
        if (this.getBirthday() != other.getBirthday())
            return false;
        if (this.getGender() != other.getGender())
            return false;
        if (this.status != other.getStatus())
            return false;
        if (!isEqual(this.hairlength, other.getHairlength()))
            return false;
        if (!isEqual(this.hobby, other.getHobby()))
            return false;
        if (!isEqual(this.job, other.getJob()))
            return false;
        if (!isEqual(this.path, other.getPath()))
            return false;
        if (!isEqual(this.label, other.getLabel()))
            return false;
        if (this.getStatus() != other.getStatus())
            return false;
        return true;
    }

    private boolean isEqual(String one, String two) {
        if ((TextUtils.isEmpty(one) && TextUtils.isEmpty(two))
                || (!TextUtils.isEmpty(one) && one.equals(two))) {
            return true;
        } else {
            return false;
        }
    }
}
