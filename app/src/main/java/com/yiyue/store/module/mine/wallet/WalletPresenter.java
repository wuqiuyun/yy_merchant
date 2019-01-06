package com.yiyue.store.module.mine.wallet;

import android.content.Context;

import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.WalletInfoApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.CashInfoAccountResult;
import com.yiyue.store.model.vo.result.CashInfoResult;
import com.yiyue.store.model.vo.result.CoinInfoAccountResult;
import com.yiyue.store.model.vo.result.CoinInfoResult;

/**
 * Created by zm on 2018/9/29.
 */
public class WalletPresenter extends BasePresenter<IWalletView> {

    //获取钱包余额
    public void getCashInfo() {
        new WalletInfoApi().getCashInfo(new YLRxSubscriberHelper<CashInfoResult>() {
            @Override
            public void _onNext(CashInfoResult cashInfoResult) {
                getMvpView().onGetCashInfoSuccess(cashInfoResult.getData());
            }
        });
    }

    //获取代币余额
    public void getCoinInfo() {
        new WalletInfoApi().getCoinInfo(new YLRxSubscriberHelper<CoinInfoResult>() {
            @Override
            public void _onNext(CoinInfoResult CoinInfoResult) {
                getMvpView().onGetCoinInfoSuccess(CoinInfoResult.getData());
            }
        });
    }

    //获取钱包余额 详情列表
//    public void getCashInfoAccount(int page,int size, Context context) {
//        new WalletInfoApi().getCashInfoAccount( page, size,new YLRxSubscriberHelper<CashInfoAccountResult>(context,true) {
//            @Override
//            public void _onNext(CashInfoAccountResult cashInfoResult) {
//                getMvpView().onGetCashInfoListSuccess(cashInfoResult.getData());
//            }
//
//            @Override
//            public void _onError(ApiException error) {
//                super._onError(error);
//                getMvpView().onGetCashInfoListFail();
//            }
//        });
//    }

    //获取代币余额 详情列表
//    public void getCoinInfoAccount(int page,int size, Context context) {
//        new WalletInfoApi().getCoinInfoAccount( page, size,new YLRxSubscriberHelper<CoinInfoAccountResult>(context,true) {
//            @Override
//            public void _onNext(CoinInfoAccountResult CoinInfoResult) {
//                getMvpView().onGetCoinInfoListSuccess(CoinInfoResult.getData());
//            }
//
//            @Override
//            public void _onError(ApiException error) {
//                super._onError(error);
//                getMvpView().onGetCoinInfoListFail();
//            }
//        });
//    }
}
