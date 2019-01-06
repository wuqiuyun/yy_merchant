package com.yl.core.component.pay.data;

import android.text.TextUtils;

import com.yl.core.component.pay.PayData;
import com.yl.core.component.pay.inf.IPayData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zm on 2018/10/24.
 */
public class WeiXinPayData implements IPayData<WeiXinPayData> {
    public String appId;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String packageValue;
    public String timeStamp;
    public String sign;


    public WeiXinPayData(PayData payData) {
        initPayData(payData);
    }

    /**
     *
     * @param payData
     */
    private void initPayData(PayData payData) {
        String sign = payData.getSign();
        try {
            JSONObject jsonObject = new JSONObject(sign);
            this.appId = jsonObject.getString("appId");
            this.partnerId = jsonObject.getString("partnerid");
            this.prepayId = jsonObject.getString("prepayid");
            this.packageValue = "Sign=WXPay";
            this.nonceStr = jsonObject.getString("nonceStr");
            this.timeStamp = jsonObject.getString("timestamp");
            this.sign = jsonObject.getString("sign");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WeiXinPayData getPayData() {
        return this;
    }

    @Override
    public boolean checkPayData() {
        if (TextUtils.isEmpty(appId)
                || TextUtils.isEmpty(this.partnerId)
                || TextUtils.isEmpty(this.prepayId)
                || TextUtils.isEmpty(this.nonceStr)
                || TextUtils.isEmpty(this.partnerId)
                || TextUtils.isEmpty(this.timeStamp)
                || TextUtils.isEmpty(this.sign)
                || TextUtils.isEmpty(this.packageValue)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WeiXinPayData{" +
                "appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
