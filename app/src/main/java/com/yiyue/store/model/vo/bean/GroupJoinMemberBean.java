package com.yiyue.store.model.vo.bean;

import android.text.TextUtils;

/**
 * Created by zhangzz on 2018/12/19
 */
public class GroupJoinMemberBean {
    private String nickName;
    private String imusername;

    public GroupJoinMemberBean(String nickName, String imusername) {
        this.nickName = nickName;
        this.imusername = imusername;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImusername() {
        return imusername;
    }

    public void setImusername(String imusername) {
        this.imusername = imusername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupJoinMemberBean groupJoinMemberBean = (GroupJoinMemberBean) o;
        if (!isEqual(this.nickName, groupJoinMemberBean.getNickName()))
            return false;
        if (!isEqual(this.imusername, groupJoinMemberBean.getImusername()))
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
