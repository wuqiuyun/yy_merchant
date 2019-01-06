package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/10/13.
 */
public class  WorksLabelBean {
    private String label;
    private int number;
    private long id;//发型/脸型的 id
    private int type;//筛选类型1发类2脸型

    public WorksLabelBean(String label, int number, long id, int type) {
        this.label = label;
        this.number = number;
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}