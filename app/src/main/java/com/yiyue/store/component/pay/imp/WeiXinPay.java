package com.yiyue.store.component.pay.imp;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yl.core.component.exception.YLError;
import com.yl.core.component.log.DLog;
import com.yl.core.component.pay.BasePay;
import com.yl.core.component.pay.data.WeiXinPayData;
import com.yiyue.store.component.pay.PayHelper;
import com.yiyue.store.component.pay.PayUtil;
import com.yiyue.store.model.constant.Constants;

import java.lang.ref.WeakReference;

/**
 * 微信支付
 * <p>
 * Created by zm on 2018/10/24.
 */
public class WeiXinPay extends BasePay<WeiXinPayData> implements IWXAPIEventHandler{
    private IWXAPI mIWXAPI;
    private WeakReference<Activity> mActivityWeakReference;

    public WeiXinPay(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
        mIWXAPI = WXAPIFactory.createWXAPI(mActivityWeakReference.get(), Constants.WEIXIN_APPID);
        mIWXAPI.handleIntent(mActivityWeakReference.get().getIntent(), this);
    }

    @Override
    public void pay() {
        /**
         * 未安装客户端
         */
        if (!PayUtil.checkCanPayWithWechat()) {
            mPayCallback.onPayError(new YLError(PayHelper.PAYRESULT_INVALID, PayHelper.PAYRESULT_INVALID_STR));
            return;
        }

        /**
         * 检查数据合法性
         */
        WeiXinPayData data = mPayData.getPayData();
        if (data == null || !data.checkPayData()) {
            DLog.i(data.toString());
            mPayCallback.onPayError(new YLError(PayHelper.PAYRESULT_FAILURE_UNKNOW, PayHelper.PAYRESULT_FAILURE_UNKNOW_STR));
            return;
        }

        PayReq req = new PayReq();
        req.appId = data.appId;
        req.partnerId = data.partnerId;
        req.prepayId = data.prepayId;
        req.packageValue = data.packageValue;
        req.nonceStr = data.nonceStr;
        req.timeStamp = data.timeStamp;
        req.sign = data.sign;

        mIWXAPI.registerApp(req.appId);
        mIWXAPI.sendReq(req);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (mPayCallback == null) {
            throw new NullPointerException("PayCallBack is Null");
        }

        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int extra_result_code;
            String extra_memo;
            switch (baseResp.errCode) {
                /**
                 * 支付成功
                 */
                case BaseResp.ErrCode.ERR_OK:
                    mPayCallback.onPaySuccess(null);
                    return;
                /**
                 * 支付失败
                 */
                case BaseResp.ErrCode.ERR_COMM:
                    extra_result_code = PayHelper.PAYRESULT_FAILURE;
                    extra_memo = TextUtils.isEmpty(baseResp.errStr) ? PayHelper.PAYRESULT_FAILURE_STR : baseResp.errStr;
                    break;
                /**
                 * 用户取消
                 */
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    extra_result_code = PayHelper.PAYRESULT_CANCELED;
                    extra_memo = TextUtils.isEmpty(baseResp.errStr) ? PayHelper.PAYRESULT_CANCELED_STR : baseResp.errStr;
                    break;
                /**
                 * 支付失败-发送失败
                 */
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    extra_result_code = PayHelper.PAYRESULT_FAILURE;
                    extra_memo = TextUtils.isEmpty(baseResp.errStr) ? PayHelper.PAYRESULT_FAILURE_STR : baseResp.errStr;
                    break;
                /**
                 * 支付失败-认证失败
                 */
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    extra_result_code = PayHelper.PAYRESULT_FAILURE;
                    extra_memo = TextUtils.isEmpty(baseResp.errStr) ? PayHelper.PAYRESULT_FAILURE_STR : baseResp.errStr;
                    break;
                /**
                 * 支付失败-不支持
                 */
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    extra_result_code = PayHelper.PAYRESULT_INVALID;
                    extra_memo = TextUtils.isEmpty(baseResp.errStr) ? PayHelper.PAYRESULT_INVALID_STR : baseResp.errStr;
                    break;
                /**
                 * 其他失败
                 */
                default:
                    extra_result_code = PayHelper.PAYRESULT_FAILURE_UNKNOW;
                    extra_memo = TextUtils.isEmpty(baseResp.errStr) ? PayHelper.PAYRESULT_FAILURE_UNKNOW_STR : baseResp.errStr;
                    break;
            }
            mPayCallback.onPayError(new YLError(extra_result_code, extra_memo));
        }
    }

    public void setIntent(Intent intent) {
        if(this.mIWXAPI != null) {
            this.mIWXAPI.handleIntent(intent, this);
        }
    }
}
