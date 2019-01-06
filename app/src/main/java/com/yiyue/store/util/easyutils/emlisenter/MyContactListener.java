package com.yiyue.store.util.easyutils.emlisenter;

import com.hyphenate.EMContactListener;

/**
 * Created by zhangzz on 2018/9/15
 * <p>
 * 监听好友状态事件
 */
public class MyContactListener implements EMContactListener {
    @Override
    public void onContactAdded(String username) {
        //增加了联系人时回调此方法
    }

    @Override
    public void onContactDeleted(String username) {
        //被删除时回调此方法
    }

    @Override
    public void onContactInvited(String username, String reason) {
        //收到好友邀请
    }

    @Override
    public void onFriendRequestAccepted(String username) {
        //好友请求被同意
    }

    @Override
    public void onFriendRequestDeclined(String username) {
        //好友请求被拒绝
    }
}