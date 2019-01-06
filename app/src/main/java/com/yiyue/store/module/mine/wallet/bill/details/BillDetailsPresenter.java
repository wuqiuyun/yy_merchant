package com.yiyue.store.module.mine.wallet.bill.details;


import com.yiyue.store.api.BillApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.BillDetailsResult;

/**
 * Created by wqy on 2018/12/18.
 */

public class BillDetailsPresenter extends BasePresenter<IBillDetailsView> {

    public void getDetail(String billId, int inType, int type) {
        new BillApi().getDetail(billId, inType, type, new YLRxSubscriberHelper<BillDetailsResult>() {
            @Override
            public void _onNext(BillDetailsResult result) {
                if (result.getData() != null) {
                    getMvpView().getDetailSuccess(result.getData());
                } else {
                    getMvpView().getDetailFail();
                }

            }
        });
    }
}
