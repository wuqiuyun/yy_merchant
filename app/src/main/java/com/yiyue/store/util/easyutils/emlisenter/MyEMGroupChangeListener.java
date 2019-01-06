package com.yiyue.store.util.easyutils.emlisenter;

import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMMucSharedFile;

import java.util.List;

/**
 * Created by zhangzz on 2018/9/15
 */
public class MyEMGroupChangeListener implements EMGroupChangeListener {
    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
        //接收到群组加入邀请
    }

    @Override
    public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
        //用户申请加入群
    }

    @Override
    public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
        //加群申请被同意
    }

    @Override
    public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
        //加群申请被拒绝
    }

    @Override
    public void onInvitationAccepted(String groupId, String inviter, String reason) {
        //群组邀请被同意
    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {
        //群组邀请被拒绝
    }

    @Override
    public void onUserRemoved(String groupId, String groupName) {

    }

    @Override
    public void onGroupDestroyed(String groupId, String groupName) {

    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
        //接收邀请时自动加入到群组的通知
    }

    @Override
    public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
        //成员禁言的通知
    }

    @Override
    public void onMuteListRemoved(String groupId, final List<String> mutes) {
        //成员从禁言列表里移除通知
    }

    @Override
    public void onAdminAdded(String groupId, String administrator) {
        //增加管理员的通知
    }

    @Override
    public void onAdminRemoved(String groupId, String administrator) {
        //管理员移除的通知
    }

    @Override
    public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
        //群所有者变动通知
    }

    @Override
    public void onMemberJoined(final String groupId, final String member) {
        //群组加入新成员通知
    }

    @Override
    public void onMemberExited(final String groupId, final String member) {
        //群成员退出通知
    }

    @Override
    public void onAnnouncementChanged(String groupId, String announcement) {
        //群公告变动通知
    }

    @Override
    public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
        //增加共享文件的通知
    }

    @Override
    public void onSharedFileDeleted(String groupId, String fileId) {
        //群共享文件删除通知
    }
}
