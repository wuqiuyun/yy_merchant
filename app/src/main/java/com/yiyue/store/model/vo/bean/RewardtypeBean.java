package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/10/26.
 * 当前用户奖励类型
 */
public class RewardtypeBean {
    /**
     "invitecode": null,
     "userId": 438,
     "rewardtype": 1,
     "changetime": "2018-10-26 14:35:23",
     "rewardTip": "选择相应奖励类型后，即可获得相应推荐奖励，确定后30天可再次更改",
     "changDay": 30
     */
    
    private String invitecode;
    private long userId;
    private int rewardtype;
    private String changetime;
    private String rewardTip;
    private int changDay;

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRewardtype() {
        return rewardtype;
    }

    public void setRewardtype(int rewardtype) {
        this.rewardtype = rewardtype;
    }

    public String getChangetime() {
        return changetime;
    }

    public void setChangetime(String changetime) {
        this.changetime = changetime;
    }

    public String getRewardTip() {
        return rewardTip;
    }

    public void setRewardTip(String rewardTip) {
        this.rewardTip = rewardTip;
    }

    public int getChangDay() {
        return changDay;
    }

    public void setChangDay(int changDay) {
        this.changDay = changDay;
    }
}
