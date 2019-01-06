package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/10/26.
 * 推荐码
 */
public class ReCodeBean {
    private String invitecode;
    private String tip;
    private long userId;
    /**
     * userId : 360
     * shareImg : https://yiyuestore.oss-cn-shenzhen.aliyuncs.com/2018-11-09/83639224cfd0425c9132a5077537c9ca-files
     * shareTitle : 这是我的意约专属推荐码：OMGGOW
     * shareType : 5
     */

    private String shareImg;
    private String shareTitle;
    private int shareType;


    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }
}
