package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2019/1/2.
 */
public class RoleIncomeBean {

    /**
     * incomeall : 0
     * recommendnumber : 0
     * roletype : 0
     */

    private double incomeall;
    private int recommendnumber;
    private int roletype;

    public double getIncomeall() {
        return incomeall;
    }

    public void setIncomeall(double incomeall) {
        this.incomeall = incomeall;
    }

    public int getRecommendnumber() {
        return recommendnumber;
    }

    public void setRecommendnumber(int recommendnumber) {
        this.recommendnumber = recommendnumber;
    }

    public int getRoletype() {
        return roletype;
    }

    public void setRoletype(int roletype) {
        this.roletype = roletype;
    }
}
