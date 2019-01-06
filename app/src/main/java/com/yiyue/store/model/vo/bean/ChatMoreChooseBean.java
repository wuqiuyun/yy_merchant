package com.yiyue.store.model.vo.bean;

/**
 * Created by zhangzz on 2018/10/10
 */
public class ChatMoreChooseBean {
    private int resouseId;
    private int nameId;

    public ChatMoreChooseBean(int resouseId, int nameId) {
        this.resouseId = resouseId;
        this.nameId = nameId;
    }

    public int getResouseId() {
        return resouseId;
    }

    public void setResouseId(int resouseId) {
        this.resouseId = resouseId;
    }

    public int getName() {
        return nameId;
    }

    public void setName(int name) {
        this.nameId = name;
    }
}
