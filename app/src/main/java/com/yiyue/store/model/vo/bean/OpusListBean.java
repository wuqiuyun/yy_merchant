package com.yiyue.store.model.vo.bean;

import java.util.List;

/**
 * Created by Lizhuo on 2018/10/24.
 * 美发师 作品集
 */
public class OpusListBean {
    private List<OpusFeaTureBean> opusFeaTureList;
    private List<OpusHairstyleBean> opusHairstyleList;
    private List<StylistOpusBean> opusList;

    public List<OpusFeaTureBean> getOpusFeaTureList() {
        return opusFeaTureList;
    }

    public void setOpusFeaTureList(List<OpusFeaTureBean> opusFeaTureList) {
        this.opusFeaTureList = opusFeaTureList;
    }

    public List<OpusHairstyleBean> getOpusHairstyleList() {
        return opusHairstyleList;
    }

    public void setOpusHairstyleList(List<OpusHairstyleBean> opusHairstyleList) {
        this.opusHairstyleList = opusHairstyleList;
    }

    public List<StylistOpusBean> getOpusList() {
        return opusList;
    }

    public void setOpusList(List<StylistOpusBean> opusList) {
        this.opusList = opusList;
    }
}
