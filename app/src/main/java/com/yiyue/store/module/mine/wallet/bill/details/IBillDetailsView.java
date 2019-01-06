package com.yiyue.store.module.mine.wallet.bill.details;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.BillDetailsBean;

/**
 * Created by wqy on 2018/12/18.
 */

public interface IBillDetailsView extends IBaseView {
    void getDetailSuccess(BillDetailsBean billDetailsBean);

    void getDetailFail();
}
