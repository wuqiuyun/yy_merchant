package com.yiyue.store.component.pay;

import android.app.Activity;
import android.os.Bundle;

import com.yl.core.component.pay.PayChannel;

/**
 * Created by zm on 2018/10/24.
 */
public class PayHelper {
    public static final int PAYRESULT_SUCCESS = 1;
    public static final String PAYRESULT_SUCCESS_STR = "支付成功";
    public static final int PAYRESULT_CANCELED = 0;
    public static final String PAYRESULT_CANCELED_STR = "取消支付";
    public static final int PAYRESULT_FAILURE = -1;
    public static final String PAYRESULT_FAILURE_STR = "支付失败";
    public static final int PAYRESULT_INVALID = -2;
    public static final String PAYRESULT_INVALID_STR = "无法初始化支付平台";
    public static final int PAYRESULT_DEALING = -3;
    public static final String PAYRESULT_DEALING_STR = "正在支付中";
    public static final int PAYRESULT_FAILURE_UNKNOW = -4;
    public static final String PAYRESULT_FAILURE_UNKNOW_STR = "unknow error!";

    public static final int REQUEST_CODE_PAYMENT = 1010;

    private static void pay(Activity activity, PayChannel payChannel, String sign, Bundle bundle) {
        PayActivity.startActivityForResult(activity, payChannel, sign, bundle, REQUEST_CODE_PAYMENT);
    }

    public static void wxpay(Activity activity, String sign) {
        wxpay(activity, sign, null);
    }

    public static void alipay(Activity activity, String sign) {
        alipay(activity, sign, null);
    }

    public static void wxpay(Activity activity, String sign, Bundle bundle) {
        pay(activity, PayChannel.WEIXIN, sign, bundle);
    }

    public static void alipay(Activity activity, String sign, Bundle bundle) {
        pay(activity, PayChannel.ALI, sign, bundle);
    }
}
