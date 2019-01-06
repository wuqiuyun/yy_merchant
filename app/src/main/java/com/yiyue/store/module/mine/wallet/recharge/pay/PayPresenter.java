package com.yiyue.store.module.mine.wallet.recharge.pay;

import android.content.Context;

import com.yiyue.store.api.WalletInfoApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.pay.imp.WeiXinPay;
import com.yiyue.store.model.vo.bean.WeiXinPayBean;
import com.yiyue.store.model.vo.result.WeiXinPayResult;


/**
 * Created by zm on 2018/10/9.
 */
public class PayPresenter extends BasePresenter<IPayView> {

    /**
     * 微信充值
     *
     * code (string, optional):
     * money (integer, optional): 充值金额
     * */
    public void wxRechargeCash(String code,double money) {
        new WalletInfoApi().wxRechargeCash(code, money, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                // callback
                getMvpView().onWxRechargeCashSuccess();
            }
        });
    }

    /**
     * 支付宝充值
     *
     * money (integer, optional): 充值金额
     * */
    public void aLiRechargeCash(double money) {
        new WalletInfoApi().aLiRechargeCash( money, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                // callback
                getMvpView().onALiRechargeCashSuccess(baseResponse.getData()+"");
            }
        });
    }

    /**
     * 支付宝充值
     *
     * money (integer, optional): 充值金额
     * */
    public void aLiRechargeCashPost(String url) {
        new WalletInfoApi().aLiRechargeCashPost(url, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                // callback
                getMvpView().onALiRechargeCashSuccess(baseResponse.getData()+"");
            }
        });
    }

    /**
     * 微信充值
     *
     * money (integer, optional): 充值金额
     * */
    public void weChatCashGet(String url, Context context) {
        new WalletInfoApi().weChatCashGet(url, new YLRxSubscriberHelper<WeiXinPayResult>(context, true) {
            @Override
            public void _onNext(WeiXinPayResult baseResponse) {
                WeiXinPayBean weiXinPay = baseResponse.getData();
                // callback
                getMvpView().onWeChatCashSuccess(weiXinPay);
            }
        });
    }

}
