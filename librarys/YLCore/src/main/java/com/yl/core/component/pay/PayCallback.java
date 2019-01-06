package com.yl.core.component.pay;

import com.yl.core.component.exception.YLError;

/**
 * 支付回调
 * Created by zm on 2018/10/24.
 */
public interface PayCallback {

    /**
     * 开始支付
     */
    void onPayStart();

    /**
     * 支付成功 如果有数据进行返回
     * @param data
     */
    void onPaySuccess(Object data);

    /**
     * 支付错误
     * @param error
     */
    void onPayError(YLError error);
}
