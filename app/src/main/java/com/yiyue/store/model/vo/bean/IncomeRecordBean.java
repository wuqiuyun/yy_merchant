package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2019/1/4.
 */
public class IncomeRecordBean {

    private String name;
    private double income;
    private double monthincome;
    private long time;
    private int level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getMonthincome() {
        return monthincome;
    }

    public void setMonthincome(double monthincome) {
        this.monthincome = monthincome;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
