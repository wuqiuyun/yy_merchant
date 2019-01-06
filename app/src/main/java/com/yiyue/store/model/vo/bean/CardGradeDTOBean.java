package com.yiyue.store.model.vo.bean;

/**
 * Created by Lizhuo on 2018/10/24.
 * 美发师详情 评价相关
 */
public class CardGradeDTOBean {
    private String gradeDescrip;
    private String gradeType;
    private double point;

    public String getGradeDescrip() {
        return gradeDescrip;
    }

    public void setGradeDescrip(String gradeDescrip) {
        this.gradeDescrip = gradeDescrip;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }
}
