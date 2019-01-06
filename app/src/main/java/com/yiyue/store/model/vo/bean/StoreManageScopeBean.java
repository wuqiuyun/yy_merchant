package com.yiyue.store.model.vo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lyj on 2018/10/23.
 */
public class StoreManageScopeBean implements Serializable{


    /**
     * catergoryNames : ["洗剪吹","烫发","染发"]
     * score : 4.8
     * scoreCount : 20
     * storeId : 1
     */

    private double score;   //综合评分
    private int scoreCount;
    private int storeId;
    private List<String> catergoryNames;
    /**
     * score : 0
     * scoretimes : 0
     * environmentScore : 0
     * serverScore : 0
     * pareEnvirtAvg : 10
     * pareServerAvg : 10
     */

    private int scoretimes; //总评分次数
    private String environmentScore;    //环境评分
    private String serverScore; //服务评分
    private int pareEnvirtAvg;  //环境平均分比较，-1低于平均分，0等于，1大于,10不显示
    private int pareServerAvg;  //服务平均分比较，-1低于平均分，0等于，1大于,10不显示

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public List<String> getCatergoryNames() {
        return catergoryNames;
    }

    public void setCatergoryNames(List<String> catergoryNames) {
        this.catergoryNames = catergoryNames;
    }

    public int getScoretimes() {
        return scoretimes;
    }

    public void setScoretimes(int scoretimes) {
        this.scoretimes = scoretimes;
    }

    public String getEnvironmentScore() {
        return environmentScore;
    }

    public void setEnvironmentScore(String environmentScore) {
        this.environmentScore = environmentScore;
    }

    public String getServerScore() {
        return serverScore;
    }

    public void setServerScore(String serverScore) {
        this.serverScore = serverScore;
    }

    public int getPareEnvirtAvg() {
        return pareEnvirtAvg;
    }

    public void setPareEnvirtAvg(int pareEnvirtAvg) {
        this.pareEnvirtAvg = pareEnvirtAvg;
    }

    public int getPareServerAvg() {
        return pareServerAvg;
    }

    public void setPareServerAvg(int pareServerAvg) {
        this.pareServerAvg = pareServerAvg;
    }
}
