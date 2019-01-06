package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/10/26.
 * 推荐用户说明
 */
public class ExplainBean {
    /**
     "frontline": 0,
     "invitestore": 0,
     "invitestylist": 0,
     "role": "string",
     "twoline": 0
     */

    private String frontline;
    private String invitestore;
    private String invitestylist;
    private String role;
    private String twoline;

    public String getFrontline() {
        return frontline;
    }

    public void setFrontline(String frontline) {
        this.frontline = frontline;
    }

    public String getInvitestore() {
        return invitestore;
    }

    public void setInvitestore(String invitestore) {
        this.invitestore = invitestore;
    }

    public String getInvitestylist() {
        return invitestylist;
    }

    public void setInvitestylist(String invitestylist) {
        this.invitestylist = invitestylist;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTwoline() {
        return twoline;
    }

    public void setTwoline(String twoline) {
        this.twoline = twoline;
    }
}
