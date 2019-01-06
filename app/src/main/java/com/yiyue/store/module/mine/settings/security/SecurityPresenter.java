package com.yiyue.store.module.mine.settings.security;

import android.content.Context;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.SecurityInfoResult;

/**
 * Created by lvlong on 2018/10/8.
 */
public class SecurityPresenter extends BasePresenter<ISecurityView> {

    //获取账户安全详情
    public void getAccountInfoAccount() {
        new SettingsApi().getAccountSafeInfo(new YLRxSubscriberHelper<SecurityInfoResult>() {
            @Override
            public void _onNext(SecurityInfoResult CoinInfoResult) {
                getMvpView().onAccountInfoSuccess(CoinInfoResult.getData());
            }
        });
    }

    //微信绑定
    public void bindWXAccount(String code, Context context) {
        new SettingsApi().bindWXAccount(code, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse CoinInfoResult) {
                getMvpView().onBindWxSuccess();
            }
        });
    }

    //支付密码状态
    public void initPayWord(Context context) {
        new SettingsApi().initPayWord(new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse CoinInfoResult) {
                getMvpView().oninitPayWordInfoSuccess(CoinInfoResult.getData().toString()+"");
            }

            @Override
            public void onError(Throwable throwable) {
                getMvpView().showToast("获取支付密码状态失败");
            }
        });
    }

}
