package com.yiyue.store.model.vo.bean;

import com.hyphenate.chat.EMMessage;
import com.yiyue.store.model.vo.bean.daobean.ChatNoFriendBean;

import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by zhangzz on 2018/9/18
 */
public class ChatAdapterHolderbean implements Serializable {
    public static final long serialVersionUID = 1L;
    private String icon="";
    private String content="";
    private String type="";
    EMMessage message;

    SelfDefinedInfoBean chatNoFriendBean;


    public ChatAdapterHolderbean() {
    }

    public ChatAdapterHolderbean(String icon, String content) {
        this.icon = icon;
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EMMessage getMessage() {
        return message;
    }

    public void setMessage(EMMessage message) {
        this.message = message;
    }

    public SelfDefinedInfoBean getChatNoFriendBean() {
        return chatNoFriendBean;
    }

    public void setChatNoFriendBean(SelfDefinedInfoBean chatNoFriendBean) {
        this.chatNoFriendBean = chatNoFriendBean;
    }
}
