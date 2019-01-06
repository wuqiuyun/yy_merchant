package com.yiyue.store.module.mine.wallet.statistics;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.OrderChartBean;
import com.yiyue.store.model.vo.bean.StoreOrderBean;
import com.yiyue.store.model.vo.bean.StoreSuccessOrdersBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvlong on 2018/10/13.
 */
public interface OrderStatisticsView extends IBaseView {

    //所有订单
    void onGetStoreOrderCountSuccess(StoreOrderBean bean);

    //订单统计
    void getStoreTimeSliceOrder(List<List<OrderChartBean>> orderChartBeans);
    //美发师订单统计
    void getStoreSuccessOrders(ArrayList<StoreSuccessOrdersBean> storeSuccessOrdersBeans);

}
