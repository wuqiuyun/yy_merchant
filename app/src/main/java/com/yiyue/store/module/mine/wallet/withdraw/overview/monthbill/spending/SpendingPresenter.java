package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.spending;


import com.yiyue.store.api.BillApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.MonthSumResult;

/**
 * Created by wqy on 2018/12/18.
 */

public class SpendingPresenter extends BasePresenter<ISpendingView> {

    /**
     * 支出总计
     * @param month 年月份:2018-12
     */
    public void getMonthSumOut(String month) {
        new BillApi().getMonthSumOut(month, new YLRxSubscriberHelper<MonthSumResult>() {
            @Override
            public void _onNext(MonthSumResult monthSumResult) {
                if (null != monthSumResult.getData()) {
                    getMvpView().getMonthSumOut(monthSumResult.getData());
                }
            }
        });
    }


}
