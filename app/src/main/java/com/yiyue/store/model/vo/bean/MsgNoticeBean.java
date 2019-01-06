package com.yiyue.store.model.vo.bean;

/**
 * Created by zhangzz on 2018/9/29
 */
public class MsgNoticeBean {
    private String url;
    private String title;
    private String time;
    private String desc;
    private boolean isRead;

    public MsgNoticeBean(String url, String title, String time, String desc, boolean isRead) {
        this.url = url;
        this.title = title;
        this.time = time;
        this.desc = desc;
        this.isRead = isRead;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
