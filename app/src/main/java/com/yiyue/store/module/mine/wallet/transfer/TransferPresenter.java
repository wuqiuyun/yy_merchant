package com.yiyue.store.module.mine.wallet.transfer;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.model.local.preferences.CommonSharedPreferences;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.WalletInfoApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by zm on 2018/10/9.
 */
public class TransferPresenter extends BasePresenter<ITransferView> {
    //上链
    public void present(String amount,String fromUserId,String toUserId, Context context) {
        if (TextUtils.isEmpty(amount)){
            getMvpView().showToast("请输入"+ FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getSysCoinDefault())+"数量");
            return;
        }
        if (TextUtils.isEmpty(toUserId)||toUserId==null){
            getMvpView().showToast("请选择转赠对象");
            return;
        }
        new WalletInfoApi().present(amount, fromUserId,toUserId, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse result) {
                getMvpView().onSuccess();
            }

            @Override
            public void _onError(ApiException error) {
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
                getMvpView().showToast("支付支付密码验证失败");
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
