package com.yiyue.store.model.vo.bean;

/**
 * Created by lvlong on 2018/12/5.
 */
public class OrderChartBean {

    @Override
    public String toString() {
        return "OrderChartBean{" +
                "date='" + date + '\'' +
                ", num=" + num +
                '}';
    }

    /**
     * date : 2018-11-30
     * num : 25
     */

    private String date;
    private int num;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
