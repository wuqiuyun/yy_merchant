package com.yiyue.store.module.im.chat;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;

import java.util.List;

/**
 * Created by zhangzz on 2018/9/18
 */
public class ChatAdapter2 extends ChatBaseAdapter {

    public ChatAdapter2(List<ChatAdapterItemTypeBean> data) {
        super(data);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV, R.layout.chat_recv);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND, R.layout.chat_send);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_VOICE, R.layout.chat_recv_voice);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_VOICE, R.layout.chat_send_voice);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_PIC, R.layout.chat_send_pic);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_PIC, R.layout.chat_recv_pic);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_LOCATION, R.layout.chat_send_location);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_LOCATION, R.layout.chat_recv_location);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_VOICE_CALL, R.layout.chat_send_voice_call);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_VOICE_CALL, R.layout.chat_recv_voice_call);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_VIDEO_CALL, R.layout.chat_send_voice_call);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_VIDEO_CALL, R.layout.chat_recv_voice_call);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_ADD_MONEY, R.layout.chat_recv_add_money);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_ADD_MONEY, R.layout.chat_send_add_money);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_RED_PACKET, R.layout.chat_send_red_packet);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_RED_PACKET, R.layout.chat_recv_red_packet);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_SHARE_MSG, R.layout.chat_recv_share_msg);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_SHARE_MSG, R.layout.chat_send_share_msg);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_RED_OPEN, R.layout.chat_recv_red_open);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_RED_OPEN, R.layout.chat_send_red_open);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_TRANS_ACCOUNT, R.layout.chat_send_transfer_account);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_TRANS_ACCOUNT, R.layout.chat_recv_transfer_account);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_TRANS_OPEN, R.layout.chat_recv_red_open);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_TRANS_OPEN, R.layout.chat_send_red_open);
        addItemType(ChatAdapterItemTypeBean.CHAT_RECV_GROUP_JOIN, R.layout.chat_send_group_join);
        addItemType(ChatAdapterItemTypeBean.CHAT_SEND_GROUP_JOIN, R.layout.chat_send_group_join);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatAdapterItemTypeBean item) {
        switch (helper.getItemViewType()) {
            case ChatAdapterItemTypeBean.CHAT_RECV:
            case ChatAdapterItemTypeBean.CHAT_SEND:
                createNormChatView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_RECV_VOICE:
            case ChatAdapterItemTypeBean.CHAT_SEND_VOICE:
                createVoiceChatView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_PIC:
            case ChatAdapterItemTypeBean.CHAT_RECV_PIC:
                createPicChatView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_LOCATION:
            case ChatAdapterItemTypeBean.CHAT_RECV_LOCATION:
                createLocationChatView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_VOICE_CALL:
            case ChatAdapterItemTypeBean.CHAT_RECV_VOICE_CALL:
                createVoiceCallView(helper, item, true);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_VIDEO_CALL:
            case ChatAdapterItemTypeBean.CHAT_RECV_VIDEO_CALL:
                createVoiceCallView(helper, item, false);
                break;
            case ChatAdapterItemTypeBean.CHAT_RECV_ADD_MONEY:
            case ChatAdapterItemTypeBean.CHAT_SEND_ADD_MONEY:
                createAddMoneyView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_RED_PACKET:
            case ChatAdapterItemTypeBean.CHAT_RECV_RED_PACKET:
                createRedPacketView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_SHARE_MSG:
            case ChatAdapterItemTypeBean.CHAT_RECV_SHARE_MSG:
                createShareMsgView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_RED_OPEN:
            case ChatAdapterItemTypeBean.CHAT_RECV_RED_OPEN:
                createRedOpenView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_TRANS_ACCOUNT:
            case ChatAdapterItemTypeBean.CHAT_RECV_TRANS_ACCOUNT:
                createTransAccountView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_TRANS_OPEN:
            case ChatAdapterItemTypeBean.CHAT_RECV_TRANS_OPEN:
                createRedOpenView(helper, item);
                break;
            case ChatAdapterItemTypeBean.CHAT_SEND_GROUP_JOIN:
            case ChatAdapterItemTypeBean.CHAT_RECV_GROUP_JOIN:
                createGroupJoinView(helper, item);
                break;
        }
    }


}
