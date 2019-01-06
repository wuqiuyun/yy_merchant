package com.yiyue.store.model.vo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by zhangzz on 2018/9/18
 */
public class ChatAdapterItemTypeBean implements Serializable, MultiItemEntity {

    public static final int CHAT_RECV = 1001;
    public static final int CHAT_SEND = 1002;
    public static final int CHAT_RECV_VOICE = 1003;
    public static final int CHAT_SEND_VOICE = 1004;
    public static final int CHAT_SEND_PIC = 1005;
    public static final int CHAT_RECV_PIC = 1006;
    public static final int CHAT_SEND_LOCATION = 1007;
    public static final int CHAT_RECV_LOCATION = 1008;
    public static final int CHAT_SEND_VOICE_CALL = 1009;
    public static final int CHAT_RECV_VOICE_CALL = 1010;

    public static final int CHAT_RECV_ADD_MONEY = 1011;//接收：服务类型加价，烫发 染发 加价20之类
    public static final int CHAT_SEND_ADD_MONEY = 1012;

    public static final int CHAT_RECV_SHARE_MSG = 1013;//接收：分享作品 分享美发师 分享门店之类
    public static final int CHAT_SEND_SHARE_MSG = 1014;

    public static final int CHAT_RECV_RED_PACKET = 1015;//接收：红包
    public static final int CHAT_SEND_RED_PACKET = 1016;

    public static final int CHAT_RECV_TRANS_ACCOUNT = 1019;//接收：转账
    public static final int CHAT_SEND_TRANS_ACCOUNT = 1020;

    public static final int CHAT_RECV_RED_OPEN = 1017;//对方已收取红包
    public static final int CHAT_SEND_RED_OPEN = 1018;//你已收取对方红包

    public static final int CHAT_RECV_TRANS_OPEN = 1021;//对方已收取转账
    public static final int CHAT_SEND_TRANS_OPEN = 1022;//你已收取对方转账

    public static final int CHAT_SEND_VIDEO_CALL = 1023;
    public static final int CHAT_RECV_VIDEO_CALL = 1024;//视频电话

    public static final int CHAT_SEND_GROUP_JOIN = 1025;
    public static final int CHAT_RECV_GROUP_JOIN = 1026;//群邀请加入消息提示

    public int type;
    public Object object;

    public ChatAdapterItemTypeBean(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
