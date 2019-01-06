package com.yiyue.store.model.vo.bean;

import java.util.List;

/**
 * 店铺认证信息
 * Created by zm on 2018/10/23.
 */
public class StoreAuthApplyBean {

    private String cardno;
    private String licenseno;
    private String realname;
    private String businesslicensePath;
    private String handIDcardPath;
    private int status;
    private String updatetime;
    private String remark;
    private List<String> storephotosPaths;
    private String bannerPath;

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getLicenseno() {
        return licenseno;
    }

    public void setLicenseno(String licenseno) {
        this.licenseno = licenseno;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getBusinesslicensePath() {
        return businesslicensePath;
    }

    public void setBusinesslicensePath(String businesslicensePath) {
        this.businesslicensePath = businesslicensePath;
    }

    public String getHandIDcardPath() {
        return handIDcardPath;
    }

    public void setHandIDcardPath(String handIDcardPath) {
        this.handIDcardPath = handIDcardPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getStorephotosPaths() {
        return storephotosPaths;
    }

    public void setStorephotosPaths(List<String> storephotosPaths) {
        this.storephotosPaths = storephotosPaths;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }
}
