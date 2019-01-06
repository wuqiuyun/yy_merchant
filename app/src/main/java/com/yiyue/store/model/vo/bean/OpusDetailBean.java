package com.yiyue.store.model.vo.bean;


import java.util.List;

/**
 * Created by Lizhuo on 2018/10/24.
 * 作品详情
 */
public class OpusDetailBean {

    private int collection;
    private String describe;
    private boolean isCollection;
    private long opusId;
    private int pageview;
    private List<String> pictrue;
    private int reposted;

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public long getOpusId() {
        return opusId;
    }

    public void setOpusId(long opusId) {
        this.opusId = opusId;
    }

    public int getPageview() {
        return pageview;
    }

    public void setPageview(int pageview) {
        this.pageview = pageview;
    }

    public List<String> getPictrue() {
        return pictrue;
    }

    public void setPictrue(List<String> pictrue) {
        this.pictrue = pictrue;
    }

    public int getReposted() {
        return reposted;
    }

    public void setReposted(int reposted) {
        this.reposted = reposted;
    }
}
