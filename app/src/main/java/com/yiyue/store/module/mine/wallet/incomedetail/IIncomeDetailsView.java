package com.yiyue.store.module.mine.wallet.incomedetail;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.IncomeBean;

import java.util.List;

/**
 * Created by wqy on 2018/12/18.
 */

public interface IIncomeDetailsView extends IBaseView {
    void getAssetDetailSuccess(List<IncomeBean> beanList);

    void getAssetDetailFail();

    void getCoinDetailSuccess(List<IncomeBean> beanList);

    void getCoinDetailFail();
}
