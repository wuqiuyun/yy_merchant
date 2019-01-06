package com.yiyue.store.module.mine.wallet.orderdetil.userorder;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.StoreStylistBean;
import com.yiyue.store.model.vo.bean.UserOrderBean;

import java.util.ArrayList;

/**
 * Created by jinyan on 2018/12/27.
 */
  public interface IUserOrderDetilView extends IBaseView {
    void getUserOrder(ArrayList<UserOrderBean> userOrderBeans);
    void getOrderIncomeSum(StoreStylistBean storeStylistBean);
    void getFail();
}
