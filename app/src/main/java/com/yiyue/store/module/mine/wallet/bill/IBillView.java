package com.yiyue.store.module.mine.wallet.bill;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.TotalBillBean;

import java.util.ArrayList;

/**
 * Created by wqy on 2018/12/18.
 */

public interface IBillView extends IBaseView {
    void getBillSuccess(ArrayList<TotalBillBean> list);

    void getBillFail();
}
