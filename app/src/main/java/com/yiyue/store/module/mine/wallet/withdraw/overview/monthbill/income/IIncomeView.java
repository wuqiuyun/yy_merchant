package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.income;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.MonthSumBean;

/**
 * Created by jinyan on 2018/12/21.
 */
public interface IIncomeView extends IBaseView {
    void getMonthSumIn(MonthSumBean monthSumInBean);
}
