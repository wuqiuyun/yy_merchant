package com.yiyue.store.model.vo.bean;

/**
 * Created by zhangzz on 2018/12/10
 */
public class CheckMsgBean {
    private int status;//status -1 消息不存在 0未处理，1同意 2拒绝

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
