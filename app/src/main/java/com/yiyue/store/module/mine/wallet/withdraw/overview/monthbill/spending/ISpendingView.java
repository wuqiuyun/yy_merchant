package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.spending;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.MonthSumBean;

/**
 * Created by jinyan on 2018/12/21.
 */
public interface ISpendingView extends IBaseView {
    void getMonthSumOut(MonthSumBean monthSumBean);
}
