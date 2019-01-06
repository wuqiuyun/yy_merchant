package com.yiyue.store.module.im.groups.groupsearch;

/**
 * Created by Lizhuo on 2018/10/15.
 */
public class GroupSearchBean {
    private String groupName;
    private int memberNum;
    private String hot;
    private String avaterUrl;
    private String groupIntro;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getAvaterUrl() {
        return avaterUrl;
    }

    public void setAvaterUrl(String avaterUrl) {
        this.avaterUrl = avaterUrl;
    }

    public String getGroupIntro() {
        return groupIntro;
    }

    public void setGroupIntro(String groupIntro) {
        this.groupIntro = groupIntro;
    }

    public GroupSearchBean() {

    }

    public GroupSearchBean(String groupName, int memberNum, String hot, String avaterUrl, String groupIntro) {
        this.groupName = groupName;
        this.memberNum = memberNum;
        this.hot = hot;
        this.avaterUrl = avaterUrl;
        this.groupIntro = groupIntro;
    }
}
