package com.yiyue.store.module.mine.wallet.withdraw.account;

import android.content.Context;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.api.StoreAuthApplyApi;
import com.yiyue.store.api.WalletInfoApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.bean.CashAliBean;
import com.yiyue.store.model.vo.bean.StoreAuthBean;
import com.yiyue.store.model.vo.result.CashAliResult;
import com.yiyue.store.model.vo.result.StoreAuthResult;
import com.yl.core.component.net.exception.ApiException;

import java.util.ArrayList;


/**
 * Created by lyj on 2018/10/30.
 */
public class AccountWithdrawPresenter extends BasePresenter<IAccountWithdrawView> {


    //当前支付宝绑定账户
    public void extractAccount(String bindType, Context context) {
        new SettingsApi().extractAccount(bindType, new YLRxSubscriberHelper<CashAliResult>(context,true) {
            @Override
            public void _onNext(CashAliResult result) {
                ArrayList<CashAliBean> cashAliBeans = result.getData();
                getMvpView().onextractBankAccountSuccess(cashAliBeans);
            }

        });
    }

    //当前账户实名信息
    public void getStoreAuthDTO(Context context) {
        new StoreAuthApplyApi().getStoreAuthDTO( new YLRxSubscriberHelper<StoreAuthResult>(context,true) {
            @Override
            public void _onNext(StoreAuthResult result) {
                StoreAuthBean cashAliBeans = result.getData();
                getMvpView().getUserInfoSuccess(cashAliBeans);
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().getUserInfoFail();
            }
        });
    }

}
