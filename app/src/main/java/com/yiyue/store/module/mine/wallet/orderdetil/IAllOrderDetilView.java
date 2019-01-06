package com.yiyue.store.module.mine.wallet.orderdetil;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.OrderDetailBean;
import com.yiyue.store.model.vo.bean.RegisterGapBetweenBean;

import java.util.List;

/**
 * Created by jinyan on 2018/12/21.
 */
public interface IAllOrderDetilView extends IBaseView {
    void findOrderDetail(List<OrderDetailBean> list);
    void getRegisterGapBetween(String data);
    void findOrderDetailFail();
}
