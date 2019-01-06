package com.yl.core.component.pay.inf;

import com.yl.core.component.pay.PayCallback;

/**
 * Created by zm on 2018/10/24.
 */
public interface IPay<T> {

    /**
     * 设置支付数据
     * @param payData
     */
    void setPayData(IPayData<T> payData);

    /**
     * 设置支付回回调
     * @param callback
     */
    void setPayCallback(PayCallback callback);

    /**
     * 进行支付
     */
    void pay();
}
