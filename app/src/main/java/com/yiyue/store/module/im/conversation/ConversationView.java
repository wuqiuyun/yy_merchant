package com.yiyue.store.module.im.conversation;

import com.hyphenate.chat.EMConversation;
import com.yiyue.store.base.mvp.IBaseView;

import java.util.List;

/**
 * Created by zhangzz on 2018/10/19.
 */
public interface ConversationView extends IBaseView {
    void loadConversationSuccess(List<EMConversation> emConversationList);
    void loadConversationFail();
}
