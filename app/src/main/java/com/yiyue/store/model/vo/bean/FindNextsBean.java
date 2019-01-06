package com.yiyue.store.model.vo.bean;

import java.util.List;

/**
 * Created by Lizhuo on 2018/10/29.
 */
public class FindNextsBean {

    private int inviteCount;//邀请数
    private List<Invites> invites;//被邀请人list

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public List<Invites> getInvites() {
        return invites;
    }

    public void setInvites(List<Invites> invites) {
        this.invites = invites;
    }

    //被邀请人
    public static class Invites {
        private int type;
        private int role;
        private int nextCount;
        private long userId;
        private String userName;
        private String nickName;
        private String createTime;
        private String headImg;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getNextCount() {
            return nextCount;
        }

        public void setNextCount(int nextCount) {
            this.nextCount = nextCount;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }
    }
         
}
