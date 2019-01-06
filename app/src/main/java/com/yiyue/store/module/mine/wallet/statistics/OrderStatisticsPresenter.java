package com.yiyue.store.module.mine.wallet.statistics;

import com.yiyue.store.api.StoreOrderApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.OrderChartResult;
import com.yiyue.store.model.vo.result.StoreSuccessOrdersResult;
import com.yiyue.store.model.vo.result.getStoreOrderCountResult;

/**
 * Created by lvlong on 2018/10/13.
 */
public class OrderStatisticsPresenter extends BasePresenter<OrderStatisticsView> {

    //获取门店所有的订单
    public void getStoreOrderCount(String type) {
        new StoreOrderApi().getStoreOrderCount(type, new YLRxSubscriberHelper<getStoreOrderCountResult>() {
            @Override
            public void _onNext(getStoreOrderCountResult getStoreOrderCountResult) {
                getMvpView().onGetStoreOrderCountSuccess(getStoreOrderCountResult.getData());
            }
        });
    }

    //获取完成的订单
    public void getStoreTimeSliceOrder(String type,String storeId) {
        new StoreOrderApi().getStoreTimeSliceOrder(type,storeId, new YLRxSubscriberHelper<OrderChartResult>() {
            @Override
            public void _onNext(OrderChartResult orderChartResult) {
                getMvpView().getStoreTimeSliceOrder(orderChartResult.getData());
            }
        });
    }
    //获取美发师订单统计
    public void getStoreSuccessOrders(String type,String nexus) {
        new StoreOrderApi().getStoreSuccessOrders(type,nexus, new YLRxSubscriberHelper<StoreSuccessOrdersResult>() {
            @Override
            public void _onNext(StoreSuccessOrdersResult storeSuccessOrdersResult) {
                getMvpView().getStoreSuccessOrders(storeSuccessOrdersResult.getData());
            }
        });
    }
}
