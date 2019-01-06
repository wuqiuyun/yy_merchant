package com.yiyue.store.module.mine.wallet.address;

import android.content.Context;
import android.text.TextUtils;

import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.WalletInfoApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by zm on 2018/10/8.
 */
public class AddressPresenter extends BasePresenter<IAddressView>{
    //绑定外部钱包地址
    public void bindWalletURL(String userId, String url, Context context) {
        if (TextUtils.isEmpty(url)){
            getMvpView().showToast("请输入钱包地址");
            return;
        }
        new WalletInfoApi().bindWalletURL(userId, url, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse result) {
                getMvpView().onSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().showToast(error.message);
            }
        });
    }
}
