package com.yiyue.store.module.im.conversation;

import android.util.Pair;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yiyue.store.base.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/10/19.
 */
public class ConversationPresenter extends BasePresenter<ConversationView> {

    /**
     * 加载环信保存再本地的消息会话记录
     *
     * @return
     */
    protected void loadConversationList() {
        Task.callInBackground(new Callable<List<EMConversation>>() {
            @Override
            public List<EMConversation> call() throws Exception {
                // 获取所有会话
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                List<EMConversation> list = null;
                if (conversations != null && conversations.size() > 0) {
                    List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
                    synchronized (conversations) {
                        for (EMConversation conversation : conversations.values()) {
                            if (conversation.getAllMessages().size() != 0) {
                                sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                            }
                        }
                    }
                    try {
                        sortConversationByLastChatTime(sortList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    list = new ArrayList<EMConversation>();
                    for (Pair<Long, EMConversation> sortItem : sortList) {
                        list.add(sortItem.second);
                    }
                }

                return list;
            }
        }).continueWith(new Continuation<List<EMConversation>, Object>() {
            @Override
            public Object then(Task<List<EMConversation>> task) throws Exception {
                if (task != null) {
                    List<EMConversation> emConversations = task.getResult();
                    if (emConversations != null && emConversations.size() > 0) {
                        getMvpView().loadConversationSuccess(emConversations);
                    } else {
                        getMvpView().loadConversationSuccess(emConversations);
                    }
                } else {
                    getMvpView().loadConversationFail();
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

}
