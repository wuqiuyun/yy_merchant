package com.yiyue.store.module.mine.wallet.bill;


import com.yiyue.store.api.BillApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.TotalBillResult;

/**
 * Created by wqy on 2018/12/18.
 */

public class BillPresenter extends BasePresenter<IBillView> {

    /**
     * 收入/支出记录
     *
     * @param month
     * @param page
     * @param size
     * @param type
     */
    public void getBill(String month, int page, int size, int type) {
        new BillApi().getBill(month, page, size, type, new YLRxSubscriberHelper<TotalBillResult>() {
            @Override
            public void _onNext(TotalBillResult totalBillResult) {
                if (null != totalBillResult.getData()) {
                    getMvpView().getBillSuccess(totalBillResult.getData());
                } else {
                    getMvpView().getBillFail();
                }
            }
        });
    }
}
