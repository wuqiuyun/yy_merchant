package com.yiyue.store.model.vo.bean;

/**
 * Created by lvlong on 2018/12/6.
 */
public class StoreSuccessOrdersBean {

    /**
     * money : 0
     * num : 15
     * name : 深圳市民治地铁口
     * headImg : https://yiyuestore.oss-cn-shenzhen.aliyuncs.com/2018-12-06/01b21a98f08e41e5bd6965b4d4140c51-files
     */

    private String money;
    private int num;
    private String name;
    private String headImg;
    private String nexus;
    private String stylistId;

    public String getStylistId() {
        return stylistId;
    }

    public void setStylistId(String stylistId) {
        this.stylistId = stylistId;
    }

    public String getNexus() {
        return nexus;
    }

    public void setNexus(String nexus) {
        this.nexus = nexus;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Override
    public String toString() {
        return "StoreSuccessOrdersBean{" +
                "money='" + money + '\'' +
                ", num=" + num +
                ", name='" + name + '\'' +
                ", headImg='" + headImg + '\'' +
                '}';
    }
}
