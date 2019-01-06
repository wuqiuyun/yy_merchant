package com.yiyue.store.model.vo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lyj on 2018/10/25.
 */
public class StoreCollectionBean implements Serializable{

    private int id;
    private int level;
    private String name;
    private int parentId;
    private int areaId;
    private int areaParentId;
    private int type;
    private List<StoreCollectionBean> areaList;

    public List<StoreCollectionBean> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<StoreCollectionBean> areaList) {
        this.areaList = areaList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getAreaParentId() {
        return areaParentId;
    }

    public void setAreaParentId(int areaParentId) {
        this.areaParentId = areaParentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
