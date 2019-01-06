package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.income;


import com.yiyue.store.api.BillApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.MonthSumResult;

/**
 * Created by wqy on 2018/12/18.
 */

public class IncomePresenter extends BasePresenter<IIncomeView> {

    /**
     * 收入总计
     * @param type 1百分环图，2柱形图
     * @param month 年月份:2018-12
     */
    public void getMonthSumIn(String month,String type) {
        new BillApi().getMonthSumIn(month, type, new YLRxSubscriberHelper<MonthSumResult>() {
            @Override
            public void _onNext(MonthSumResult monthSumResult) {
                if (null != monthSumResult.getData()) {
                    getMvpView().getMonthSumIn(monthSumResult.getData());
                }
            }
        });
    }


}
