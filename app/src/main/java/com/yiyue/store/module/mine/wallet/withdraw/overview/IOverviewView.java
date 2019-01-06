package com.yiyue.store.module.mine.wallet.withdraw.overview;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.BankSumBean;
import com.yiyue.store.model.vo.bean.MonthSumBean;

import java.util.ArrayList;

/**
 * Created by jinyan on 2018/12/21.
 */
public interface IOverviewView extends IBaseView {
    //账单总计
    void getMonthSum(MonthSumBean monthSumBean);
    //显示最近提现两个账户总计
    void getNewesTwoSuccess(ArrayList<BankSumBean> beans);
}
