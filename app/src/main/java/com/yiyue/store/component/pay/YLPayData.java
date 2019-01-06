package com.yiyue.store.component.pay;

import com.yl.core.component.pay.PayCallback;
import com.yl.core.component.pay.PayChannel;
import com.yl.core.component.pay.PayData;

/**
 * 这里面重新封装了一下，把支付数据和支付渠道封装到一个类中
 * <p>
 * Created by zm on 2018/10/24.
 */
public class YLPayData {
    public PayChannel mPayChannel; // 支付渠道
    public PayData mPayData; // 支付数据
    public PayCallback mPayCallback; // 支付回调

    public YLPayData(PayChannel payChannel, PayData payData) {
        mPayChannel = payChannel;
        mPayData = payData;
    }

    public void setPayCallback(PayCallback payCallback) {
        mPayCallback = payCallback;
    }
}
