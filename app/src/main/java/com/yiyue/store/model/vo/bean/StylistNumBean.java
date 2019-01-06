package com.yiyue.store.model.vo.bean;

/*
 * Create by lvlong on  2018/11/13
 */

public class StylistNumBean {

    private int enter;  //平台美发师
    private int sign;   //店内美发师

    public StylistNumBean(){

    }

    public StylistNumBean(int enter, int sign) {
        this.enter = enter;
        this.sign = sign;
    }

    public int getEnter() {
        return enter;
    }

    public void setEnter(int enter) {
        this.enter = enter;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
