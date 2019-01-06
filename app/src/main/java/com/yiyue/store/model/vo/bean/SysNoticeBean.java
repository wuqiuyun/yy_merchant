package com.yiyue.store.model.vo.bean;

/**
 * Created by zhangzz on 2018/9/29
 */
public class SysNoticeBean {
    private String title;
    private String time;
    private String desc;

    public SysNoticeBean(String title, String time, String desc) {
        this.title = title;
        this.time = time;
        this.desc = desc;
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
}
