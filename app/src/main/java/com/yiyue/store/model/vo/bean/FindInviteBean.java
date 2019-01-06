package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/10/26.
 * 查询到的推荐人
 */
public class FindInviteBean {
    /**
     "createTime": "2018-10-26T02:06:34.849Z",
     "headImg": "string",
     "nextCount": 0,
     "nickName": "string",
     "role": 0, //1用户 2 美发师 3商户
     "type": 0,
     "userId": 0,
     "userName": "string"
     */
    
    private String createTime;
    private String headImg;
    private int nextCount;
    private String nickName;
    private int role;
    private int type;
    private long userId;
    private String userName;

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

    public int getNextCount() {
        return nextCount;
    }

    public void setNextCount(int nextCount) {
        this.nextCount = nextCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
