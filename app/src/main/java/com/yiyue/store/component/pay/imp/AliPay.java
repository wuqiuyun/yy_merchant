package com.yiyue.store.component.pay.imp;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.yl.core.component.exception.YLError;
import com.yl.core.component.pay.BasePay;
import com.yl.core.component.pay.data.AliPayData;
import com.yiyue.store.component.pay.AliPayResult;
import com.yiyue.store.component.pay.PayHelper;
import com.yiyue.store.util.ActivityUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.yiyue.store.component.pay.PayHelper.PAYRESULT_FAILURE_UNKNOW_STR;

/**
 * 支付宝支付
 * <p>
 * Created by zm on 2018/10/24.
 */
public class AliPay extends BasePay<AliPayData> implements Runnable{
    private WeakReference<Activity> mActivityWeakReference;
    private PayTask aliPay;
    private ExecutorService executors;

    public AliPay(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void pay() {
        executors = Executors.newSingleThreadExecutor();
        executors.execute(this);
    }

    @Override
    public void run() {
        /**
         * 如果Activity关闭则关闭线程池
         */
        if (ActivityUtil.isActivityFinished(mActivityWeakReference.get())) {
            shutdownNow();
            return;
        }

        if (mPayCallback == null) {
            throw new NullPointerException("PayCallBack is Null");
        }

        /**
         * 检查数据合法性
         */
        AliPayData data = mPayData.getPayData();
        if (data == null || !data.checkPayData()) {
            mPayCallback.onPayError(new YLError(PayHelper.PAYRESULT_INVALID, PayHelper.PAYRESULT_INVALID_STR));
            return;
        }

        if (aliPay == null) {
            aliPay = new PayTask(mActivityWeakReference.get());
        }

        AliPayResult payResult = new AliPayResult(aliPay.payV2(data.sign, true));

        int result_code;
        switch (payResult.getResultStatus()) {
            /**
             * 用户取消支付
             */
            case "6001":
                result_code = PayHelper.PAYRESULT_CANCELED;
                break;
            /**
             * 支付成功
             */
            case "9000":
                mPayCallback.onPaySuccess(null);
                return;
            /**
             * 网络连接出错
             */
            case "6002":
                result_code = PayHelper.PAYRESULT_FAILURE;
                break;
            /**
             * 正在处理中
             */
            case "8000":
                result_code = PayHelper.PAYRESULT_DEALING;
                break;
            /**
             * 支付失败
             */
            case "4000":
                result_code = PayHelper.PAYRESULT_FAILURE;
                break;
            /**
             * 支付失败
             */
            default:
                result_code = PayHelper.PAYRESULT_FAILURE_UNKNOW;
                break;
        }
        mPayCallback.onPayError(new YLError(
                result_code, TextUtils.isEmpty(payResult.getMemo()) ? PAYRESULT_FAILURE_UNKNOW_STR : payResult.getMemo()));
    }

    /**
     * 结束线程池
     */
    private void shutdownNow() {
        if (executors != null) {
            executors.shutdownNow();
        }
    }
}
