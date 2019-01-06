package com.yiyue.store.module.mine.wallet.withdraw;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.api.WalletInfoApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/10/8.
 */
public class WithdrawPresenter extends BasePresenter<IWithdrawView> {
    /**
     * 微信提现
     *
     * code (string, optional):
     * money (integer, optional): 充值金额
     * */
    public void cash2Wx(double money) {
        new WalletInfoApi().cash2Wx( money, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                // callback
                getMvpView().onWxCashSuccess();
            }
        });
    }

    /**
     * 支付宝提现
     *
     * money (integer, optional): 充值金额
     * */
    public void cash2ALi(double money) {
        new WalletInfoApi().cash2ALi( money, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                // callback
                getMvpView().onALiCashSuccess();
            }
        });
    }

    /**
     * 提现
     * accountId 账户ID
     * money (integer, optional): 充值金额
     * */
    public void toCash(double money, String accountId, Context context) {
        new WalletInfoApi().toCash( money,accountId, new YLRxSubscriberHelper<BaseResponse>(context, true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("提现成功");
                // callback
                getMvpView().onALiCashSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().showToast("提现失败");
            }

        });
    }

    //验证支付密码
    public void checkPayWord(String pwd,Context context) {
        if (TextUtils.isEmpty(pwd)) {
            getMvpView().showToast("支付密码不能为空");
            return;
        }
        new SettingsApi().checkPayWord(pwd,new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().checkPasswordSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    //支付密码状态
    public void initPayWord(Context context) {
        new SettingsApi().initPayWord(new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse CoinInfoResult) {
                getMvpView().oninitPayWordInfoSuccess(CoinInfoResult.getData()+"");
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
            }
        });
    }


}
