package com.yiyue.store.model.vo.bean.daobean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by zhangzz on 2018/10/29
 * 订单详情页等页面跳到聊天时 需要传递的jsonbean
 */
@Entity
public class ChatNoFriendBean implements Parcelable {

    @Id(autoincrement = true)
    private Long _id;

    /**
     * 以下为自定义消息必传字段
     */
    // 发送方信息
    private long userId;//用户id
    private String nickname;//昵称
    private String imusername;
    private String path;//头像路径
    private String label; //标签
    private int gender;//性别 1男  2女 3人妖


    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeLong(this.userId);
        dest.writeString(this.nickname);
        dest.writeString(this.imusername);
        dest.writeString(this.path);
        dest.writeString(this.label);
        dest.writeInt(this.gender);
    }

    public ChatNoFriendBean() {
    }

    protected ChatNoFriendBean(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.userId = in.readLong();
        this.nickname = in.readString();
        this.imusername = in.readString();
        this.path = in.readString();
        this.label = in.readString();
        this.gender = in.readInt();
    }

    @Generated(hash = 1400037260)
    public ChatNoFriendBean(Long _id, long userId, String nickname, String imusername,
            String path, String label, int gender) {
        this._id = _id;
        this.userId = userId;
        this.nickname = nickname;
        this.imusername = imusername;
        this.path = path;
        this.label = label;
        this.gender = gender;
    }

    public static final Creator<ChatNoFriendBean> CREATOR = new Creator<ChatNoFriendBean>() {
        @Override
        public ChatNoFriendBean createFromParcel(Parcel source) {
            return new ChatNoFriendBean(source);
        }

        @Override
        public ChatNoFriendBean[] newArray(int size) {
            return new ChatNoFriendBean[size];
        }
    };
}
