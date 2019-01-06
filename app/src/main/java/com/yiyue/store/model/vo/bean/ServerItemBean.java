package com.yiyue.store.model.vo.bean;

import java.util.List;

/**
 * Created by lvlong on 2018/11/1.
 */
public class ServerItemBean {

    /**
     * serviceId : 2
     * serviceName : 烫发
     * type : 0
     * price : 24
     * deadline : 7
     * sell : 0
     * storeLimit : 0
     * begintime : 
     * endtime : 
     * online : 1
     * pictures : ["xxx.jpg"]
     * priceType : 1
     * fromPrice : null
     * toPrice : null
     */

    private String serviceId;
    private String serviceName;
    private String type;
    private String price;
    private String deadline;
    private String sell;
    private int storeLimit;
    private String begintime;
    private String endtime;
    private String online;
    private String priceType;
    private String fromPrice;
    private String toPrice;
    private List<String> pictures;
    private String isoption ;

    public String getIsoption() {
        return isoption;
    }

    public void setIsoption(String isoption) {
        this.isoption = isoption;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public int getStoreLimit() {
        return storeLimit;
    }

    public void setStoreLimit(int storeLimit) {
        this.storeLimit = storeLimit;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(String fromPrice) {
        this.fromPrice = fromPrice;
    }

    public String getToPrice() {
        return toPrice;
    }

    public void setToPrice(String toPrice) {
        this.toPrice = toPrice;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
