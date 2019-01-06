package com.yiyue.store.util.easyutils;

import com.google.gson.Gson;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.daobean.ChatNoFriendBean;
import com.yiyue.store.util.easyutils.emlisenter.MyCallBackImpl;
import com.yiyue.store.util.easyutils.emlisenter.MyEMMessageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/14
 * 环信各种api接口实现代理类 方便不同工具库的封装
 */
public class EMInterfaceImpl implements EMInterface {
    public static EMInterfaceImpl instance;

    public static void initEMInstance() {
        if (instance == null) {
            synchronized (EMInterfaceImpl.class) {
                if (instance == null) {
                    instance = new EMInterfaceImpl();
                    EasyUtil.EasyManagerSet.setUmManager(instance);
                }
            }
        }
    }

    /**
     * 注册用户名会自动转为小写字母，所以建议用户名均以小写注册
     *
     * @param username
     * @param pwd
     */
    @Override
    public void createAccount(String username, String pwd) throws HyphenateException {
        EMClient.getInstance().createAccount(username, pwd);//同步方法
    }

    /**
     * 登录方法
     *
     * @param userName
     * @param password
     * @param callback
     */
    @Override
    public void login(String userName, String password, MyCallBackImpl callback) {
        EMClient.getInstance().login(userName, password, callback);
    }

    /**
     * 获取好友列表
     *
     * @return
     * @throws HyphenateException
     */
    @Override
    public List<String> getAllContactsFromServer() throws HyphenateException {
        List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
        return usernames;
    }

    /**
     * 查找好友
     *
     * @return
     * @throws HyphenateException 参数为要添加的好友的username和添加理由
     */
    @Override
    public void addContact(String toAddUsername, String reason) throws HyphenateException {
        EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
    }

    /**
     * 同意好友请求
     *
     * @param username
     */
    @Override
    public void acceptInvitation(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().acceptInvitation(username);
    }

    /**
     * 拒绝好友请求
     *
     * @param username
     */
    @Override
    public void declineInvitation(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().declineInvitation(username);
    }

    /**
     * 通过注册消息监听来接收消息。
     *
     * @param myEMMessageListener
     */
    @Override
    public void addMessageListener(MyEMMessageListener myEMMessageListener) {
        EMClient.getInstance().chatManager().addMessageListener(myEMMessageListener);
    }

    /**
     * 在不需要的时候移除listener，如在activity的onDestroy()时
     *
     * @param myEMMessageListener
     */
    @Override
    public void removeMessageListener(MyEMMessageListener myEMMessageListener) {
        EMClient.getInstance().chatManager().removeMessageListener(myEMMessageListener);
    }

    /**
     * 初始化会话对象，这里有三个参数么，
     * 第一个表示会话的当前聊天的 useranme 或者 groupid
     * 第二个是会话类型可以为空
     * 第三个表示如果会话不存在是否创建
     */
    @Override
    public EMConversation getConversation(String username, EMConversation.EMConversationType type, boolean createIfNotExists) {

        return EMClient.getInstance().chatManager().getConversation(username, null, createIfNotExists);
    }

    /**
     * //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id
     *
     * @param content
     * @param toChatUsername
     * @return
     */
    @Override
    public EMMessage createTxtSendMessage(String content, String toChatUsername) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        return message;
    }

    @Override
    public EMMessage createVoiceSendMessage(String filePath, int timeLength, String username) {
        return EMMessage.createVoiceSendMessage(filePath, timeLength, username);
    }

    @Override
    public EMMessage createLocationSendMessage(double latitude, double longitude, String locationAddress, String username) {
        return EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, username);
    }

    @Override
    public EMMessage createImageSendMessage(String filePath, boolean sendOriginalImage, String username) {
        return EMMessage.createImageSendMessage(filePath, false, username);
    }

    @Override
    public EMMessage createFileSendMessage(String filePath, String username) {
        return EMMessage.createFileSendMessage(filePath, username);
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMessage(EMMessage message) {
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 创建群组
     *
     * @param groupName  群组名称
     * @param desc       群组简介
     * @param allMembers 群组初始成员，如果只有自己传空数组即可
     * @param reason     邀请成员加入的reason
     * @param option     群组类型选项，可以设置群组最大用户数(默认200)及群组类型
     *                   option.inviteNeedConfirm表示邀请对方进群是否需要对方同意，默认是需要用户同意才能加群的。
     *                   option.extField创建群时可以为群组设定扩展字段，方便个性化订制。
     * @return 创建好的group
     * @throws HyphenateException
     */
    @Override
    public void createGroup(String groupName, String desc, String[] allMembers, String reason, EMGroupOptions option) throws HyphenateException {
        EMClient.getInstance().groupManager().createGroup(groupName, desc, allMembers, reason, option);
    }

    /**
     * 从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
     * 需异步处理;
     *
     * @return
     */
    @Override
    public List<EMGroup> getJoinedGroupsFromServer() throws HyphenateException {
        return EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
    }

    /**
     * 从本地加载群组列表
     *
     * @return
     */
    @Override
    public List<EMGroup> getAllGroups() {
        return EMClient.getInstance().groupManager().getAllGroups();
    }

    /**
     * 根据群组ID从本地获取群组基本信息
     *
     * @param groupId
     */
    @Override
    public EMGroup getGroup(String groupId) {
        return EMClient.getInstance().groupManager().getGroup(groupId);
    }

    /**
     * 根据群组ID从服务器获取群组基本信息
     * <p>
     * 获取单个群组信息。getGroupFromServer(groupId)返回结果包含群组名称，群描述，群主，管理员列表，不包含群成员。
     * <p>
     * v getGroupFromServer(String groupId, boolean fetchMembers)，如果fetchMembers为true，取群组信息的时候也会获取群成员，最大数200人。
     * <p>
     * group.getOwner();//获取群主
     * List<String> members = group.getMembers();//获取内存中的群成员
     * List<String> adminList = group.getAdminList();//获取管理员列表
     *
     * @param groupId
     * @param fetchMembers
     */
    @Override
    public EMGroup getGroupFromServer(String groupId, boolean fetchMembers) throws HyphenateException {
        return EMClient.getInstance().groupManager().getGroupFromServer(groupId, fetchMembers);
    }

    @Override
    public void addUsersToGroup(String groupId, String[] newmembers) {

    }

    /**
     * 获取完整的群成员列表,如果群成员较多，需要多次从服务器获取完成
     *
     * @param groupId
     * @param cursor
     * @param pageSize
     * @return
     * @throws HyphenateException
     */
    @Override
    public EMCursorResult<String> fetchGroupMembers(String groupId, String cursor, int pageSize) throws HyphenateException {
        return EMClient.getInstance().groupManager().fetchGroupMembers(groupId, cursor, pageSize);
    }

    /**
     * 获取全部群成员列表 这里把获取列表处理了
     *
     * @param groupId
     * @return
     */
    @Override
    public List<String> getGroupMembers(String groupId) throws HyphenateException {
        List<String> memberList = new ArrayList<>();
        EMCursorResult<String> result = null;
        do {
            // page size set to 20 is convenient for testing, should be applied to big value
            result = EasyUtil.getEmManager().fetchGroupMembers(groupId, result != null ? result.getCursor() : "", 20);
            if (result != null) {
                memberList.addAll(result.getData());
            }
        } while (result != null && result.getCursor() != null && !result.getCursor().isEmpty());
        return memberList;
    }

    /**
     * 如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
     * 需异步处理
     *
     * @param groupid
     */
    @Override
    public void joinGroup(String groupid) throws HyphenateException {
        EMClient.getInstance().groupManager().joinGroup(groupid);
    }

    /**
     * 需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法
     * 需异步处理
     *
     * @param groupid
     * @param joinReason
     */
    @Override
    public void applyJoinToGroup(String groupid, String joinReason) throws HyphenateException {
        EMClient.getInstance().groupManager().applyJoinToGroup(groupid, joinReason);
    }

    @Override
    public EMCursorResult<EMGroupInfo> getPublicGroupsFromServer(int pagesize, String cursor) throws HyphenateException {
        EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(pagesize, cursor);
        return result;
    }

    /**
     * 调用sdk的退出登录方法
     *
     * @param unbindToken 表示是否解绑推送的token 没有使用推送或者被踢都要传false
     * @param callback
     */
    @Override
    public void logout(boolean unbindToken, MyCallBackImpl callback) {
        EMClient.getInstance().logout(false, callback);
    }

    /**
     * 判断环信sdk是否登录成功过，并没有退出和被踢
     *
     * @return
     */
    @Override
    public boolean isLoggedInBefore() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    @Override
    public String getCurrentUser() {
        return EMClient.getInstance().getCurrentUser();
    }

    @Override
    public void  deleteConversation(String imId){
        //删除和某个user的整个的聊天记录（包括本地）
        EMClient.getInstance().chatManager().deleteConversation(imId, true);
    }


    /**
     * 默认单聊
     * 服务加价消息发送方法调用
     *
     * @param chatNoFriendBean
     * @param mChatId 发送给谁 就是谁的聊天imId
     */
    @Override
    public EMMessage sendOrderAddMoneyMes(SelfDefinedInfoBean chatNoFriendBean, String mChatId) {
        EMMessage message = EasyUtil.getEmManager().createTxtSendMessage(chatNoFriendBean.getContent(), mChatId);
        message.setChatType(EMMessage.ChatType.Chat);
        message.setAttribute(Constants.IM_MESSAGE_ORDER_ADDMONEY_KEY, new Gson().toJson(chatNoFriendBean));//染发 加价 ！之类！自定义消息类型  用这个key传值/ios端也能识别
        // 调用发送消息的方法
        sendMessage(message);
        return message;
    }
}
