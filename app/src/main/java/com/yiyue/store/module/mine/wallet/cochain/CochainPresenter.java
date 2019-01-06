package com.yiyue.store.module.mine.wallet.cochain;

import android.content.Context;
import android.text.TextUtils;

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
public class CochainPresenter extends BasePresenter<ICochainView>{
    //上链
    public void cochain(String coinId, String num, Context context) {
        if (TextUtils.isEmpty(num)){
            getMvpView().showToast("请输入"+ FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getSysCoinDefault())+"数量");
        }
        new WalletInfoApi().cochain(coinId, num, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse result) {
                getMvpView().onSuccess();
            }

            @Override
            public void _onError(ApiException error) {
            }
        });
    }
}
