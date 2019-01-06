package com.yiyue.store.model.vo.bean;

/**
 * Created by zm on 2018/10/30.
 */
public class AppInfoBean {

    private String appDescribe; // 描述
    private int appId; // 应用id
    private String appName; // 应用名称
    private String appVersion; // 应用版本
    private int isUpdate; // 是否升级，1升级，0不升级 2是强更
    private String logoImg; // logo图片
    private String newAppVersionName; // 最新版本类型
    private int newAppVersionCode; // 最新版本
    private String appUrl;
    private String appSize;

    public String getAppDescribe() {
        return appDescribe;
    }

    public void setAppDescribe(String appDescribe) {
        this.appDescribe = appDescribe;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }


    public String getNewAppVersionName() {
        return newAppVersionName;
    }

    public void setNewAppVersionName(String newAppVersionName) {
        this.newAppVersionName = newAppVersionName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public int getNewAppVersionCode() {
        try {
            newAppVersionCode = Integer.valueOf(newAppVersionName.replace(".", ""));
        }catch (Exception e) {
            newAppVersionCode = 1;
        }
        return newAppVersionCode;
    }
}
