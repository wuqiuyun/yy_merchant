package com.yiyue.store.model.vo.bean;

import android.text.TextUtils;

import com.hyphenate.chat.EMMessage;

import java.io.Serializable;

/**
 * Created by zhangzz on 2018/10/18
 */
public class ConversationBean implements Serializable {
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
    private int type;//聊天类型 0 正常文字 1 图片 2位置 3语音 4语音通话
    private String content;
    private int unreadNum;//未读数量
    private EMMessage.ChatType chatType;
    private boolean isFriend;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }

    public EMMessage.ChatType getChatType() {
        return chatType;
    }

    public void setChatType(EMMessage.ChatType chatType) {
        this.chatType = chatType;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConversationBean other = (ConversationBean) obj;
        if (!isEqual(this.nickname, other.getNickname()))
            return false;
        if (!isEqual(this.remarks, other.getRemarks()))
            return false;
        if (this.getId() != other.getId())
            return false;
        if (!isEqual(this.imusername, other.getImusername()))
            return false;
        if (this.friendId != other.getFriendId())
            return false;
        if (this.userId != other.getUserId())
            return false;
        if (!isEqual(this.content, other.getContent()))
            return false;
        if (!isEqual(this.path, other.getPath()))
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
