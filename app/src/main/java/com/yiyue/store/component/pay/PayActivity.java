package com.yiyue.store.component.pay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.yl.core.component.exception.YLError;
import com.yl.core.component.log.DLog;
import com.yl.core.component.pay.PayCallback;
import com.yl.core.component.pay.PayChannel;
import com.yl.core.component.pay.PayData;
import com.yl.core.component.pay.inf.IPay;
import com.yiyue.store.component.pay.imp.WeiXinPay;

/**
 * Create by zm on 2018/10/24
 */
public class PayActivity extends Activity implements PayCallback{

    private static final String EXTRA_CHANNEL = "extra_channel";
    private static final String EXTRA_SIGN = "extra_sign";

    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static final String EXTRA_RESULT_CODE = "extra_result_code";
    public static final String EXTRA_MEMO = "extra_memo";

    private PayChannel payChannel;
    private String sign;
    private Bundle bundleExtra;

    private YLPay ylPay;
    private boolean onRestartFlag = false;

    /**
     *
     * @param activity
     * @param payChannel
     * @param sign
     * @param bundle
     * @param requestCode
     */
    public static void startActivityForResult(Activity activity, PayChannel payChannel, String sign, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CHANNEL, payChannel);
        intent.putExtra(EXTRA_SIGN, sign);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        if (payChannel == PayChannel.WEIXIN){
            intent.setComponent(new ComponentName(
                    activity.getPackageName(), activity.getPackageName()+ ".wxapi.WXPayEntryActivity"));
        }else {
            intent.setClass(activity, PayActivity.class);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payChannel = (PayChannel) bundle.get(EXTRA_CHANNEL);
            sign = bundle.getString(EXTRA_SIGN);
            bundleExtra = bundle.getBundle(EXTRA_BUNDLE);
        }
        toPay();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // 微信支付需实现
        IPay iPay = ylPay.getPayStrategy();
        if (ylPay!= null && iPay!=null && iPay instanceof WeiXinPay) {
            ((WeiXinPay)iPay).setIntent(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRestartFlag = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (onRestartFlag) {
            onRestartFlag = false;
            // 微信支付需实现
            IPay iPay = ylPay.getPayStrategy();
            if (ylPay != null && iPay != null && iPay instanceof WeiXinPay) {
                onPayError(new YLError(PayHelper.PAYRESULT_CANCELED, PayHelper.PAYRESULT_CANCELED_STR));
            }
        }
    }

    @Override
    public void onBackPressed() {
        onPayError(new YLError(PayHelper.PAYRESULT_CANCELED, PayHelper.PAYRESULT_CANCELED_STR));
    }

    private void toPay() {
        YLPayData ylPayData = new YLPayData(payChannel, new PayData(sign));
        ylPayData.setPayCallback(this);

        ylPay = new YLPay(this, ylPayData);
        ylPay.toPay();
    }

    @Override
    public void onPayStart() {

    }

    @Override
    public void onPaySuccess(Object data) {
        DLog.i(data);
        setResult(true, PayHelper.PAYRESULT_SUCCESS, PayHelper.PAYRESULT_SUCCESS_STR);
    }

    @Override
    public void onPayError(YLError error) {
        DLog.e(error);
        setResult(false, error.getCode(), error.getMessage());
    }

    public void setResult(boolean isPaySuccess, int code, String msg) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BUNDLE, bundleExtra);
        intent.putExtra(EXTRA_RESULT_CODE, code);
        intent.putExtra(EXTRA_MEMO, msg);
        setResult(isPaySuccess ? RESULT_OK : RESULT_CANCELED, intent);
        finish();
    }
}
