package com.yiyue.store.module.mine.wallet.incomedetail;


import com.yiyue.store.api.StoreIncomeDetailsApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.IncomeResult;

/**
 * Created by wqy on 2018/12/18.
 */

public class IncomeDetailsPresenter extends BasePresenter<IIncomeDetailsView> {

    /**
     * 收入余额明细 (根据时间查询)
     *
     * @param date
     * @param userId
     * @param pageNo
     * @param pageSize
     */
    public void getAssetDetailList(String date, String userId, int pageNo, int pageSize) {
        new StoreIncomeDetailsApi().getAssetDetailList(date, userId, pageNo, pageSize, new YLRxSubscriberHelper<IncomeResult>() {
            @Override
            public void _onNext(IncomeResult result) {
                if (null != result.getData()) {
                    getMvpView().getAssetDetailSuccess(result.getData());
                } else {
                    getMvpView().getAssetDetailFail();
                }
            }
        });
    }

    /**
     * 收入余额明细 (根据时间查询)
     *
     * @param date
     * @param pageNo
     * @param pageSize
     */
    public void getCoinWalletDetailList(String date, int pageNo, int pageSize) {
        new StoreIncomeDetailsApi().getCoinWalletDetailList(date, AccountManager.getInstance().getUserId(), pageNo, pageSize, new YLRxSubscriberHelper<IncomeResult>() {
            @Override
            public void _onNext(IncomeResult result) {
                if (null != result.getData()) {
                    getMvpView().getCoinDetailSuccess(result.getData());
                } else {
                    getMvpView().getCoinDetailFail();
                }
            }
        });
    }

}
