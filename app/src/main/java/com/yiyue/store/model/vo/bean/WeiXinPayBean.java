package com.yiyue.store.model.vo.bean;

/**
 * Created by lyj on 2018/11/9.
 */

public class WeiXinPayBean {

    /**
     * appId : wxda7128307372278d
     * nonceStr : 4fc41324cdfa35c1b9e2f8d59371eb20
     * pkg : Sign=WXPay
     * sign : DB3EAAEFD8533AFEC426A5C73C821D06
     * partnerid : 1516804261
     * prepayid : wx091522245898271fc532317c3767208299
     * timestamp : 1541748140
     */

    private String appId;
    private String nonceStr;
    private String pkg;
    private String sign;
    private String partnerid;
    private String prepayid;
    private String timestamp;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}