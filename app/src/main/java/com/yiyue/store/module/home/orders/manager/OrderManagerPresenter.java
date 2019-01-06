package com.yiyue.store.module.home.orders.manager;

import com.yiyue.store.api.StoreManageApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.GetStylistManagerResult;

/**
 * Created by zm on 2018/12/17.
 */
public class OrderManagerPresenter extends BasePresenter<IOrderManagerView> {

    /**
     * 获取订单数据
     * @param date
     */
    public void getOrderDatas(String date) {
        new StoreManageApi().timemanage(date, new YLRxSubscriberHelper<GetStylistManagerResult>() {
            @Override
            public void _onNext(GetStylistManagerResult getStylistManagerResult) {
                getMvpView().setDatas(getStylistManagerResult.getData());
            }
        });
    }
}
