package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/11/19.
 * 推送透传消息
 */
public class AliMessageBean {
    private int code;//自定义消息类型
    private String data;//消息内容

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
